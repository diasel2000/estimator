package com.estimator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

// Composite key class for RolePermission
@Data
@NoArgsConstructor
@Embeddable
public class RolePermissionKey implements Serializable {
    @Column(name = "roleID")
    private Integer roleID;

    @Column(name = "permissionID")
    private Integer permissionID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermissionKey that = (RolePermissionKey) o;
        return roleID.equals(that.roleID) && permissionID.equals(that.permissionID);
    }

    @Override
    public int hashCode() {
        return 31 * roleID.hashCode() + permissionID.hashCode();
    }
}