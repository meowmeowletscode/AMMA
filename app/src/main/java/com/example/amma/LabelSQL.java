package com.example.amma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LabelSQL {
    private ConSQL conn;
    private Connection connection;

    public LabelSQL() {
        conn = new ConSQL();
        connection = conn.SQLConnection();
    }

    public List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();
        String query = "SELECT LabelName FROM Label";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String labelName = resultSet.getString("LabelName");
                labels.add(new Label(labelName));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }

    // Add a new label
    public boolean addLabel(String labelName) {
        String query = "INSERT INTO Label (LabelName, CreatedAt) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, labelName);

            preparedStatement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected > 0; // Return true if the insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Update an existing label
    public boolean updateLabel(String oldLabelName, String newLabelName) {
        String query = "UPDATE Label SET LabelName = ?, EditedAt = ? WHERE LabelName = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newLabelName);

            preparedStatement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));

            preparedStatement.setString(3, oldLabelName);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected > 0; // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Delete a label
    public boolean deleteLabel(String labelName) {
        String query = "DELETE FROM Label WHERE LabelName = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, labelName);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
