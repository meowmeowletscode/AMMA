package com.example.amma.ui.deleteasset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DeleteAssetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DeleteAssetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is deleteasset fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}