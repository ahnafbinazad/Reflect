package com.reflect.reflect.ui.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reflect.reflect.model.MyDay;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    // LiveData to hold the list of MyDay objects.
    MutableLiveData<List<MyDay>> listMyDay = new MutableLiveData<>();

    // LiveData to hold the selected time.
    MutableLiveData<Long> selectedTime = new MutableLiveData<>();

    // Method to set the list of MyDay objects.
    public void setList(List<MyDay> list) {
        listMyDay.setValue(list);
    }
}
