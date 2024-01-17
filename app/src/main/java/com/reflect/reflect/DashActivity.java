package com.reflect.reflect;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.reflect.reflect.databinding.ActivityDashBinding;
import com.reflect.reflect.model.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashActivity extends BaseActivity {

    private ActivityDashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dash);
        NavigationUI.setupWithNavController(binding.navView, navController);
        loadAndSaveToken();
    }

    private void loadAndSaveToken() {

        showProgressDialog();
        RetrofitProvider.getApiService(RetrofitProvider.TOKEN_URL).getAccessToken("client_credentials")
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        hideProgress();
                        if (response.isSuccessful() && response.body() != null) {
                            TokenResponse tokenResponse = response.body();
                            Log.d("TAG", "onResponse: TOKEN LOADED : "+tokenResponse.getAccessToken());
                            new SharedPref(DashActivity.this).saveString(SharedPref.KEY_TOKEN,tokenResponse.getAccessToken());
                        }
                        else
                        {
                            Toast.makeText(DashActivity.this, "Spotify token failed to load", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        hideProgress();
                        Toast.makeText(DashActivity.this, "Spotify token failed to load", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}