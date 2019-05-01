package com.example.androidmanifestation.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

//Make this class extend ViewModel
public class AddTaskViewModel extends ViewModel {

    //Add a task member variable for the TaskEntry object wrapped in a LiveData
    private LiveData<TaskEntity> taskEntityLiveData;

    //Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    public AddTaskViewModel(AppDatabase appDatabase,int taskId){
        taskEntityLiveData=appDatabase.taskDao().loadTaskById(taskId);
    }

    // Create a getter for the task variable
    public LiveData<TaskEntity>  getTask(){
        return taskEntityLiveData;
    }
}
