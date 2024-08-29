package com.example.amma.ui.CustomizeLabel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomizeLabelViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CustomizeLabelViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Customize Label");
    }

    public LiveData<String> getText() {
        return mText;
    }
}