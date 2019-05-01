package com.example.androidmanifestation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    // Constants for priority
    public static final int HIGH_PRIORITY = 1;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;

    // Create AppDatabase member variable for the Database
    AppDatabase appDatabase;

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constant for default task id to be used when not in update mode
    public static final int DEFAULT_TASK_ID = -1;

    private int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //Initialize member variable for the data base
        appDatabase = AppDatabase.getInstance(this);
        initViews();
        addTaskListener();

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            btnAddTask.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final TaskEntity taskEntity = appDatabase.taskDao().loadTaskById(mTaskId);
                        populateUi(taskEntity);
                    }
                });
            }
        }
    }

    private void populateUi(TaskEntity taskEntity) {
        if (taskEntity == null) {
            return;
        }
        edtTaskDescription.setText(taskEntity.getDescription());
        setPriorityInViews(taskEntity.getPriority());
    }

    private void setPriorityInViews(int priority) {
        switch (priority) {
            case HIGH_PRIORITY:
                (rgPriority).check(R.id.rbHigh);
                break;
            case MEDIUM_PRIORITY:
                (rgPriority).check(R.id.rbMedium);
                break;
            case LOW_PRIORITY:
                (rgPriority).check(R.id.rbLow);
                break;
        }
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
        //make taskEntry final so it is visible inside the run method
        final TaskEntity taskEntity = new TaskEntity(description, priority, date);

        //Get the diskIO Executor from the instance of AppExecutors and
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (mTaskId==DEFAULT_TASK_ID) {
                    //Use the taskDao in the AppDatabase variable to insert the taskEntry
                    appDatabase.taskDao().insertTask(taskEntity);
                }else {
                    taskEntity.setId(mTaskId);
                    appDatabase.taskDao().updateTask(taskEntity);
                }
                //call finish() to come back to MainActivity
                finish();
            }
        });

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
