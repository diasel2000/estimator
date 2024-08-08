package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class RoleTest {

    @Test
    public void testEqualsAndHashCode() {
        Role role1 = new Role();
        role1.setRoleID(1);
        role1.setRoleName("ROLE_USER");

        Role role2 = new Role();
        role2.setRoleID(1);
        role2.setRoleName("ROLE_USER");

        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testToString() {
        Role role = new Role();
        role.setRoleID(1);
        role.setRoleName("ROLE_ADMIN");

        String expected = "Role(roleID=1, roleName=ROLE_ADMIN, description=null)";
        assertEquals(expected, role.toString());
    }
}