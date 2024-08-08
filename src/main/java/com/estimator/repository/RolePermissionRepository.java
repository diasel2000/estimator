package com.estimator.repository;

import com.estimator.model.RolePermission;
import com.estimator.model.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionKey> {
}