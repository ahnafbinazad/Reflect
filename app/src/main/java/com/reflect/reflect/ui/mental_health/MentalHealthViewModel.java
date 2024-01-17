package com.reflect.reflect.ui.mental_health;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.reflect.reflect.model.MentalHealthRecords;

import java.util.List;

public class MentalHealthViewModel extends ViewModel {

    public void setMentalHealthRecords(MentalHealthRecords mentalHealthRecords) {
        liveDatamentalHealthRecords.setValue(mentalHealthRecords);
    }
    MutableLiveData<MentalHealthRecords> liveDatamentalHealthRecords=new MutableLiveData<>();
    LiveData<MentalHealthRecords> getRecords()
    {
        return liveDatamentalHealthRecords;
    }

}