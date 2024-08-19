package com.example.amma.ui.addasset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.amma.ConSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddAssetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddAssetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Create Asset Record");
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