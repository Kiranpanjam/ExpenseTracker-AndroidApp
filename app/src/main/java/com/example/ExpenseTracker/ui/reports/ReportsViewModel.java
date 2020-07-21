package com.example.ExpenseTracker.ui.reports;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReportsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Reports fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}