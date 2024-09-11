package com.example.amma;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import com.example.amma.UserManager;

public class UserManagerTest {

    private UserManager userManager;

    @Before
    public void setUp() {
        UserManager.resetInstance();
        userManager = UserManager.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        UserManager anotherInstance = UserManager.getInstance();
        assertSame(userManager, anotherInstance);
    }

    @Test
    public void testSetCurrentUser() {
        User testUser = new User("testUser", "password", "Admin");
        userManager.setCurrentUser(testUser);

        User currentUser = userManager.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("testUser", currentUser.getUserName());
        assertEquals("Admin", currentUser.getRole());
    }

    @Test
    public void testGetCurrentUserWhenNotSet() {
        assertNull(userManager.getCurrentUser());
    }

    @Test
    public void testSetAndGetCurrentUser() {
        User testUser = new User("anotherUser", "password123", "Staff");
        userManager.setCurrentUser(testUser);

        User currentUser = userManager.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("anotherUser", currentUser.getUserName());
        assertEquals("Staff", currentUser.getRole());
    }
}
