package com.example.amma;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

public class LabelSQLTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private LabelSQL labelSQL;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        labelSQL = new LabelSQL(mockConnection);
    }

    @Test
    public void testIsLabelExists() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean exists = labelSQL.isLabelExists("TestLabel");

        assertTrue(exists);
        verify(mockPreparedStatement).setString(1, "TestLabel");
        verify(mockResultSet).close();
    }

    @Test
    public void testGetLabels() throws SQLException {
        when(mockStatement.executeQuery(any(String.class))).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("LabelName")).thenReturn("Label1");

        List<Label> labels = labelSQL.getLabels();

        assertEquals(1, labels.size());
        assertEquals("Label1", labels.get(0).getLabelName());
        verify(mockResultSet).close();
        verify(mockStatement).close();
    }

    @Test
    public void testAddLabel() throws SQLException {
        // Setup mock behavior
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Call the method under test
        boolean result = labelSQL.addLabel("NewLabel");

        // Verify results
        assertTrue(result);
        verify(mockPreparedStatement).setString(eq(1), eq("NewLabel"));
        verify(mockPreparedStatement).setTimestamp(eq(2), any(java.sql.Timestamp.class));
        verify(mockPreparedStatement).close();
    }


    @Test
    public void testUpdateLabel() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        // Create an instance of LabelSQL with the mocked connection
        LabelSQL labelSQL = new LabelSQL(mockConnection);

        String oldLabelName = "Old Label";
        String newLabelName = "New Label";

        // Set up the mock Connection to return the mock PreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);

        // Set up the mock execution
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate successful update

        // Act
        boolean result = labelSQL.updateLabel(oldLabelName, newLabelName);

        // Assert
        verify(mockPreparedStatement).setString(1, newLabelName);
        verify(mockPreparedStatement).setTimestamp(2, any(java.sql.Timestamp.class));
        verify(mockPreparedStatement).setString(3, oldLabelName);
        verify(mockPreparedStatement).executeUpdate();
        assertTrue(result);
    }

    @Test
    public void testDeleteLabel() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = labelSQL.deleteLabel("LabelToDelete");

        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "LabelToDelete");
        verify(mockPreparedStatement).close();
    }
}
