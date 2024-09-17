package com.example.amma;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AssetSQL {

    private ConSQL conn;
    private Connection connection;

    // Constructor to use in tests
    public AssetSQL(Connection connection) {
        this.connection = connection;
    }

    public AssetSQL() {
        conn = new ConSQL();
        connection = conn.SQLConnection();
    }

    private static final String DATE_INPUT_FORMAT = "MM/dd/yyyy";
    private static final String SQL_DATE_FORMAT = "yyyy-MM-dd";

    // Accessor for the connection, useful for testing
    protected Connection getConnection() {
        return connection;
    }

    public List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();
        String query = "SELECT * FROM Asset1";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Asset asset = new Asset(
                        resultSet.getString("AssetName"),
                        resultSet.getString("Barcode"),
                        resultSet.getInt("Quantity"),
                        resultSet.getString("Description"),
                        resultSet.getString("LabelName")
                );
                assets.add(asset);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assets;
    }

    public List<Asset> getAssetsByDateRange(String startDateStr, String endDateStr) {
        List<Asset> assets = new ArrayList<>();

        try {
            // Parse the input date strings
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = inputFormat.parse(startDateStr);
            Date endDate = inputFormat.parse(endDateStr);

            // Format dates for SQL
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDateFormatted = sqlFormat.format(startDate);
            Log.d("SQL Debug", "Formatted From Date: " + startDateFormatted);
            String endDateFormatted = sqlFormat.format(endDate);
            Log.d("SQL Debug", "Formatted To Date: " + endDateFormatted);

            // Construct the SQL query
            String query = "SELECT * FROM Asset1 WHERE CreatedAt BETWEEN ? AND ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, startDateFormatted);
                stmt.setString(2, endDateFormatted);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    // Retrieve asset data and add to list
                    Asset asset = new Asset(
                            rs.getString("AssetName"),
                            rs.getString("Barcode"),
                            rs.getInt("Quantity"),
                            rs.getString("Description"),
                            rs.getString("LabelName")
                    );
                    assets.add(asset);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assets;
    }

    // Use for datetime format CreatedAT: fail
//    public List<Asset> getAssetsByDateRange(String startDateStr, String endDateStr) {
//        List<Asset> assets = new ArrayList<>();
//        try {
//            // Define the input date format (dd/MM/yyyy)
//            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//
//            // Parse the input date strings
//            Date startDate = inputFormat.parse(startDateStr);
//            Date endDate = inputFormat.parse(endDateStr);
//
//            // Convert Dates to SQL Timestamps
//            Timestamp startTimestamp = new Timestamp(startDate.getTime());
//            Timestamp endTimestamp = new Timestamp(endDate.getTime());
//
//            // Adjust end timestamp to include the entire end day
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(endDate);
//            calendar.set(Calendar.HOUR_OF_DAY, 23);
//            calendar.set(Calendar.MINUTE, 59);
//            calendar.set(Calendar.SECOND, 59);
//            Timestamp endOfDayTimestamp = new Timestamp(calendar.getTimeInMillis());
//
//            // Ensure that formatted dates are correct
//            Log.d("SQL Debug", "Formatted From Date: " + startTimestamp.toString());
//            Log.d("SQL Debug", "Formatted To Date: " + endOfDayTimestamp.toString());
//
//            // Construct the SQL query
//            String query = "SELECT * FROM Asset1 WHERE CreatedAt BETWEEN ? AND ?";
//
//            try (PreparedStatement stmt = connection.prepareStatement(query)) {
//                stmt.setTimestamp(1, startTimestamp);
//                stmt.setTimestamp(2, endOfDayTimestamp);
//
//                ResultSet rs = stmt.executeQuery();
//
//                while (rs.next()) {
//                    // Retrieve asset data and add to list
//                    Asset asset = new Asset(
//                            rs.getString("AssetName"),
//                            rs.getString("Barcode"),
//                            rs.getInt("Quantity"),
//                            rs.getString("Description"),
//                            rs.getString("LabelName")
//                    );
//                    assets.add(asset);
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return assets;
//    }

    public List<Asset> getAssetsByLabel(String label) {
        List<Asset> assets = new ArrayList<>();

        try {
            // Construct the SQL query with label filter
            String query = "SELECT * FROM Asset1 WHERE LabelName = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, label); // Set the label parameter

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    // Retrieve asset data and add to list
                    Asset asset = new Asset(
                            rs.getString("AssetName"),
                            rs.getString("Barcode"),
                            rs.getInt("Quantity"),
                            rs.getString("Description"),
                            rs.getString("LabelName")
                    );
                    assets.add(asset);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assets;
    }


    public boolean saveAsset(String assetName, String barcode, int quantity, String description, String label, Bitmap photo) {
        try {
            String insertQuery = "INSERT INTO Asset1 (AssetName, Barcode, Quantity, Description, LabelName, Photo, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
//            Timestamp createdAt = new Timestamp(new Date().getTime());
//            preparedStatement.setTimestamp(7, createdAt);

            java.sql.Date createdAt = new java.sql.Date(new java.util.Date().getTime());
            preparedStatement.setDate(7, createdAt);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Check if a barcode exists for add Asset
    public boolean isBarcodeExists(String barcode) {
        String query = "SELECT COUNT(*) FROM Asset1 WHERE Barcode = ?";
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

    // Check if a barcode exists for import Asset
    public boolean isImportBarcodeExists(String barcode) {
        String query = "SELECT 1 FROM Asset1 WHERE Barcode = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, barcode);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exists = resultSet.next();
            resultSet.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> searchAsset(String barcode) {
        Map<String, Object> assetDetails = new HashMap<>();

        try {
            // SQL query to search for the asset by barcode
            String selectQuery = "SELECT AssetName, Barcode, Quantity, Description, LabelName, Photo FROM Asset1 WHERE Barcode = ?";
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

    public boolean editAsset(String assetName, String barcode, int quantity, String description, String label, Bitmap photo) {
        try {
            String updateQuery = "UPDATE Asset1 SET AssetName = ?, Quantity = ?, Description = ?, LabelName = ?, Photo = ?, EditedAt = ? WHERE Barcode = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, assetName);
            preparedStatement.setInt(2, quantity);

            // Set Description (can be NULL)
            if (description == null || description.isEmpty()) {
                preparedStatement.setNull(3, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(3, description);
            }

            // Set Label
            preparedStatement.setString(4, label);

            // Convert Bitmap to Byte Array for Photo (can be NULL)
            if (photo != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] data = bos.toByteArray();
                preparedStatement.setBytes(5, data);
            } else {
                preparedStatement.setNull(5, java.sql.Types.BINARY);
            }

            // Set EditedAt to current date and time
            Timestamp editedAt = new Timestamp(new Date().getTime());
            preparedStatement.setTimestamp(6, editedAt);

            // Set Barcode for the WHERE clause
            preparedStatement.setString(7, barcode);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected > 0; // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAsset(String barcode) {
        try {
            String deleteQuery = "DELETE FROM Asset1 WHERE Barcode = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, barcode);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected > 0; // Return true if delete was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
