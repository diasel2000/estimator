package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleTest {

    @Test
    public void testEqualsAndHashCode() {
        UserRoleKey key1 = new UserRoleKey();
        key1.setUserID(1l);
        key1.setRoleID(1l);

        UserRoleKey key2 = new UserRoleKey();
        key2.setUserID(1l);
        key2.setRoleID(1l);

        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testToString() {
        UserRole userRole = new UserRole();
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        userRole.setRole(role);

        String expected = "UserRole{role=ROLE_USER}";
        assertEquals(expected, userRole.toString());
    }
}