package com.example.amma;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AssetTest {

    private Asset asset;

    // Setup method to initialize the Asset object before each test
    @Before
    public void setUp() {
        asset = new Asset("Laptop", "123456789", 10, "Office Laptop", "Electronics");
    }

    // Test the constructor for Asset class
    @Test
    public void testAssetConstructor() {
        assertEquals("Laptop", asset.getAssetName());
        assertEquals("123456789", asset.getBarcode());
        assertEquals(10, asset.getQuantity());
        assertEquals("Office Laptop", asset.getDescription());
        assertEquals("Electronics", asset.getLabel());
        assertNull(asset.getPhoto());  // Photo is not set, should be null
        assertNull(asset.getEditedAt()); // EditedAt is not set, should be null
        assertNull(asset.getCreatedAt()); // CreatedAt is not set, should be null
    }

    // Test the getter and setter for Asset ID
    @Test
    public void testSetAndGetAssetID() {
        asset.setAssetID(1);
        assertEquals(1, asset.getAssetID());
    }

    // Test the getter and setter for Asset Name
    @Test
    public void testSetAndGetAssetName() {
        asset.setAssetName("Desktop");
        assertEquals("Desktop", asset.getAssetName());
    }

    // Test the getter and setter for Barcode
    @Test
    public void testSetAndGetBarcode() {
        asset.setBarcode("987654321");
        assertEquals("987654321", asset.getBarcode());
    }

    // Test the getter and setter for Quantity
    @Test
    public void testSetAndGetQuantity() {
        asset.setQuantity(20);
        assertEquals(20, asset.getQuantity());
    }

    // Test the getter and setter for Description
    @Test
    public void testSetAndGetDescription() {
        asset.setDescription("Updated Description");
        assertEquals("Updated Description", asset.getDescription());
    }

    // Test the getter and setter for Label
    @Test
    public void testSetAndGetLabel() {
        asset.setLabel("Updated Label");
        assertEquals("Updated Label", asset.getLabel());
    }

    // Test the getter and setter for Photo
    @Test
    public void testSetAndGetPhoto() {
        asset.setPhoto("photo.png");
        assertEquals("photo.png", asset.getPhoto());
    }

    // Test the getter and setter for EditedAt timestamp
    @Test
    public void testSetAndGetEditedAt() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        asset.setEditedAt(timestamp);
        assertEquals(timestamp, asset.getEditedAt());
    }

    // Test the getter and setter for CreatedAt timestamp
    @Test
    public void testSetAndGetCreatedAt() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        asset.setCreatedAt(timestamp);
        assertEquals(timestamp, asset.getCreatedAt());
    }
}
