// Import necessary packages and classes.
package com.reflect.reflect;

// Import Context, View, and InputMethodManager from Android framework.
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

// Define a class named KeyboardUtils.
public class KeyboardUtils {

    // Define a static method to hide the keyboard.
    public static void hideKeyboard(View view) {
        // Get the InputMethodManager service from the context of the given view.
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // Check if InputMethodManager is not null.
        if (inputMethodManager != null) {
            // Hide the soft keyboard associated with the given view.
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
