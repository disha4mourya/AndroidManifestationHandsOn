package com.example.androidmanifestation;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

//implement task click listener
public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {

    private RecyclerView rvTasks;
    private FloatingActionButton fabAddTask;

    //Create AppDatabase member variable for the Database
    private AppDatabase appDatabase;
    //create member variable for adapter
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize member variable for the data base
        appDatabase=AppDatabase.getInstance(this);

        intialiseViews();
        setUpAddTaskListener();
        setAdapterOnRecyclerView();
    }

    private void setAdapterOnRecyclerView() {
        //Initialize member variable for adapter
        taskAdapter=new TaskAdapter(this,this);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        //set adapter on recycler view
        rvTasks.setAdapter(taskAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();


        //Get the diskIO Executor from the instance of AppExecutors and

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<TaskEntity> taskEntityList=appDatabase.taskDao().loadAllTasks();

                //Wrap the setTask call in a call to runOnUiThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Call the adapter's setTasks method using the result
                        taskAdapter.setTasks(taskEntityList);
                    }
                });
            }
        });
    }

    private void setUpAddTaskListener() {
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    private void intialiseViews() {
        rvTasks = findViewById(R.id.rvTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
    }

    @Override
    public void onTaskClickListener(int taskId) {

    }
}
