package com.reflect.reflect.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.reflect.reflect.R;

public class BaseFragment extends Fragment {

    private Dialog progressDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog =new Dialog(requireContext());
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
