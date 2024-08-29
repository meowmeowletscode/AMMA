package com.example.amma;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AssetSQL {

    private ConSQL conn;
    private Connection connection;

    public AssetSQL() {
        conn = new ConSQL();
        connection = conn.SQLConnection();
    }

    public boolean saveAsset(String assetName, String barcode, int quantity, String description, String label, Bitmap photo) {

        try {
            String insertQuery = "INSERT INTO Asset (AssetName, Barcode, Quantity, Description, LabelName, Photo, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, assetName);
            preparedStatement.setString(2, barcode);
            preparedStatement.setInt(3, quantity);

            // Set Description (can be NULL)
            if (description == null || description.isEmpty()) {
                preparedStatement.setNull(4, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(4, description);
            }

            // Set Label
            preparedStatement.setString(5, label);

            // Convert Bitmap to Byte Array for Photo (can be NULL)
            if (photo != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] data = bos.toByteArray();
                preparedStatement.setBytes(6, data);
            } else {
                preparedStatement.setNull(6, java.sql.Types.BINARY);
            }

            // Set CreatedAt to current date and time
            Timestamp createdAt = new Timestamp(new Date().getTime());
            preparedStatement.setTimestamp(7, createdAt);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isBarcodeExists(String barcode) {
        String query = "SELECT COUNT(*) FROM Asset WHERE Barcode = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the count is greater than 0
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                resultSet.close();
                preparedStatement.close();
                return true; // Barcode exists
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Barcode does not exist
    }

    public Map<String, Object> searchAsset(String barcode) {
        Map<String, Object> assetDetails = new HashMap<>();

        try {
            // SQL query to search for the asset by barcode
            String selectQuery = "SELECT AssetName, Barcode, Quantity, Description, LabelName, Photo FROM Asset WHERE Barcode = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, barcode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve asset details from the result set
                assetDetails.put("AssetName", resultSet.getString("AssetName"));
                assetDetails.put("Barcode", resultSet.getString("Barcode"));
                assetDetails.put("Quantity", resultSet.getInt("Quantity"));
                assetDetails.put("Description", resultSet.getString("Description"));
                assetDetails.put("LabelName", resultSet.getString("LabelName"));

                // Convert byte array back to Bitmap if photo exists
                byte[] photoBytes = resultSet.getBytes("Photo");
                if (photoBytes != null) {
                    Bitmap photo = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
                    assetDetails.put("Photo", photo);
                } else {
                    assetDetails.put("Photo", null);
                }

                // Close the result set and prepared statement
                resultSet.close();
                preparedStatement.close();
                return assetDetails;
            }

            // Close the result set and prepared statement if no asset found
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if the asset is not found
    }

}
