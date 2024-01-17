package com.reflect.reflect.ui.mental_health;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.reflect.reflect.R;
import com.reflect.reflect.RetrofitProvider;
import com.reflect.reflect.adapter.NewsAdapter;
import com.reflect.reflect.databinding.FragmentMentalHealthBinding;
import com.reflect.reflect.model.MentalHealthRecords;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MentalHealthFragment extends Fragment {

    private MentalHealthViewModel mViewModel;
    private FragmentMentalHealthBinding binding;
    private NewsAdapter newsAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=FragmentMentalHealthBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsAdapter=new NewsAdapter(requireContext());
        mViewModel = new ViewModelProvider(this).get(MentalHealthViewModel.class);
        mViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<MentalHealthRecords>() {
            @Override
            public void onChanged(MentalHealthRecords mentalHealthRecords) {
                newsAdapter.setArticles(mentalHealthRecords.getArticles());
            }
        });

        binding.recyclerView.setAdapter(newsAdapter);
        if(mViewModel.getRecords().getValue()==null)
        {
            loadNews();
        }
    }

    private void loadNews() {
        RetrofitProvider.getApiService(RetrofitProvider.NEWS_API_URL).getNews("mental health","235868cd3a2546a39e01f97606317d8e").enqueue(new Callback<MentalHealthRecords>() {
            @Override
            public void onResponse(Call<MentalHealthRecords> call, Response<MentalHealthRecords> response) {

                if(response.isSuccessful() && response.body()!=null)
                {
                    MentalHealthRecords records=response.body();
                    mViewModel.setMentalHealthRecords(records);
                }
                else
                {
                    Toast.makeText(requireContext(), "ERROR : 101", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MentalHealthRecords> call, Throwable t) {
                Toast.makeText(requireContext(), "ERROR : 102"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}