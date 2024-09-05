package com.example.amma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserSQL {

    private ConSQL conn;
    private Connection connection;

    public UserSQL() {
        conn = new ConSQL();
        connection = conn.SQLConnection();
    }

    //List for User Account Control
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT UserName, Role FROM [User]";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String userName = resultSet.getString("UserName");
                String role = resultSet.getString("Role");
                users.add(new User(userName, role));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean addUser(String username, String password, String role) {
        String insertQuery = "INSERT INTO [User] (UserName, Password, Role, CreatedAt) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            statement.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean editUserPassword(String username, String oldPassword, String newPassword) {
        // Update password only after verifying the old password
        String verifyQuery = "SELECT * FROM [User] WHERE UserName = ? AND Password = ?";
        String updateQuery = "UPDATE [User] SET Password = ? WHERE UserName = ?";

        try {
            // Verify old password
            PreparedStatement verifyStatement = connection.prepareStatement(verifyQuery);
            verifyStatement.setString(1, username);
            verifyStatement.setString(2, oldPassword);
            ResultSet resultSet = verifyStatement.executeQuery();

            if (resultSet.next()) {
                // Old password is correct, proceed with update
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setString(1, newPassword);
                updateStatement.setString(2, username);
                updateStatement.executeUpdate();
                updateStatement.close();
                resultSet.close();
                verifyStatement.close();
                return true;
            }
            resultSet.close();
            verifyStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String username, String password) {
        // Delete user only after verifying the password
        String verifyQuery = "SELECT * FROM [User] WHERE UserName = ? AND Password = ?";
        String deleteQuery = "DELETE FROM [User] WHERE UserName = ?";

        try {
            // Verify password
            PreparedStatement verifyStatement = connection.prepareStatement(verifyQuery);
            verifyStatement.setString(1, username);
            verifyStatement.setString(2, password);
            ResultSet resultSet = verifyStatement.executeQuery();

            if (resultSet.next()) {
                // Password is correct, proceed with deletion
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, username);
                deleteStatement.executeUpdate();
                deleteStatement.close();
                resultSet.close();
                verifyStatement.close();
                return true;
            }
            resultSet.close();
            verifyStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
            ex.printStackTrace();
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

    public void insertUserLoginHistory(String userName, String deviceId) {
        try {
            // Step 1: Retrieve UserID and UserName from User table
            String selectQuery = "SELECT UserID, UserName FROM [User] WHERE UserName = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, userName);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserID");
                String user = rs.getString("UserName");

                // Step 2: Insert into UserLoginHistory table with Device ID
                String insertQuery = "INSERT INTO UserLoginHistory (UserID, UserName, Login_Time, DeviceID) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, user);
                insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                insertStmt.setString(4, deviceId);

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
