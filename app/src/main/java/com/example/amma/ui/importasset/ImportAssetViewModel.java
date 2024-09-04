package com.example.amma.ui.importasset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImportAssetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ImportAssetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Import Asset Data");
    }

    public LiveData<String> getText() {
        return mText;
    }

}