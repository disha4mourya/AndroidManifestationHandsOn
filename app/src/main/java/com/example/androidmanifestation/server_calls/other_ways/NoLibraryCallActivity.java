package com.example.androidmanifestation.server_calls.other_ways;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.server_calls.adapter.SongsAdapter;
import com.example.androidmanifestation.server_calls.entity.SongsEntity;
import com.example.androidmanifestation.server_calls.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidmanifestation.server_calls.utils.Constants.CONNECTION_TIMEOUT;
import static com.example.androidmanifestation.server_calls.utils.Constants.READ_TIMEOUT;


public class NoLibraryCallActivity extends AppCompatActivity {

    private SongsAdapter songsAdapter;
    RecyclerView rvSongList;
    ProgressBar pbLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_library_call);

        rvSongList = findViewById(R.id.rvSongsList);
        pbLoading = findViewById(R.id.pbLoading);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        rvSongList.setLayoutManager(llm);

        songsAdapter = new SongsAdapter(this);
        rvSongList.setAdapter(songsAdapter);

        fetchList();
    }

    private void fetchList() {
        new AsyncRetrieve(new LoadCallback<List<SongsEntity>>() {
            @Override
            public void onSuccess(List<SongsEntity> response) {
                songsAdapter.setSongs(response);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        }).execute();
    }

    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        LoadCallback<List<SongsEntity>> loadCallback;

        //constructor to pass callback response
        AsyncRetrieve(LoadCallback<List<SongsEntity>> loadCallback) {
            this.loadCallback = loadCallback;
        }

        @Override
        protected void onPreExecute() {
            pbLoading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(Constants.SONGS_LIST_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);

            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            pbLoading.setVisibility(View.GONE);

            if (!result.equals("")) {

                Log.d("responseGet", "is" + result);
                List<SongsEntity> songsEntityList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        SongsEntity songsEntity = new SongsEntity();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.has("trackName")) {
                            songsEntity.setTrackName(jsonObject1.getString("trackName"));
                        }
                        if (jsonObject1.has("collectionName")) {
                            songsEntity.setCollectionName(jsonObject1.getString("collectionName"));
                        }

                        if (jsonObject1.has("artworkUrl100")) {
                            songsEntity.setArtworkUrl100(jsonObject1.getString("artworkUrl100"));
                        }

                        if (jsonObject1.has("trackTimeMillis")) {
                            songsEntity.setTrackTimeMillis(jsonObject1.getString("trackTimeMillis"));
                        }

                        if (jsonObject1.has("artistName")) {
                            songsEntity.setArtistName(jsonObject1.getString("artistName"));
                        }
                        if (jsonObject1.has("collectionPrice")) {
                            songsEntity.setCollectionPrice(jsonObject1.getString("collectionPrice"));
                        }
                        if (jsonObject1.has("trackPrice")) {
                            songsEntity.setTrackPrice(jsonObject1.getString("trackPrice"));
                        }
                        if (jsonObject1.has("releaseDate")) {
                            songsEntity.setReleaseDate(jsonObject1.getString("releaseDate"));
                        }
                        if (jsonObject1.has("trackCensoredName")) {
                            songsEntity.setTrackCensoredName(jsonObject1.getString("trackCensoredName"));
                        }
                        if (jsonObject1.has("collectionViewUrl")) {
                            songsEntity.setCollectionViewUrl(jsonObject1.getString("collectionViewUrl"));
                        }
                        if (jsonObject1.has("artistViewUrl")) {
                            songsEntity.setArtistViewUrl(jsonObject1.getString("artistViewUrl"));
                        }
                        songsEntityList.add(songsEntity);
                    }

                    loadCallback.onSuccess(songsEntityList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
