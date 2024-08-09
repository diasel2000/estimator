package com.estimator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleKeyTest {

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
    public void testEqualsWithDifferentUserID() {
        UserRoleKey key1 = new UserRoleKey();
        key1.setUserID(1l);
        key1.setRoleID(1l);

        UserRoleKey key2 = new UserRoleKey();
        key2.setUserID(2l);
        key2.setRoleID(1l);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testEqualsWithDifferentRoleID() {
        UserRoleKey key1 = new UserRoleKey();
        key1.setUserID(1l);
        key1.setRoleID(1l);

        UserRoleKey key2 = new UserRoleKey();
        key2.setUserID(1l);
        key2.setRoleID(2l);

        assertNotEquals(key1, key2);
    }

    @Test
    public void testToString() {
        UserRoleKey key = new UserRoleKey();
        key.setUserID(1l);
        key.setRoleID(1l);

        String expected = "UserRoleKey(userID=1, roleID=1)";
        assertEquals(expected, key.toString());
    }
}