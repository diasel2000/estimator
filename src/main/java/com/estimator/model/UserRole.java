package com.estimator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Model for UserRole with composite key
@Data
@NoArgsConstructor
@Entity
@Table(name = "userroles")
public class UserRole {
    @EmbeddedId
    private UserRoleKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userID")
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleID")
    @JoinColumn(name = "roleID")
    private Role role;
}