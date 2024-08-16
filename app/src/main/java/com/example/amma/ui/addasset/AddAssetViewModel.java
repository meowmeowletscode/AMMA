package com.example.amma.ui.addasset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddAssetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddAssetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Create Asset Record");
    }

    public LiveData<String> getText() {
        return mText;
    }
}