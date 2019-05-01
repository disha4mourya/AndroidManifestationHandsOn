package com.example.androidmanifestation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;

import java.util.List;

//  make this class extend AndroidViewModel and implement its default constructor
public class MainViewModel extends AndroidViewModel {

    //Add a tasks member variable for a list of TaskEntry objects wrapped in a LiveData
    LiveData<List<TaskEntity>> listLiveData;

    //In the constructor use the loadAllTasks of the taskDao to initialize the tasks variable

    public MainViewModel(@NonNull Application application) {
        super(application);
        listLiveData = AppDatabase.getInstance(application).taskDao().loadAllTasks();
    }
    //Create a getter for the tasks variable
    public LiveData<List<TaskEntity>> getAllTasks(){
        return listLiveData;
    }

}
