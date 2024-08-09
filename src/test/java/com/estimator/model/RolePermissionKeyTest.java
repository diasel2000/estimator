package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RolePermissionKeyTest {

    @Test
    public void testEqualsAndHashCode() {
        RolePermissionKey key1 = new RolePermissionKey();
        key1.setRoleID(1L);
        key1.setPermissionID(1L);

        RolePermissionKey key2 = new RolePermissionKey();
        key2.setRoleID(1L);
        key2.setPermissionID(1L);

        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentRoleID() {
        RolePermissionKey key1 = new RolePermissionKey();
        key1.setRoleID(1L);
        key1.setPermissionID(1L);

        RolePermissionKey key2 = new RolePermissionKey();
        key2.setRoleID(2L);
        key2.setPermissionID(1L);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testEqualsWithDifferentPermissionID() {
        RolePermissionKey key1 = new RolePermissionKey();
        key1.setRoleID(1l);
        key1.setPermissionID(1l);

        RolePermissionKey key2 = new RolePermissionKey();
        key2.setRoleID(1l);
        key2.setPermissionID(2l);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testToString() {
        RolePermissionKey key = new RolePermissionKey();
        key.setRoleID(1l);
        key.setPermissionID(1l);

        String expected = "RolePermissionKey(roleID=1, permissionID=1)";
        assertEquals(expected, key.toString());
    }
}