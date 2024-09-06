package com.example.amma.ui.editasset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.amma.ConSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EditAssetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EditAssetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Edit & Search Asset Record");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        try {
            // Assume you have a method to get a database connection
            ConSQL conn = new ConSQL();
            Connection connection = conn.SQLConnection();
            if(connection != null) {
                String query = "SELECT LabelName FROM [Label]"; // Replace with your actual query
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    labels.add(resultSet.getString("LabelName"));
                }
                resultSet.close();
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return labels;
    }
}