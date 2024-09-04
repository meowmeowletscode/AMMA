package com.example.amma.ui.usercontrol;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class UserControlViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UserControlViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("User Account Control");
    }

    public LiveData<String> getText() {
        return mText;
    }

}