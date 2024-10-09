package com.github.bccyben.common.repository;

import jakarta.persistence.LockModeType;
import com.github.bccyben.common.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfoEntity, String>, JpaSpecificationExecutor<UserInfoEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserInfoEntity> findAndLockByUserId(String userId);

}
