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

    private ActivityLoginBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            toMainDash();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        binding.txtCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
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
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgress();
                if (task.isSuccessful()) {
                    toMainDash();
                } else {
                    binding.txtError.setText(task.getException().getMessage());
                }
            }
        });
    }

    private void toMainDash() {
        startActivity(new Intent(LoginActivity.this, DashActivity.class));
        finishAfterTransition();
    }
}
