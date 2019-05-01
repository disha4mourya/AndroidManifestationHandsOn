package com.example.androidmanifestation.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("Select * from tasks ORDER BY priority")
    List<TaskEntity> loadAllTasks();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntity taskEntity);

    @Delete
    void deleteTask(TaskEntity taskEntity);

    @Insert
    void insertTask(TaskEntity taskEntity);
}
