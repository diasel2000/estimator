package com.estimator.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RolePermissionDTO {
    private RolePermissionKeyDTO id;
    private RoleDTO role;
    private PermissionDTO permission;
}