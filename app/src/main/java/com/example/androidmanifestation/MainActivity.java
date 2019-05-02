package com.example.androidmanifestation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;
import com.example.androidmanifestation.server_calls.ServerCallOptions;
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
        appDatabase = AppDatabase.getInstance(this);

        intialiseViews();
        setUpAddTaskListener();
        setAdapterOnRecyclerView();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                //Get the diskIO Executor from the instance of AppExecutors and
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        //get the position from the viewHolder parameter
                        int position = viewHolder.getAdapterPosition();

                        //Call deleteTask in the taskDao with the task at that position
                        List<TaskEntity> taskEntityList = taskAdapter.getTasks();
                        appDatabase.taskDao().deleteTask(taskEntityList.get(position));

                        //Remove the call to retrieveTasks
                        //Call retrieveTasks method to refresh the UI
                    }
                });
            }
        }).attachToRecyclerView(rvTasks);

        //remove retrieve task from here
        //add set up view model
        setUpViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_server_call_options) {
            Intent intent = new Intent(MainActivity.this, ServerCallOptions.class);
            startActivity(intent);
        }
        return true;
    }

    private void setAdapterOnRecyclerView() {
        //Initialize member variable for adapter
        taskAdapter = new TaskAdapter(this, this);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        //set adapter on recycler view
        rvTasks.setAdapter(taskAdapter);
    }


    private void setUpViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getAllTasks().observe(this, new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(List<TaskEntity> taskEntities) {
                taskAdapter.setTasks(taskEntities);
            }
        });
    }

    private void setUpAddTaskListener() {
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
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
        //Launch AddTaskActivity with itemId as extra for the key AddTaskActivity.EXTRA_TASK_ID
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }
}
