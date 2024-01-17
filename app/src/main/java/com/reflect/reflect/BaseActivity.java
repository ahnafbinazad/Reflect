package com.reflect.reflect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class BaseActivity extends AppCompatActivity {

    private Dialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog =new Dialog(this);
        progressDialog.setContentView(R.layout.custom_progress_dialog); // Set the layout for the dialog
        progressDialog.setCancelable(false);
    }

    protected void showProgressDialog() {
        if (progressDialog != null)
            progressDialog.show();
    }

    protected void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
