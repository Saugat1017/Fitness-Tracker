package com.cse3310.myfitnesstracker.ui.trainer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrainerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TrainerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is trainer fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}