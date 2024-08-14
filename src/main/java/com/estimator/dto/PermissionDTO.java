package com.estimator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionDTO {
    private Long permissionID;
    private String permissionName;
    private String description;
}