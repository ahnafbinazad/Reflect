// Import necessary packages and classes.
package com.reflect.reflect;

// Import AppCompatActivity from the androidx.appcompat library.
import androidx.appcompat.app.AppCompatActivity;

// Import Bundle from the android.os library.
import android.os.Bundle;

// Import ActivityMainBinding from the com.reflect.reflect.databinding package.
import com.reflect.reflect.databinding.ActivityMainBinding;

// Define a class named MainActivity that extends AppCompatActivity.
public class MainActivity extends AppCompatActivity {

    // Declare a variable activityMainBinding of type ActivityMainBinding.
    private ActivityMainBinding activityMainBinding;

    // Override the onCreate method.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ActivityMainBinding.
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }
}
