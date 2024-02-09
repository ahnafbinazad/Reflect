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

    // ViewModel instance
    private HistoryViewModel mViewModel;

    // Date pattern for formatting
    private String DATE_PATTERN = "d' 'MMMM', 'yyyy";

    // Binding instance for the fragment
    private FragmentHistoryBinding binding;

    // Adapter for RecyclerView
    private MyDayAdapter myDayAdapter;

    // Calendar instance to manage dates
    Calendar calendar;

    // Static method to create a new instance of HistoryFragment
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    // Lifecycle method called when the view is created
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using the binding
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Lifecycle method called after the view is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize calendar with current date and time
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Initialize RecyclerView adapter
        myDayAdapter = new MyDayAdapter();

        // Initialize ViewModel
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        // Set click listener for date text view to show date picker
        binding.txtDate.setOnClickListener(view1 -> {
            showDatePicker();
        });

        // Set adapter for RecyclerView
        binding.recyclerView.setAdapter(myDayAdapter);

        // Observe changes in MyDay list LiveData and update adapter accordingly
        mViewModel.listMyDay.observe(getViewLifecycleOwner(), new Observer<List<MyDay>>() {
            @Override
            public void onChanged(List<MyDay> myDays) {
                myDayAdapter.setMyDayList(myDays);
            }
        });

        // Observe changes in selected time LiveData
        mViewModel.selectedTime.observe(getViewLifecycleOwner(), aLong -> {
            // Update date text view with selected date
            binding.txtDate.setText(new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(aLong));
            // Load data for the selected date
            loadData();
        });

        // Set initial date value if not already set
        if (mViewModel.selectedTime.getValue() != null) {
            calendar.setTimeInMillis(mViewModel.selectedTime.getValue());
        } else {
            mViewModel.selectedTime.setValue(calendar.getTimeInMillis());
        }
    }

    // Method to show date picker dialog
    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(requireContext());
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Set selected date on calendar
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                // Update selected time LiveData with selected date
                mViewModel.selectedTime.setValue(calendar.getTimeInMillis());
            }
        });
        // Set maximum date for date picker to current date
        dialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        long maxDateMillis = System.currentTimeMillis();
        dialog.getDatePicker().setMaxDate(maxDateMillis);
        dialog.show();
    }

    // Method to load data for the selected date
    private void loadData() {
        // Show progress UI
        showProgress();
        // Get current user's UID
        String mUid = FirebaseAuth.getInstance().getUid();
        // Get Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Query Firestore for MyDay documents with matching UID and date
        db.collection("myday").whereEqualTo("uid", mUid).whereEqualTo("date", calendar.getTimeInMillis()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<MyDay> list = new ArrayList<>();
                if (task.isSuccessful()) {
                    // Iterate through query results and convert documents to MyDay objects
                    task.getResult().forEach(new Consumer<QueryDocumentSnapshot>() {
                        @Override
                        public void accept(QueryDocumentSnapshot queryDocumentSnapshot) {
                            MyDay myDay = queryDocumentSnapshot.toObject(MyDay.class);
                            list.add(myDay);
                        }
                    });
                }
                // Update MyDay list LiveData with loaded data
                mViewModel.listMyDay.setValue(list);
                // Hide progress UI if data is loaded
                hideProgress(!list.isEmpty());
            }
        });
    }

    // Method to hide progress UI and show appropriate views based on data availability
    private void hideProgress(boolean hasResult) {
        binding.lytProgress.parent.setVisibility(View.GONE);
        binding.txtNotFoundMsg.setVisibility(hasResult ? View.GONE : View.VISIBLE);
        binding.recyclerView.setVisibility(hasResult ? View.VISIBLE : View.GONE);
    }

    // Method to show progress UI
    private void showProgress() {
        binding.txtNotFoundMsg.setVisibility(View.GONE);
        binding.lytProgress.parent.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }
}
