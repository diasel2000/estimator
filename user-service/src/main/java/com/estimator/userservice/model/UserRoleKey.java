package com.estimator.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class UserRoleKey implements Serializable {
    @Column(name = "userID")
    private Long userID;

    @Column(name = "roleID")
    private Long roleID;

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