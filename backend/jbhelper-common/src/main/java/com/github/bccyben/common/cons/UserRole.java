package com.github.bccyben.common.cons;

import java.util.ArrayList;
import java.util.List;

/**
 * ユーザ権限
 */
public enum UserRole {
    /**
     * システム管理者
     */
    SYS_ADMIN(1, 10d),

    /**
     * PF管理者
     */
    PF_ADMIN(2, 20d),

    /**
     * PF承認者
     */
    PF_MANAGER(3, 30d),

    /**
     * PFオペレーター
     */
    PF_OPERATOR(4, 40d),

    /**
     * PF管理閲覧者
     */
    PF_READER(5, 50d),

    /**
     * 一般ユーザー(出品者)
     */
    ORDINARY_USER(9, 90d),

    /**
     * 非ログインユーザー
     */
    ANONYMOUS_USER(99, 990d);

    private final int id;

    private final Double accessLevel;

    UserRole(int id, Double accessLevel) {
        this.id = id;
        this.accessLevel = accessLevel;
    }

    /**
     * roleIdから{@link UserRole}に変換
     *
     * @param value
     * @return
     */
    public static UserRole ofId(Integer value) {
        if (value == null) {
            return ANONYMOUS_USER;
        }
        for (UserRole role : values()) {
            if (role.id == value) {
                return role;
            }
        }
        return ANONYMOUS_USER;
    }

    public static List<UserRole> getAdminRoleList() {
        return new ArrayList<>(List.of(
                UserRole.SYS_ADMIN,
                UserRole.PF_ADMIN,
                UserRole.PF_MANAGER,
                UserRole.PF_OPERATOR,
                UserRole.PF_READER));
    }

    public static UserRole[] getAdminRoleArray() {
        return new UserRole[]{
                UserRole.SYS_ADMIN,
                UserRole.PF_ADMIN,
                UserRole.PF_MANAGER,
                UserRole.PF_OPERATOR,
                UserRole.PF_READER
        };
    }

    /**
     * roleIdを取得
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * ログインユーザはanotherより権限は高いかどうか
     *
     * @param another
     * @return
     */
    public boolean isHigherThan(UserRole another) {
        if (another == null) {
            return true;
        }
        return accessLevel < another.accessLevel;
    }

    /**
     * ログインユーザはanotherより権限は高いもしくは等しいかどうか
     *
     * @param another
     * @return
     */
    public boolean isHigherThanOrEqual(UserRole another) {
        if (another == null) {
            return true;
        }
        return accessLevel <= another.accessLevel;
    }

    /**
     * ログインユーザはanotherより権限は低いかどうか
     *
     * @param another
     * @return
     */
    public boolean isLowerThan(UserRole another) {
        if (another == null) {
            return false;
        }
        return accessLevel > another.accessLevel;
    }

    /**
     * ログインユーザはanotherより権限は低いもしくは等しいかどうか
     *
     * @param another
     * @return
     */
    public boolean isLowerThanOrEqual(UserRole another) {
        if (another == null) {
            return false;
        }
        return accessLevel >= another.accessLevel;
    }
}
