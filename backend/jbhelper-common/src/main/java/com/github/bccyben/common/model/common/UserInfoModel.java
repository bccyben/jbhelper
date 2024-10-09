package com.github.bccyben.common.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import com.github.bccyben.common.cons.UserRole;
import com.github.bccyben.common.entity.UserInfoEntity;
import com.github.bccyben.common.validator.ValidMust;
import com.github.bccyben.common.validator.ValidRegex;
import com.github.bccyben.common.validator.group.Create;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserInfoModel {

    @ValidMust(field = "ユーザID")
    private String userId;

    @ValidMust(field = "ユーザ名")
    private String userName;

    @ValidRegex(field = "メール", regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @ValidMust(field = "メール")
    private String mailAddress;

    @ValidMust(field = "名前")
    private String givenName;

    @ValidMust(field = "苗字")
    private String familyName;

    private LocalDateTime createdAt;

    private LocalDateTime updateTime;

    @ValidMust(field = "権限ID")
    @Schema(implementation = UserRole.class)
    private UserRole userRole;

    @ValidMust(field = "パスワード", groups = { Create.class })
    private String password;

    private String currentPassword;

    /**
     * {@link UserInfoEntity}から{@link UserInfoModel}
     *
     * @param entity
     */
    public UserInfoModel(UserInfoEntity entity) {
        this.setUserId(entity.getUserId());
        this.setUserName(entity.getUserName());
        this.setMailAddress(entity.getMailAddress());
        this.setCreatedAt(entity.getCreatedAt());
        this.setUpdateTime(entity.getUpdateTime());
        this.setUserRole(entity.getRoleId());
    }

}
