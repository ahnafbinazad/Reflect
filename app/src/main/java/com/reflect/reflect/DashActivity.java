// Import necessary packages and classes.
package com.reflect.reflect;

// Import Android Bundle, Log, and Toast classes.
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

// Import AppCompatActivity from the androidx.appcompat library.
import androidx.appcompat.app.AppCompatActivity;

// Import BottomNavigationView from the com.google.android.material.bottomnavigation package.
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Import NavController and Navigation from the androidx.navigation package.
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

// Import AppBarConfiguration and NavigationUI from the androidx.navigation.ui package.
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

// Import ActivityDashBinding from the com.reflect.reflect.databinding package.
import com.reflect.reflect.databinding.ActivityDashBinding;

// Import TokenResponse, Call, Callback, and Response from Retrofit library.
import com.reflect.reflect.model.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Define a class named DashActivity that extends BaseActivity.
public class DashActivity extends BaseActivity {

    // Declare a variable binding of type ActivityDashBinding.
    private ActivityDashBinding binding;

    // Override the onCreate method.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the ActivityDashBinding.
        binding = ActivityDashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the BottomNavigationView by its id.
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Initialize NavController by finding the NavHostFragment.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dash);

        // Setup the BottomNavigationView with the NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Load and save access token.
        loadAndSaveToken();
    }

    // Define a method to load and save access token.
    private void loadAndSaveToken() {

        // Show progress dialog.
        showProgressDialog();

        // Make network call to get access token.
        RetrofitProvider.getApiService(RetrofitProvider.TOKEN_URL).getAccessToken("client_credentials")
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        // Hide progress dialog.
                        hideProgress();
                        // Check if the response is successful and not null.
                        if (response.isSuccessful() && response.body() != null) {
                            // Retrieve the token response body.
                            TokenResponse tokenResponse = response.body();
                            // Log the loaded token.
                            Log.d("TAG", "onResponse: TOKEN LOADED : "+tokenResponse.getAccessToken());
                            // Save the token in SharedPreferences.
                            new SharedPref(DashActivity.this).saveString(SharedPref.KEY_TOKEN,tokenResponse.getAccessToken());
                        }
                        else
                        {
                            // Show toast message for failure to load token.
                            Toast.makeText(DashActivity.this, "Spotify token failed to load", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        // Hide progress dialog.
                        hideProgress();
                        // Show toast message for failure to load token.
                        Toast.makeText(DashActivity.this, "Spotify token failed to load", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
