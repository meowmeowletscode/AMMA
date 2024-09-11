package com.example.amma;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AssetSQLTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private PreparedStatement mockPreparedStatement;

    private AssetSQL assetSQL;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Use the modified constructor
        assetSQL = new AssetSQL(mockConnection);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testGetAllAssets() throws Exception {
        when(mockStatement.executeQuery(any(String.class))).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("AssetName")).thenReturn("Asset1");
        when(mockResultSet.getString("Barcode")).thenReturn("123456");
        when(mockResultSet.getInt("Quantity")).thenReturn(10);
        when(mockResultSet.getString("Description")).thenReturn("Description1");
        when(mockResultSet.getString("LabelName")).thenReturn("Label1");

        List<Asset> assets = assetSQL.getAllAssets();

        assertNotNull(assets);
        assertEquals(1, assets.size());
        assertEquals("Asset1", assets.get(0).getAssetName());
    }

    @Test
    public void testGetFilteredAssets() throws Exception {
        // Set up mock behavior for ResultSet
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("AssetName")).thenReturn("FilteredAsset");
        when(mockResultSet.getString("Barcode")).thenReturn("654321");
        when(mockResultSet.getInt("Quantity")).thenReturn(5);
        when(mockResultSet.getString("Description")).thenReturn("FilteredDescription");
        when(mockResultSet.getString("LabelName")).thenReturn("FilteredLabel");

        // Set up mock behavior for PreparedStatement
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Create a mock connection and set it up in AssetSQL
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        assetSQL = new AssetSQL() {
            @Override
            protected Connection getConnection() {
                return mockConnection;
            }
        };

        // Call the method under test
        List<Asset> assets = assetSQL.getFilteredAssets("2023-01-01", "2023-12-31", "FilteredLabel");

        // Verify the result
        assertNotNull(assets);
        assertEquals(1, assets.size());
        assertEquals("FilteredAsset", assets.get(0).getAssetName());
        assertEquals("654321", assets.get(0).getBarcode());
        assertEquals(5, assets.get(0).getQuantity());
        assertEquals("FilteredDescription", assets.get(0).getDescription());
        assertEquals("FilteredLabel", assets.get(0).getLabel());

        // Verify interactions with mocks
        verify(mockPreparedStatement).setTimestamp(1, Timestamp.valueOf("2023-01-01 00:00:00.000"));
        verify(mockPreparedStatement).setTimestamp(2, Timestamp.valueOf("2023-12-31 23:59:59.999"));
        verify(mockPreparedStatement).setString(3, "FilteredLabel");
        verify(mockPreparedStatement).executeQuery();
    }


    @Test
    public void testSaveAsset() throws Exception {
        // Assuming `photo` is null for this test
        boolean result = assetSQL.saveAsset("NewAsset", "987654", 20, "NewDescription", "NewLabel", null);

        assertTrue(result);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testIsBarcodeExists() throws Exception {
        // Set up mock behavior for PreparedStatement and ResultSet
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Simulate that the ResultSet contains a row
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulate a non-empty result

        // Call the method under test
        boolean exists = assetSQL.isBarcodeExists("123456");

        // Verify the result
        assertTrue(exists);

        // Verify interactions with mocks
        verify(mockPreparedStatement).setString(1, "123456"); // Check if barcode was set
        verify(mockPreparedStatement).executeQuery(); // Check if executeQuery was called
    }

    @Test
    public void testIsImportBarcodeExists() throws Exception {
        // Set up mock behavior for PreparedStatement and ResultSet
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Simulate that the ResultSet contains a row
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulate a non-empty result

        // Call the method under test
        boolean exists = assetSQL.isImportBarcodeExists("123456");

        // Verify the result
        assertTrue(exists);

        // Verify interactions with mocks
        verify(mockPreparedStatement).setString(1, "123456"); // Check if barcode was set
        verify(mockPreparedStatement).executeQuery(); // Check if executeQuery was called
    }

    @Test
    public void testSearchAsset() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("AssetName")).thenReturn("SearchedAsset");
        when(mockResultSet.getString("Barcode")).thenReturn("112233");
        when(mockResultSet.getInt("Quantity")).thenReturn(15);
        when(mockResultSet.getString("Description")).thenReturn("SearchedDescription");
        when(mockResultSet.getString("LabelName")).thenReturn("SearchedLabel");
        when(mockResultSet.getBytes("Photo")).thenReturn(null);

        Map<String, Object> assetDetails = assetSQL.searchAsset("112233");

        assertNotNull(assetDetails);
        assertEquals("SearchedAsset", assetDetails.get("AssetName"));
    }

    @Test
    public void testEditAsset() throws Exception {
        // Mock the behavior of executeUpdate
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Call the method under test
        boolean result = assetSQL.editAsset("EditedAsset", "123456", 25, "EditedDescription", "EditedLabel", null);

        // Verify the result
        assertTrue(result);

        // Verify interactions with mocks
        verify(mockPreparedStatement).setString(1, "EditedAsset");
        verify(mockPreparedStatement).setInt(2, 25);
        verify(mockPreparedStatement).setString(3, "EditedDescription");
        verify(mockPreparedStatement).setString(4, "EditedLabel");
        verify(mockPreparedStatement).setNull(5, java.sql.Types.BINARY); // Check if null was set for photo
        verify(mockPreparedStatement).setTimestamp(6, any(Timestamp.class)); // Use any(Timestamp.class) for Timestamp
        verify(mockPreparedStatement).setString(7, "123456");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteAsset() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = assetSQL.deleteAsset("123456");
        assertTrue(result);

        verify(mockPreparedStatement).setString(1, "123456");
    }
}
