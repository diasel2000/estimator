package com.estimator.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class RolePermissionKey implements Serializable {
    @Column(name = "roleID")
    private Long roleID;

    @Column(name = "permissionID")
    private Long permissionID;

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