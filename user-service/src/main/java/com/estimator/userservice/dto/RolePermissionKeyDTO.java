package com.estimator.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RolePermissionKeyDTO {
    private Long roleID;
    private Long permissionID;
}