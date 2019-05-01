package com.example.androidmanifestation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidmanifestation.database.AddTaskViewModel;
import com.example.androidmanifestation.database.AppDatabase;

// Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    // Add two member variables. One for the database and one for the taskId
    private final AppDatabase appDatabase;
    private final int mTaskId;

    //Initialize the member variables in the constructor with the parameters received
    public AddTaskViewModelFactory(AppDatabase appDatabase, int mTaskId) {
        this.appDatabase = appDatabase;
        this.mTaskId = mTaskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTaskViewModel(appDatabase,mTaskId);
    }
}
