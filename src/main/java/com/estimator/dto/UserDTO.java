package com.estimator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long userID;
    private String username;
    private String email;
    private String googleID;
    private LocalDateTime createdAt;
    private SubscriptionDTO subscription;
    private Set<UserRoleDTO> userRoles;
}
