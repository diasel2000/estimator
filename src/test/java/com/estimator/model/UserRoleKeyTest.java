package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleKeyTest {

    @Test
    public void testEqualsAndHashCode() {
        UserRoleKey key1 = new UserRoleKey();
        key1.setUserID(1);
        key1.setRoleID(1);

        UserRoleKey key2 = new UserRoleKey();
        key2.setUserID(1);
        key2.setRoleID(1);

        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentUserID() {
        UserRoleKey key1 = new UserRoleKey();
        key1.setUserID(1);
        key1.setRoleID(1);

        UserRoleKey key2 = new UserRoleKey();
        key2.setUserID(2);
        key2.setRoleID(1);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testEqualsWithDifferentRoleID() {
        UserRoleKey key1 = new UserRoleKey();
        key1.setUserID(1);
        key1.setRoleID(1);

        UserRoleKey key2 = new UserRoleKey();
        key2.setUserID(1);
        key2.setRoleID(2);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testToString() {
        UserRoleKey key = new UserRoleKey();
        key.setUserID(1);
        key.setRoleID(1);

        String expected = "UserRoleKey(userID=1, roleID=1)";
        assertEquals(expected, key.toString());
    }
}