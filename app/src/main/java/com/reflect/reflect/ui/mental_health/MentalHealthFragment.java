// Import necessary packages and classes.
package com.reflect.reflect.ui.mental_health;

// Import Android Fragment, LayoutInflater, View, ViewGroup, Bundle, and Toast classes.
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

// Import androidx.annotation.NonNull and androidx.annotation.Nullable annotations.
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Import androidx.lifecycle.Observer and ViewModelProvider from androidx.lifecycle package.
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

// Import Retrofit Callback and Response from retrofit2 package.
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Import RecyclerView and RecyclerView.Adapter from AndroidX RecyclerView package.
import androidx.recyclerview.widget.RecyclerView;

// Import NewsAdapter from the com.reflect.reflect.adapter package.
import com.reflect.reflect.adapter.NewsAdapter;

// Import FragmentMentalHealthBinding from the com.reflect.reflect.databinding package.
import com.reflect.reflect.databinding.FragmentMentalHealthBinding;

// Import MentalHealthRecords from the com.reflect.reflect.model package.
import com.reflect.reflect.model.Article;
import com.reflect.reflect.model.MentalHealthRecords;

// Import RetrofitProvider from the com.reflect.reflect package.
import com.reflect.reflect.RetrofitProvider;

import java.util.Collections;
import java.util.Comparator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

// Define a class named MentalHealthFragment that extends Fragment.
public class MentalHealthFragment extends Fragment {

    // Declare variables.
    private MentalHealthViewModel mViewModel;
    private FragmentMentalHealthBinding binding;
    private NewsAdapter newsAdapter;

    // Override the onCreateView method.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout using FragmentMentalHealthBinding.
        binding = FragmentMentalHealthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Override the onViewCreated method.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize NewsAdapter with the application context.
        newsAdapter = new NewsAdapter(requireContext());

        // Initialize the ViewModel using ViewModelProvider.
        mViewModel = new ViewModelProvider(this).get(MentalHealthViewModel.class);

        // Observe the LiveData for MentalHealthRecords changes.
        mViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<MentalHealthRecords>() {
            @Override
            public void onChanged(MentalHealthRecords mentalHealthRecords) {
                // Sort the articles by date published
                Collections.sort(mentalHealthRecords.getArticles(), new Comparator<Article>() {
                    @Override
                    public int compare(Article article1, Article article2) {
                        // Parse the dates from strings to Date objects
                        Date date1 = parseDate(article1.getPublishedAt());
                        Date date2 = parseDate(article2.getPublishedAt());

                        // Compare the dates
                        if (date1 != null && date2 != null) {
                            return date2.compareTo(date1); // Sorting in descending order
                        } else {
                            return 0; // Handle if parsing fails, or dates are null
                        }
                    }
                });

                // Update the NewsAdapter with the sorted MentalHealthRecords data.
                newsAdapter.setArticles(mentalHealthRecords.getArticles());
            }
        });

        // Set the RecyclerView adapter to the NewsAdapter.
        binding.recyclerView.setAdapter(newsAdapter);

        // If MentalHealthRecords LiveData is null, load news.
        if (mViewModel.getRecords().getValue() == null) {
            loadNews();
        }
    }

    // Method to parse date from string. Used to organise articles in onCreate
    private Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }

    // Method to load news data.
    private void loadNews() {
        // Make a network call to get news data.
        RetrofitProvider.getApiService(RetrofitProvider.NEWS_API_URL).getNews("mental health", "235868cd3a2546a39e01f97606317d8e").enqueue(new Callback<MentalHealthRecords>() {
            @Override
            public void onResponse(Call<MentalHealthRecords> call, Response<MentalHealthRecords> response) {
                // Check if the response is successful and not null.
                if (response.isSuccessful() && response.body() != null) {
                    // Get the retrieved MentalHealthRecords data.
                    MentalHealthRecords records = response.body();
                    // Set the MentalHealthRecords data to the ViewModel.
                    mViewModel.setMentalHealthRecords(records);
                } else {
                    // Show toast message for error.
                    Toast.makeText(requireContext(), "ERROR : 101", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MentalHealthRecords> call, Throwable t) {
                // Show toast message for failure.
                Toast.makeText(requireContext(), "ERROR : 102" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
