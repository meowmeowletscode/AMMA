package com.example.amma.ui.editasset;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditAssetViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EditAssetViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is editasset fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}