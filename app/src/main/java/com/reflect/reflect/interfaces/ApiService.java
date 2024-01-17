package com.reflect.reflect.interfaces;

import com.reflect.reflect.model.MentalHealthRecords;
import com.reflect.reflect.model.SpotifyRecords;
import com.reflect.reflect.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @Headers({
            "Authorization: Basic MjE2NDNhZGE0ODkwNDdiZGFlZmZhOWRjNmJhODA5Njg6YTgyMmY5ZmQ1MjAzNDA4ZmI4NTA4YzA0ZTNjZGQ1Y2Q="
    })
    @POST("api/token")
    Call<TokenResponse> getAccessToken(
            @Field("grant_type") String grantType
    );


    @GET("search")
    Call<SpotifyRecords> searchTracks(
            @Query("q") String query,
            @Query("type") String type,
            @Query("limit") int limit
    );



    @GET("everything")
    Call<MentalHealthRecords> getNews(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );
}