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

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        // Add TextWatcher for etEmail
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

        // Add TextWatcher for etPassword
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

        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        binding.txtHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void signIn() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            binding.txtError.setText("All fields are required.");
            return;
        } else binding.txtError.setText(null);
        showProgressDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgress();
                if (task.isSuccessful()) {
                    startActivity(new Intent(RegisterActivity.this, DashActivity.class));
                    finishAfterTransition();
                } else {
                    binding.txtError.setText(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
