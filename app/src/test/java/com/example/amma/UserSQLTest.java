package com.example.amma;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserSQLTest {

    private UserSQL userSQL;

    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Initialize UserSQL with a mocked connection
        userSQL = new UserSQL(mockConnection);

        // Common mock behavior setup
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
    }

    @Test
    public void testGetUsers() throws SQLException {
        // Mock the SQL execution flow
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // Mock result set data
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("UserName")).thenReturn("testUser");
        when(mockResultSet.getString("Role")).thenReturn("Admin");

        List<User> users = userSQL.getUsers();

        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUserName());
        assertEquals("Admin", users.get(0).getRole());
    }

    @Test
    public void testAddUser() throws SQLException {
        // Mock the prepared statement and execution
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userSQL.addUser("newUser", "password123", "Staff");

        assertTrue(result);

        // Verify the statement values
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockPreparedStatement, times(3)).setString(anyInt(), captor.capture());
        assertEquals("newUser", captor.getAllValues().get(0));
        assertEquals("password123", captor.getAllValues().get(1));
        assertEquals("Staff", captor.getAllValues().get(2));
    }

    @Test
    public void testEditUserPasswordSuccess() throws SQLException {
        String username = "testUser";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        // Step 1: Setup mock for verifying old password
        when(mockConnection.prepareStatement(ArgumentMatchers.contains("SELECT * FROM [User]"))).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);

        // Step 2: Setup mock for updating password
        PreparedStatement mockUpdateStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(ArgumentMatchers.contains("UPDATE [User]"))).thenReturn(mockUpdateStatement);

        // Execute the method
        boolean result = userSQL.editUserPassword(username, oldPassword, newPassword);

        // Verify that verification and update are called
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockUpdateStatement, times(1)).executeUpdate();

        // Verify that the edit operation was successful
        assertTrue(result);
    }

    @Test
    public void testDeleteUserSuccess() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate user found

        boolean result = userSQL.deleteUser("testUser", "password");

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testIsValidUser() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate user exists

        boolean result = userSQL.isValidUser("testUser");

        assertTrue(result);
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void testGetStoredPassword() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate user exists
        when(mockResultSet.getString("Password")).thenReturn("password123");

        String password = userSQL.getStoredPassword("testUser");

        assertEquals("password123", password);
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void testGetUserRole() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate user exists
        when(mockResultSet.getString("Role")).thenReturn("Admin");

        String role = userSQL.getUserRole("testUser");

        assertEquals("Admin", role);
        verify(mockStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void testUpdateLastLogin() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        userSQL.updateLastLogin("testUser");

        verify(mockPreparedStatement, times(1)).setTimestamp(anyInt(), any(Timestamp.class));
        verify(mockPreparedStatement, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testInsertUserLoginHistory() throws SQLException {
        String username = "testUser";
        String deviceId = "testDevice";

        // Step 1: Setup mock for SELECT statement
        when(mockConnection.prepareStatement(ArgumentMatchers.contains("SELECT UserID, UserName"))).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("UserID")).thenReturn(1);
        when(mockResultSet.getString("UserName")).thenReturn(username);

        // Step 2: Setup mock for INSERT statement
        PreparedStatement mockInsertStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(ArgumentMatchers.contains("INSERT INTO UserLoginHistory"))).thenReturn(mockInsertStatement);

        // Execute the method
        userSQL.insertUserLoginHistory(username, deviceId);

        // Verify the interaction with the mock objects
        verify(mockPreparedStatement, times(1)).setString(1, username);
        verify(mockInsertStatement, times(1)).setInt(1, 1); // UserID
        verify(mockInsertStatement, times(1)).setString(2, username); // UserName
        verify(mockInsertStatement, times(1)).setTimestamp(eq(3), any(Timestamp.class)); // Login_Time
        verify(mockInsertStatement, times(1)).setString(4, deviceId); // DeviceID
        verify(mockInsertStatement, times(1)).executeUpdate();
    }
}
