package com.estimator.repository;

import com.estimator.model.RolePermission;
import com.estimator.model.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionKey> {
}