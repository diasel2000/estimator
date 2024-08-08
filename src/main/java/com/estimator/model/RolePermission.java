package com.estimator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RolePermission(id=null, role=" + (role != null ? role.getRoleName() : "null") +
                ", permission=" + (permission != null ? permission.getPermissionName() : "null") + ")";
    }

}


