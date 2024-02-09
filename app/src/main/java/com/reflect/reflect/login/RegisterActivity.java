package com.reflect.reflect.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.reflect.reflect.BaseActivity;
import com.reflect.reflect.DashActivity;
import com.reflect.reflect.R;
import com.reflect.reflect.databinding.ActivityRegisterBinding;

public class RegisterActivity extends BaseActivity {

    // Binding instance for the activity
    private ActivityRegisterBinding binding;

    // Lifecycle method called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using the binding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find email and password EditText views
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        // Add TextWatcher for etEmail to change text color to white
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Change the text color to white
                etEmail.setTextColor(getResources().getColor(android.R.color.white));
            }
        });

        // Add TextWatcher for etPassword to change text color to white
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Change the text color to white
                etPassword.setTextColor(getResources().getColor(android.R.color.white));
            }
        });

        // Set onClickListener for Create Account button
        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Set onClickListener for "Already have an account?" text view
        binding.txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the activity to navigate back to login screen
                finish();
            }
        });
    }

    // Method to handle user registration
    private void signIn() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            // Display error message if email or password is empty
            binding.txtError.setText("All fields are required.");
            return;
        } else {
            // Clear error message
            binding.txtError.setText(null);
        }
        // Show progress dialog
        showProgressDialog();
        // Create user with email and password using FirebaseAuth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Hide progress dialog
                hideProgress();
                if (task.isSuccessful()) {
                    // If registration is successful, start DashActivity and finish current activity
                    startActivity(new Intent(RegisterActivity.this, DashActivity.class));
                    finishAfterTransition();
                } else {
                    // If registration fails, display error message
                    binding.txtError.setText(task.getException().getMessage());
                }
            }
        });
    }

    // Method to handle back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Finish the activity
        finish();
    }
}
