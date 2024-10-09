package com.github.bccyben.common.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.core.Response;
import com.github.bccyben.common.config.KeycloakConfig;
import com.github.bccyben.common.exception.InvalidRequestException;
import com.github.bccyben.common.exception.JbhelperException;
import com.github.bccyben.common.model.common.UserInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * keycloak系機能
 */
@Service
@ConditionalOnProperty(value = "keycloak.enabled", havingValue = "true", matchIfMissing = false)
public class KeycloakService {

    private Keycloak keycloak;

    private RealmResource realmResource;

    private AuthzClient authzClient;

    @Autowired
    private KeycloakConfig config;


    @PostConstruct
    private void init() {
        // keycloak server
        keycloak = Keycloak.getInstance(
                config.getUrl(),
                config.getAdminRealm(),
                config.getUsername(),
                config.getPassword(),
                "admin-cli");
        realmResource = keycloak.realm(config.getTargetRealm());
        // keycloak client
        Configuration configuration = new Configuration();
        configuration.setRealm(config.getTargetRealm());
        configuration.setAuthServerUrl(config.getUrl());
        configuration.setResource(config.getClientId());
        Map<String, Object> map = new HashMap<>();
        map.put("secret", "secret");
        configuration.setCredentials(map);
        authzClient = AuthzClient.create(configuration);
    }

    public void createUser(UserInfoModel user) {
        UserRepresentation rep = new UserRepresentation();
        rep.setUsername(user.getUserId());
        updateUserRepresentation(rep, user, false);
        rep.setEnabled(true);
        // メール認証はkeycloak側で行う
        Response response = realmResource.users().create(rep);
        if (response.getStatus() == HttpStatus.CREATED.value()) {
            return;
        }
        handleKeycloakError(response);
    }

    private void handleKeycloakError(Response response) {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        if (status.is4xxClientError()) {
            throw new InvalidRequestException(response.readEntity(String.class));
        }
        throw new JbhelperException(status, response.readEntity(String.class));
    }

    public void updateUser(UserInfoModel user, boolean isSelfEditing, boolean validPassword) {
        UserRepresentation rep = search(user.getUserId());
        rep.setEnabled(true);
        updateUserRepresentation(rep, user, isSelfEditing);
        final boolean isPwChanged = changePassword(rep, user, validPassword);
        UserResource userResource = realmResource.users().get(rep.getId());
        userResource.update(rep);
        if (isPwChanged) {
            doLogout(user.getUserId());
        }
    }


    public void validPassword(String userId, String password) {
        if (StringUtils.isBlank(password)) {
            throw new InvalidRequestException("パスワードが正しくありません。\n The password is incorrect.");
        }
        try {
            authzClient.obtainAccessToken(userId, password);
        } catch (HttpResponseException e) {
            throw new InvalidRequestException("パスワードが正しくありません。\n The password is incorrect.");
        }
    }

    public void changePassword(String userName, String password) {
        UserRepresentation rep = search(userName);
        rep.setCredentials(toCredential(password));
        UserResource userResource = realmResource.users().get(rep.getId());
        userResource.update(rep);
    }

    /**
     * パスワード変更
     * 
     * @param rep
     * @param user
     * @param validPassword
     * @return 変更されたかどうか
     */
    public boolean changePassword(UserRepresentation rep, UserInfoModel user, boolean validPassword) {
        if (StringUtils.isBlank(user.getPassword())) {
            return false;
        }
        if (validPassword) {
            validPassword(user.getUserId(), user.getCurrentPassword());
        }
        rep.setCredentials(toCredential(user.getPassword()));
        return true;
    }


    public void deleteUser(String userName) {
        UserRepresentation rep = search(userName);
        UserResource userResource = realmResource.users().get(rep.getId());
        userResource.remove();
    }

    public UserRepresentation search(String userName) {
        List<UserRepresentation> users = realmResource.users().search(userName, true);
        if (users.isEmpty()) {
            throw new JbhelperException("not found");
        }
        return users.get(0);
    }

    public List<UserSessionRepresentation> getSessionsByUserName(String userName) {
        final String userId = search(userName).getId();
        return realmResource.users().get(userId).getUserSessions();
    }

    public void doLogout(String userName) {
        final String userId = search(userName).getId();
        var user = realmResource.users().get(userId);
        doLogout(user);
    }

    private void doLogout(UserResource user) {
        if (!user.getConsents().isEmpty()) {
            user.logout();
            user.revokeConsent(config.getClientId());
        }
    }


    private static void updateUserRepresentation(UserRepresentation rep, UserInfoModel user, boolean isSelfEditing) {
        rep.setFirstName(user.getGivenName());
        rep.setLastName(user.getFamilyName());
        rep.setEmail(user.getMailAddress());
        if (!StringUtils.isEmpty(user.getPassword())) {
            rep.setCredentials(toCredential(user.getPassword()));
            if (!isSelfEditing) {
                rep.setRequiredActions(Collections.singletonList(UserModel.RequiredAction.UPDATE_PASSWORD.name()));
            }
        }
    }

    private static List<CredentialRepresentation> toCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return Collections.singletonList(credential);
    }

    @PreDestroy
    public void destroy() {
        if (keycloak.isClosed()) {
            return;
        }
        keycloak.close();
    }
}
