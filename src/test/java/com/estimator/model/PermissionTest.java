package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionTest {

    @Test
    public void testEqualsAndHashCode() {
        Permission permission1 = new Permission();
        permission1.setPermissionID(1);
        permission1.setPermissionName("PERMISSION_VIEW");

        Permission permission2 = new Permission();
        permission2.setPermissionID(1);
        permission2.setPermissionName("PERMISSION_VIEW");

        assertEquals(permission1, permission2);
        assertEquals(permission1.hashCode(), permission2.hashCode());
    }

    @Test
    public void testToString() {
        Permission permission = new Permission();
        permission.setPermissionID(1);
        permission.setPermissionName("PERMISSION_EDIT");

        String expected = "Permission(permissionID=1, permissionName=PERMISSION_EDIT, description=null)";
        assertEquals(expected, permission.toString());
    }
}