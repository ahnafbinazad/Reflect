// Define a ViewModel class named HomeViewModel responsible for managing home screen data.
package com.reflect.reflect.ui.home;

// Import necessary packages and classes.
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Define a class named HomeViewModel that extends ViewModel.
public class HomeViewModel extends ViewModel {

    // Declare a MutableLiveData to hold text data.
    private final MutableLiveData<String> mText;

    // Constructor to initialize HomeViewModel with default text.
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    // Method to get LiveData of text.
    public LiveData<String> getText() {
        return mText;
    }
}
