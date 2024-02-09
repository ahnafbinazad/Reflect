// Import necessary packages and classes.
package com.reflect.reflect;

// Import AppCompatActivity from the androidx.appcompat library.
import androidx.appcompat.app.AppCompatActivity;

// Import Dialog and ProgressDialog from the android.app library.
import android.app.Dialog;
import android.app.ProgressDialog;

// Import Bundle from the android.os library.
import android.os.Bundle;

// Import annotations for nullable and overrides.
import androidx.annotation.Nullable;

// Import AppCompatActivity from the androidx.appcompat library again.
import androidx.appcompat.app.AppCompatActivity;

// Import LottieAnimationView from a third-party library.
import com.airbnb.lottie.LottieAnimationView;

// Define a class named BaseActivity that extends AppCompatActivity.
public class BaseActivity extends AppCompatActivity {

    // Declare a private variable progressDialog of type Dialog.
    private Dialog progressDialog;

    // Override the onCreate method of AppCompatActivity.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate progressDialog as a new Dialog with the current context.
        progressDialog = new Dialog(this);
        // Set the layout for the progressDialog using a custom layout file named "custom_progress_dialog".
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        // Ensure that the progressDialog is not cancellable by pressing the back button.
        progressDialog.setCancelable(false);
    }

    // Define a method named showProgressDialog, accessible to subclasses.
    protected void showProgressDialog() {
        // Check if progressDialog is not null, then show the progressDialog.
        if (progressDialog != null)
            progressDialog.show();
    }

    // Define a method named hideProgress, accessible to subclasses.
    protected void hideProgress() {
        // Check if progressDialog is not null and is currently showing, then dismiss it.
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
