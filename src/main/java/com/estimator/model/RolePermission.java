package com.estimator.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "RolePermissions")
public class RolePermission {
    @EmbeddedId
    private RolePermissionKey id;

    @ManyToOne
    @MapsId("roleID")
    @JoinColumn(name = "roleID")
    private Role role;

    @ManyToOne
    @MapsId("permissionID")
    @JoinColumn(name = "permissionID")
    private Permission permission;
}

