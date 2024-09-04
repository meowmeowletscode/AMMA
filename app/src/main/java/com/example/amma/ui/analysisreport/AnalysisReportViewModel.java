package com.example.amma.ui.analysisreport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.amma.ConSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnalysisReportViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AnalysisReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Analysis Asset Report");
    }

    public LiveData<String> getText() {
        return mText;
    }

}