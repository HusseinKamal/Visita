package com.emergency.app.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    MutableLiveData<Integer> data = new MutableLiveData<>();
    public MutableLiveData<Integer> getData() {
        return data;
    }
    public void setData(int item) {
        data.setValue(item);
    }
}
