package com.example.androidmanifestation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    EditText edtTaskDescription;
    RadioGroup rgPriority;
    Button btnAddTask;

    public static final int HIGH_PRIORITY = 1;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;

    // Create AppDatabase member variable for the Database
    AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //Initialize member variable for the data base
        appDatabase = AppDatabase.getInstance(this);
        initViews();
        addTaskListener();
    }

    private void addTaskListener() {
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });
    }

    private void addTask() {
        //Create a description variable and assign to it the value in the edit text
        String description = edtTaskDescription.getText().toString();
        //Create a priority variable and assign the value returned by getPriorityFromViews()
        int priority = getTaskPriority();
        //Create a date variable and assign to it the current Date
        Date date = new Date();

        //Create taskEntry variable using the variables defined above
        TaskEntity taskEntity = new TaskEntity(description, priority, date);
        //Use the taskDao in the AppDatabase variable to insert the taskEntry
        appDatabase.taskDao().insertTask(taskEntity);
        //call finish() to come back to MainActivity
        finish();
    }

    private int getTaskPriority() {
        int selectedPriority = HIGH_PRIORITY;
        int checkedId = ((RadioGroup) findViewById(R.id.rgPriority)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.rbHigh: {
                selectedPriority = HIGH_PRIORITY;
                break;
            }
            case R.id.rbMedium: {
                selectedPriority = MEDIUM_PRIORITY;
                break;
            }
            case R.id.rbLow: {
                selectedPriority = LOW_PRIORITY;
                break;
            }
        }
        return selectedPriority;
    }

    private void initViews() {
        edtTaskDescription = findViewById(R.id.edtTaskDescription);
        rgPriority = findViewById(R.id.rgPriority);
        btnAddTask = findViewById(R.id.btnAddTask);
    }
}
