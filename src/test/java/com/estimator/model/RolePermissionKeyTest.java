package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RolePermissionKeyTest {

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
    public void testEqualsWithDifferentRoleID() {
        RolePermissionKey key1 = new RolePermissionKey();
        key1.setRoleID(1);
        key1.setPermissionID(1);

        RolePermissionKey key2 = new RolePermissionKey();
        key2.setRoleID(2);
        key2.setPermissionID(1);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testEqualsWithDifferentPermissionID() {
        RolePermissionKey key1 = new RolePermissionKey();
        key1.setRoleID(1);
        key1.setPermissionID(1);

        RolePermissionKey key2 = new RolePermissionKey();
        key2.setRoleID(1);
        key2.setPermissionID(2);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testToString() {
        RolePermissionKey key = new RolePermissionKey();
        key.setRoleID(1);
        key.setPermissionID(1);

        String expected = "RolePermissionKey(roleID=1, permissionID=1)";
        assertEquals(expected, key.toString());
    }
}