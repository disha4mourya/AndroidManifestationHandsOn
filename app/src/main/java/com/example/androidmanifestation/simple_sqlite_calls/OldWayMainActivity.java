package com.example.androidmanifestation.simple_sqlite_calls;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmanifestation.AppExecutors;
import com.example.androidmanifestation.R;
import com.example.androidmanifestation.TaskAdapter;
import com.example.androidmanifestation.database.DateConverter;
import com.example.androidmanifestation.database.TaskEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OldWayMainActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {

    private RecyclerView rvTasks;
    private FloatingActionButton fabAddTask;
    private TaskAdapter taskAdapter;
    private DbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new DbAdapter(this);
        mDbHelper.open();

        initialSetup();
        setAdapterOnRecyclerView();
        setTouchHelperToDeleteTask();

        insertDummyData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTask();
    }

    private void insertDummyData() {
        mDbHelper.truncateAll();
        mDbHelper.insertTask("abcd", "2", String.valueOf(System.currentTimeMillis()));
        mDbHelper.insertTask("cdtf", "2", String.valueOf(System.currentTimeMillis()));
    }

    private void loadTask() {

        List<TaskEntity> songsEntityList = new ArrayList<>();
        Cursor empCurscor = mDbHelper.fetchAllTask();
        while (empCurscor.moveToNext()) {

            int index0 = empCurscor.getColumnIndex(mDbHelper.KEY_ID);

            int index1 = empCurscor.getColumnIndex(mDbHelper.KEY_DESCRIPTION);
            int index3 = empCurscor.getColumnIndex(mDbHelper.KEY_PRIORITY);
            int index4 = empCurscor.getColumnIndex(mDbHelper.KEY_UPDATED_AT);

            int id = empCurscor.getInt(index0);
            String desc = empCurscor.getString(index1);
            int pri = Integer.parseInt(empCurscor.getString(index3));
            Long update = Long.valueOf(empCurscor.getString(index4));

            Date updateAt = DateConverter.toDate(update);

            TaskEntity songsEntity = new TaskEntity(id, desc, pri, updateAt);

            songsEntityList.add(songsEntity);
        }

        taskAdapter.setTasks(songsEntityList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }


    private void setTouchHelperToDeleteTask() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<TaskEntity> taskEntities = taskAdapter.getTasks();
                        mDbHelper.deleteTaskById(String.valueOf(taskEntities.get(position)));
                    }
                });
            }
        }).attachToRecyclerView(rvTasks);
    }


    private void setAdapterOnRecyclerView() {
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, this);
        rvTasks.setAdapter(taskAdapter);
    }


    private void initialSetup() {
        rvTasks = findViewById(R.id.rvTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
    }

    @Override
    public void onTaskClickListener(int taskId) {
    }
}