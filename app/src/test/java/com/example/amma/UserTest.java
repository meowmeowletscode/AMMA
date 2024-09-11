package com.example.amma;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserTest {

    private User userWithPassword;
    private User userWithoutPassword;

    // Setup method to initialize the User objects before each test
    @Before
    public void setUp() {
        userWithPassword = new User("john_doe", "securePassword123", "Admin");
        userWithoutPassword = new User("jane_doe", "Staff");
    }

    // Test the constructor with password
    @Test
    public void testUserConstructorWithPassword() {
        assertEquals("john_doe", userWithPassword.getUserName());
        assertEquals("securePassword123", userWithPassword.getPassword());
        assertEquals("Admin", userWithPassword.getRole());
    }

    // Test the constructor without password
    @Test
    public void testUserConstructorWithoutPassword() {
        assertEquals("jane_doe", userWithoutPassword.getUserName());
        assertNull(userWithoutPassword.getPassword());
        assertEquals("Staff", userWithoutPassword.getRole());
    }

    // Test the getUserName method
    @Test
    public void testGetUserName() {
        assertEquals("john_doe", userWithPassword.getUserName());
    }

    // Test the setUserName method
    @Test
    public void testSetUserName() {
        userWithPassword.setUserName("john_admin");
        assertEquals("john_admin", userWithPassword.getUserName());
    }

    // Test the getPassword method
    @Test
    public void testGetPassword() {
        assertEquals("securePassword123", userWithPassword.getPassword());
    }

    // Test the setPassword method
    @Test
    public void testSetPassword() {
        userWithPassword.setPassword("newPassword456");
        assertEquals("newPassword456", userWithPassword.getPassword());
    }

    // Test the getRole method
    @Test
    public void testGetRole() {
        assertEquals("Admin", userWithPassword.getRole());
    }

    // Test the setRole method
    @Test
    public void testSetRole() {
        userWithPassword.setRole("Manager");
        assertEquals("Manager", userWithPassword.getRole());
    }

    // Test the toString method
    @Test
    public void testToString() {
        assertEquals("UserName: john_doe, Role: Admin", userWithPassword.toString());
    }
}

