package com.example.androidmanifestation.server_calls.other_ways;

public interface LoadCallback<T> {
    void onSuccess(T response);
    void onFailure(Throwable throwable);
}
