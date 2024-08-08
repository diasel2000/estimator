package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RolePermissionTest {

    @Test
    public void testEqualsAndHashCode() {
        RolePermissionKey key1 = new RolePermissionKey();
        key1.setRoleID(1);
        key1.setPermissionID(1);

        RolePermissionKey key2 = new RolePermissionKey();
        key2.setRoleID(1);
        key2.setPermissionID(1);

        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testToString() {
        RolePermission rolePermission = new RolePermission();
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        rolePermission.setRole(role);

        String expected = "RolePermission(id=null, role=ROLE_USER, permission=null)";
        assertEquals(expected, rolePermission.toString());
    }
}