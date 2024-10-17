package com.cse3310.myfitnesstracker.ui.subscription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubscriptionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SubscriptionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is subscription fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}