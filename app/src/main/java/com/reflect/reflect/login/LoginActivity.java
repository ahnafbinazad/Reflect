package com.reflect.reflect.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.reflect.reflect.BaseActivity;
import com.reflect.reflect.DashActivity;
import com.reflect.reflect.R;
import com.reflect.reflect.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    // Binding instance for the activity
    private ActivityLoginBinding binding;

    // Lifecycle method called when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already signed in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // If user is signed in, navigate to main dash activity
            toMainDash();
        }
    }

    // Lifecycle method called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using the binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adjust window to resize when keyboard appears
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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

        // Set onClickListener for Sign In button
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Set onClickListener for "Create Account" text view to navigate to register activity
        binding.txtCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    // Method to handle user sign-in
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
        // Sign in with email and password using FirebaseAuth
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Hide progress dialog
                hideProgress();
                if (task.isSuccessful()) {
                    // If sign-in is successful, navigate to main dash activity
                    toMainDash();
                } else {
                    // If sign-in fails, display error message
                    binding.txtError.setText(task.getException().getMessage());
                }
            }
        });
    }

    // Method to navigate to main dash activity
    private void toMainDash() {
        startActivity(new Intent(LoginActivity.this, DashActivity.class));
        finishAfterTransition();
    }
}
