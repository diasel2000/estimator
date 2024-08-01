package com.estimator.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "UserRoles")
public class UserRole {
    @EmbeddedId
    private UserRoleKey id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "userID")
    private User user;

    @ManyToOne
    @MapsId("roleID")
    @JoinColumn(name = "roleID")
    private Role role;
}


