package com.reflect.reflect.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reflect.reflect.model.MyDay;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.flow.StateFlow;

public class HistoryViewModel extends ViewModel {

    MutableLiveData<List<MyDay>> listMyDay = new MutableLiveData<>();
    MutableLiveData<Long> selectedTime=new MutableLiveData<>();

    void setList(List<MyDay> list)
    {
        listMyDay.setValue(list);
    }
}