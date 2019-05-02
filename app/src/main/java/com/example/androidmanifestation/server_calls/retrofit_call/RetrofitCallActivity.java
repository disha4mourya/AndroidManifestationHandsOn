package com.example.androidmanifestation.server_calls.retrofit_call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Retrofit");
        }

        rvSongList = findViewById(R.id.rvSongsList);
        pbLoading = findViewById(R.id.pbLoading);
        GridLayoutManager llm = new GridLayoutManager(this, 2);
        llm.setOrientation(RecyclerView.VERTICAL);
        rvSongList.setLayoutManager(llm);

        songsAdapter = new SongsAdapter(this);
        rvSongList.setAdapter(songsAdapter);

        loadSongList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadSongList() {
        pbLoading.setVisibility(View.VISIBLE);

        RetrofitServices retrofitServices = RetrofitInstance.getRetrofitInstance().create(RetrofitServices.class);
        Call<SongsListResult> call = retrofitServices.fetchAllSongs();
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
