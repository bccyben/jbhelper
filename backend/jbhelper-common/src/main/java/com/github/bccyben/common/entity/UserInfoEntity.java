package com.github.bccyben.common.entity;

import jakarta.persistence.*;
import com.github.bccyben.common.cons.UserRole;
import com.github.bccyben.common.domain.UserRoleConverter;
import com.github.bccyben.common.model.common.UserInfoModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * ユーザ情報
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfoEntity {
    /**
     * ユーザID
     */
    @Id
    @Column(name = "user_id", nullable = false, length = 32)
    private String userId;

    /**
     * ユーザ名
     */
    @Column(name = "user_name", nullable = false, length = 32)
    private String userName;

    /**
     * 名前
     */
    @Column(name = "given_name")
    private String givenName;

    /**
     * 苗字
     */
    @Column(name = "family_name")
    private String familyName;

    /**
     * メール
     */
    @Column(name = "mail_address")
    private String mailAddress;

    /**
     * 作成日時
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /**
     * 権限コード
     *
     * @see UserRole
     */
    @Column(name = "role_id", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRole roleId;

    public UserInfoEntity(UserInfoModel model) {
        this.setUserId(model.getUserId().toLowerCase());
        this.of(model);
    }

    public void of(UserInfoModel model) {
        this.setRoleId(model.getUserRole());
        this.setUserName(model.getUserName());
        this.setGivenName(model.getGivenName());
        this.setFamilyName(model.getFamilyName());
        this.setMailAddress(model.getMailAddress());
        this.update();
    }

    public void update() {
        final LocalDateTime now = LocalDateTime.now();
        this.setCreatedAt(now);
        this.setUpdateTime(now);
    }
}
