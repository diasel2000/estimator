package com.estimator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

// Composite key class for UserRole
@Data
@NoArgsConstructor
@Embeddable
public class UserRoleKey implements Serializable {
    @Column(name = "userID")
    private Integer userID;

    @Column(name = "roleID")
    private Integer roleID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleKey that = (UserRoleKey) o;
        return userID.equals(that.userID) && roleID.equals(that.roleID);
    }

    @Override
    public int hashCode() {
        return 31 * userID.hashCode() + roleID.hashCode();
    }
}