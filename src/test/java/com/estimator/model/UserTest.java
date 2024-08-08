package com.estimator.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testGetRoles() {
        User user = new User();
        Role role = new Role();
        role.setRoleName("ROLE_USER");

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        user.getUserRoles().add(userRole);

        Set<Role> roles = user.getRoles();

        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertTrue(roles.stream().anyMatch(r -> r.getRoleName().equals("ROLE_USER")));
    }
}
