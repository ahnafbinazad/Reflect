// Define a ViewModel class named MentalHealthViewModel responsible for managing MentalHealthRecords data.
package com.reflect.reflect.ui.mental_health;

// Import necessary packages and classes.
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.reflect.reflect.model.MentalHealthRecords;
import java.util.List;

// Define a class named MentalHealthViewModel that extends ViewModel.
public class MentalHealthViewModel extends ViewModel {

    // Declare a MutableLiveData to hold MentalHealthRecords data.
    MutableLiveData<MentalHealthRecords> liveDatamentalHealthRecords = new MutableLiveData<>();

    // Method to set MentalHealthRecords data.
    public void setMentalHealthRecords(MentalHealthRecords mentalHealthRecords) {
        liveDatamentalHealthRecords.setValue(mentalHealthRecords);
    }

    // Method to get LiveData of MentalHealthRecords.
    LiveData<MentalHealthRecords> getRecords() {
        return liveDatamentalHealthRecords;
    }
}
