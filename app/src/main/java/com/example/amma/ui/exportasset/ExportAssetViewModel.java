package com.example.amma.ui.exportasset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExportAssetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ExportAssetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Export Asset Data");
    }

    public LiveData<String> getText() {
        return mText;
    }

}