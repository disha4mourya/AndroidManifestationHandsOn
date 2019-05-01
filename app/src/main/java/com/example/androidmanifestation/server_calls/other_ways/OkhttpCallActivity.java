package com.example.androidmanifestation.server_calls.other_ways;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.server_calls.adapter.SongsAdapter;
import com.example.androidmanifestation.server_calls.entity.SongsEntity;
import com.example.androidmanifestation.server_calls.entity.SongsListResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.androidmanifestation.server_calls.utils.Constants.SONGS_LIST_URL;

public class OkhttpCallActivity extends AppCompatActivity {

    Context mContext;
    private SongsAdapter songsAdapter;
    RecyclerView rvSongList;
    ProgressBar pbLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_library_call);
        mContext = this;


        rvSongList = findViewById(R.id.rvSongsList);
        pbLoading = findViewById(R.id.pbLoading);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rvSongList.setLayoutManager(llm);

        songsAdapter = new SongsAdapter(this);
        rvSongList.setAdapter(songsAdapter);

        loadSongList();
    }

    void loadSongList() {

        pbLoading.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(SONGS_LIST_URL)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideProgressBar();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                hideProgressBar();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            SongsListResult songsListResult = new Gson().fromJson(response.body().charStream(), SongsListResult.class);
                            List<SongsEntity> songsEntityList = songsListResult.getResults();

                            songsAdapter.setSongs(songsEntityList);

                        }
                    });
                }
            }
        });
    }

    void hideProgressBar(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                pbLoading.setVisibility(View.GONE);
            }
        });
    }
}

