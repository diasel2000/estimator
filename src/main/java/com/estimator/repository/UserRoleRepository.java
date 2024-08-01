package com.estimator.repository;

import com.estimator.model.UserRole;
import com.estimator.model.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
}
