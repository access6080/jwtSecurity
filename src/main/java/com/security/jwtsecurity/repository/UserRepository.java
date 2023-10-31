package com.security.jwtsecurity.repository;

import com.security.jwtsecurity.models.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserById(Long id);

    Optional<UserEntity> findUserByUsername(String username);
}
