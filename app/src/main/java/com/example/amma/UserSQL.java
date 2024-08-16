package com.example.amma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class UserSQL {

    private ConSQL conn;
    private Connection connection;

    public UserSQL() {
        conn = new ConSQL();
        connection = conn.SQLConnection();
    }

    // Method to validate if the user exists
    public boolean isValidUser(String userName) {
        try {
            if (connection != null) {
                String query = "SELECT UserName FROM [User] WHERE UserName='" + userName + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                return rs.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Handle the exception appropriately
        }
        return false;
    }

    // Method to get the stored password for a user
    public String getStoredPassword(String userName) {
        try {
            if (connection != null) {
                String query = "SELECT Password FROM [User] WHERE UserName='" + userName + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    return rs.getString("Password");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Method to get the role of a user
    public String getUserRole(String userName) {
        try {
            if (connection != null) {
                String query = "SELECT Role FROM [User] WHERE UserName='" + userName + "'";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    return rs.getString("Role");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Method to update the Last_Login timestamp for a user
    public void updateLastLogin(String userName) {
        try {
            if (connection != null) {
                String query = "UPDATE [User] SET Last_Login = ? WHERE UserName = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Set current timestamp
                pstmt.setString(2, userName);
                pstmt.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void insertUserLoginHistory(String userName) {
        try {
            // Step 1: Retrieve UserID and UserName from User table
            String selectQuery = "SELECT UserID, UserName FROM [User] WHERE UserName = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, userName);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserID");
                String user = rs.getString("UserName");

                // Step 2: Insert into UserLoginHistory table
                String insertQuery = "INSERT INTO UserLoginHistory (UserID, UserName, Login_Time) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, user);
                insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

                insertStmt.executeUpdate();
                insertStmt.close();
            }

            rs.close();
            selectStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
