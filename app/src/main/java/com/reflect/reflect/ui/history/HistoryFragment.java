package com.reflect.reflect.ui.history;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reflect.reflect.R;
import com.reflect.reflect.adapter.MyDayAdapter;
import com.reflect.reflect.databinding.FragmentHistoryBinding;
import com.reflect.reflect.model.MyDay;
import com.reflect.reflect.ui.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class HistoryFragment extends BaseFragment {

    private HistoryViewModel mViewModel;
    private String DATE_PATTERN = "d' 'MMMM', 'yyyy";

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    private FragmentHistoryBinding binding;
    private MyDayAdapter myDayAdapter;

    Calendar calendar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        myDayAdapter = new MyDayAdapter();
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        binding.txtDate.setOnClickListener(view1 -> {
            showDatePicker();
        });
        binding.recyclerView.setAdapter(myDayAdapter);
        mViewModel.listMyDay.observe(getViewLifecycleOwner(), new Observer<List<MyDay>>() {
            @Override
            public void onChanged(List<MyDay> myDays) {
                myDayAdapter.setMyDayList(myDays);

            }
        });
        mViewModel.selectedTime.observe(getViewLifecycleOwner(), aLong -> {
            binding.txtDate.setText(new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(aLong));
            loadData();
        });

//        if (mViewModel.listMyDay.getValue() == null)
//            loadData();
        if (mViewModel.selectedTime.getValue() != null) {
            calendar.setTimeInMillis(mViewModel.selectedTime.getValue());
        } else {
            mViewModel.selectedTime.setValue(calendar.getTimeInMillis());
        }
    }

    private void showDatePicker() {

        DatePickerDialog dialog = new DatePickerDialog(requireContext());
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                mViewModel.selectedTime.setValue(calendar.getTimeInMillis());
            }
        });
        dialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        long maxDateMillis = System.currentTimeMillis();
        dialog.getDatePicker().setMaxDate(maxDateMillis);
        dialog.show();
    }

    private void loadData() {


        showProgress();
        String mUid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("myday").whereEqualTo("uid", mUid).whereEqualTo("date", calendar.getTimeInMillis()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<MyDay> list = new ArrayList<>();
                if (task.isSuccessful()) {

                    task.getResult().forEach(new Consumer<QueryDocumentSnapshot>() {
                        @Override
                        public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                            MyDay myDay = queryDocumentSnapshot.toObject(MyDay.class);
                            list.add(myDay);
                        }
                    });

                }
                mViewModel.listMyDay.setValue(list);
                hideProgress(!list.isEmpty());
            }
        });
    }


    private void hideProgress(boolean hasResult) {

        binding.lytProgress.parent.setVisibility(View.GONE);
        binding.txtNotFoundMsg.setVisibility(hasResult ? View.GONE : View.VISIBLE);
        binding.recyclerView.setVisibility(hasResult ? View.VISIBLE : View.GONE);


    }

    private void showProgress() {
        binding.txtNotFoundMsg.setVisibility(View.GONE);
        binding.lytProgress.parent.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }
}