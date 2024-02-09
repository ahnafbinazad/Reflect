// Define a class named SharedPref responsible for managing SharedPreferences.
package com.reflect.reflect;

// Import necessary packages and classes.
import android.content.Context;
import android.content.SharedPreferences;

// Define a class named SharedPref.
public class SharedPref {

    // Define constant key for storing token.
    public static String KEY_TOKEN = "token";

    // Declare a variable sharedPreferences of type SharedPreferences.
    private SharedPreferences sharedPreferences;

    // Constructor to initialize SharedPref with Context.
    public SharedPref(Context context) {
        // Initialize sharedPreferences with application's SharedPreferences file.
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    // Method to save a string value to SharedPreferences.
    public void saveString(String key, String value) {
        // Put the string value into SharedPreferences with the given key.
        this.sharedPreferences.edit().putString(key, value).apply();
    }

    // Method to retrieve a string value from SharedPreferences.
    public String getString(String key) {
        // Retrieve the string value from SharedPreferences with the given key.
        // Return an empty string if the key is not found.
        return this.sharedPreferences.getString(key, "");
    }
}
