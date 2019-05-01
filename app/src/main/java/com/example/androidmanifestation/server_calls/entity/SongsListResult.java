package com.example.androidmanifestation.server_calls.entity;

import java.util.List;

public class SongsListResult {

    List<SongsEntity> results;

    public List<SongsEntity> getResults() {
        return results;
    }

    public void setResults(List<SongsEntity> results) {
        this.results = results;
    }
}
