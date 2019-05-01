package com.example.androidmanifestation.server_calls.retrofit_call;

import com.example.androidmanifestation.server_calls.entity.SongsListResult;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitServices {

    @GET("/search?term=Michael+jackson")
    Call<SongsListResult> fetchAllSongs();
}
