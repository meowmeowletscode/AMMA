package com.example.amma;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConSQLTest {

    @Mock
    private Connection mockConnection;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSQLConnection() throws Exception {
        // Mock the DriverManager to return a mock Connection
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(any(String.class))).thenReturn(mockConnection);

            ConSQL conSQL = new ConSQL();
            Connection connection = conSQL.SQLConnection();

            assertNotNull(connection);
            assertEquals(mockConnection, connection);

            // Verify DriverManager.getConnection was called with the expected URL
            String expectedURL = "jdbc:jtds:sqlserver://192.168.1.7:1433;databasename=AMMA;user=sa;password=sa;";
            mockedDriverManager.verify(() -> DriverManager.getConnection(expectedURL));
        }
    }
}

