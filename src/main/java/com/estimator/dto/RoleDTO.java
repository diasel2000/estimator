package com.estimator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    private Long roleID;
    private String roleName;
    private String description;
}