package com.estimator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RolePermissionDTO {
    private RolePermissionKeyDTO id;
    private RoleDTO role;
    private PermissionDTO permission;
}