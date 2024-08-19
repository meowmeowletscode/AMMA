package com.example.amma;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class AssetSQL {

    private ConSQL conn;
    private Connection connection;

    public AssetSQL() {
        conn = new ConSQL();
        connection = conn.SQLConnection();
    }

    public void saveAsset(String assetName, String barcode, int quantity, String description, String label, Bitmap photo) {
        String insertQuery = "INSERT INTO Asset (AssetName, Barcode, Quantity, Description, LabelName, Photo, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
