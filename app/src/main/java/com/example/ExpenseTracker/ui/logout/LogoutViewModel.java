package com.example.ExpenseTracker.ui.logout;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LogoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Please login to view your account.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}