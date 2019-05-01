package com.example.androidmanifestation.server_calls.retrofit_call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.server_calls.adapter.SongsAdapter;
import com.example.androidmanifestation.server_calls.entity.SongsEntity;
import com.example.androidmanifestation.server_calls.entity.SongsListResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallActivity extends AppCompatActivity {

    private SongsAdapter songsAdapter;
    RecyclerView rvSongList;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_call);

        rvSongList = findViewById(R.id.rvSongsList);
        pbLoading = findViewById(R.id.pbLoading);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rvSongList.setLayoutManager(llm);

        songsAdapter = new SongsAdapter(this);
        rvSongList.setAdapter(songsAdapter);

        loadSongList();
    }

    private void loadSongList() {
        pbLoading.setVisibility(View.VISIBLE);

        RetrofitServices retrofitServices = RetrofitInstance.getRetrofitInstance().create(RetrofitServices.class);
        Call<SongsListResult> call=retrofitServices.fetchAllSongs();
        call.enqueue(new Callback<SongsListResult>() {
            @Override
            public void onResponse(retrofit2.Call<SongsListResult> call, Response<SongsListResult> response) {
                pbLoading.setVisibility(View.GONE);
                songsAdapter.setSongs(response.body().getResults());
            }

            @Override
            public void onFailure(retrofit2.Call<SongsListResult> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
            }
        });

    }
}
