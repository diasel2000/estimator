package com.estimator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
