package com.github.bccyben.common.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.github.bccyben.common.domain.SimplePage;
import com.github.bccyben.common.domain.UserMasterPageRequest;
import com.github.bccyben.common.entity.*;
import com.github.bccyben.common.exception.ExpectDataNotFound;
import com.github.bccyben.common.exception.InvalidRequestException;
import com.github.bccyben.common.message.MessageIdConstants;
import com.github.bccyben.common.message.Messages;
import com.github.bccyben.common.model.common.UserInfoModel;
import com.github.bccyben.common.model.spec.UserMasterSpec;
import com.github.bccyben.common.repository.UserInfoRepo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserInfoService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    @Autowired(required = false)
    private KeycloakService keycloakService;

    @Autowired
    private Messages messages;

    /**
     * ユーザ新規作成
     * 
     * @param model
     */
    @Transactional
    public void create(UserInfoModel model) {
        if (userInfoRepo.existsById(model.getUserId())) {
            throw new InvalidRequestException(messages.getMessage(MessageIdConstants.MESSAGE_E1008, model.getUserId()));
        }
        // save db
        UserInfoEntity entity = new UserInfoEntity(model);
        userInfoRepo.saveAndFlush(entity);
        if (keycloakService != null) {
            keycloakService.createUser(model);
        }
    }

    /**
     * ユーザ編集、サンプルコードなので現パスワード不要
     * 
     * @param model
     */
    @Transactional
    public void update(UserInfoModel model) {
        UserInfoEntity entity = userInfoRepo.findAndLockByUserId(model.getUserId())
                .orElseThrow(() -> {
                    throw new ExpectDataNotFound(
                            messages.getMessage(MessageIdConstants.MESSAGE_E0005, model.getUserId(),
                                    "ユーザー", "user"));
                });
        // save db
        entity.of(model);
        userInfoRepo.saveAndFlush(entity);
        if(keycloakService!=null){
            keycloakService.updateUser(model, true, false);
        }
        
    }

    /**
     * ユーザ詳細
     * 
     * @param userId
     * @return
     */
    @Transactional
    public UserInfoModel findById(String userId) {
        UserInfoEntity entity = userInfoRepo.findById(userId.toLowerCase())
                .orElseThrow(() -> {
                    throw new ExpectDataNotFound(
                            messages.getMessage(MessageIdConstants.MESSAGE_E0005, userId, "ユーザー", "user"));
                });
        return new UserInfoModel(entity);
    }

    /**
     * ユーザ一覧
     * 
     * @param spec
     * @param page
     * @return
     */
    @Transactional
    public SimplePage<UserInfoModel> findAll(UserMasterSpec spec, UserMasterPageRequest page) {
        Page<UserInfoEntity> result = userInfoRepo.findAll(spec, page.toPageable());
        return new SimplePage<>(result.map(UserInfoModel::new));
    }

}
