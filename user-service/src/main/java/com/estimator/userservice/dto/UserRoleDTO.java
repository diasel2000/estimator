package com.estimator.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleDTO {
    private UserRoleKeyDTO id;
    private UserDTO user;
    private RoleDTO role;
}
