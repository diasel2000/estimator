package com.estimator.userservice.repository;

import com.estimator.userservice.model.RolePermission;
import com.estimator.userservice.model.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionKey> {
}