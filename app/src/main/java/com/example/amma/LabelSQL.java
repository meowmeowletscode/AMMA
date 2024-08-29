package com.example.amma;

import java.sql.Connection;
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
}
