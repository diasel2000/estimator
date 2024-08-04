package com.estimator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Model for RolePermission with composite key
@Data
@NoArgsConstructor
@Entity
@Table(name = "rolepermissions")
public class RolePermission {
    @EmbeddedId
    private RolePermissionKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleID")
    @JoinColumn(name = "roleID")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionID")
    @JoinColumn(name = "permissionID")
    private Permission permission;
}

