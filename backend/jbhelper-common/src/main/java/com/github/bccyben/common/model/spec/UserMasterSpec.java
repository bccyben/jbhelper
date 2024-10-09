package com.github.bccyben.common.model.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import com.github.bccyben.common.cons.UserRole;
import com.github.bccyben.common.entity.UserInfoEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

/**
 * ユーザ一覧検索用spec
 */
@AllArgsConstructor
public class UserMasterSpec implements Specification<UserInfoEntity> {

    private final List<UserRole> roleList;

    private final String userId;

    private final String filter;


    @Override
    public Predicate toPredicate(Root<UserInfoEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        // ユーザ名部分一致
        if (StringUtils.isNotBlank(filter)) {
            predicates.add(
                    builder.or(builder.like(root.get("userName"), "%" + filter + "%")));
        }
        // ユーザID検索
        if (StringUtils.isNotBlank(userId)) {
            predicates.add(
                    builder.equal(root.get("userId"), userId));
        }
        // 権限コード検索
        if (this.roleList != null && !this.roleList.isEmpty()) {
            predicates.add(root.get("roleId").in(this.roleList));
        }
        return builder.and(predicates.toArray(Predicate[]::new));
    }
}
