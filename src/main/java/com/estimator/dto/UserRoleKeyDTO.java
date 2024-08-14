package com.estimator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleKeyDTO {
    private Long userID;
    private Long roleID;
}