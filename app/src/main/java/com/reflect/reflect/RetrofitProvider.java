// This file provides a RetrofitProvider class responsible for creating Retrofit instances
// and generating ApiService instances for making network requests.
package com.reflect.reflect;

// Import necessary packages and classes.
import com.reflect.reflect.interfaces.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Define a class named RetrofitProvider.
public class RetrofitProvider {

    // Define constant URLs for token, Spotify API, and News API.
    public static String TOKEN_URL = "https://accounts.spotify.com/";
    public static String SPOTIFY_API_URL = "https://api.spotify.com/v1/";
    public static String NEWS_API_URL = "https://newsapi.org/v2/";

    // Define a method to get ApiService with authentication token and base URL.
    public static ApiService getApiService(final String authToken, String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + authToken)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(ApiService.class);
    }

    // Define a method to get ApiService with base URL only.
    public static ApiService getApiService(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }
}
