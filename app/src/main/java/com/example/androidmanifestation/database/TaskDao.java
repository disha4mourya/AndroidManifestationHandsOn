package com.example.androidmanifestation.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    //Wrap the return type with LiveData
    @Query("Select * from tasks ORDER BY priority")
    LiveData<List<TaskEntity>> loadAllTasks();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntity taskEntity);

    @Delete
    void deleteTask(TaskEntity taskEntity);

    @Insert
    void insertTask(TaskEntity taskEntity);

    //Wrap the return type with LiveData
    //Create a Query method named loadTaskById that receives an int id and returns a TaskEntry Object
    @Query("Select * from tasks where id=:id")
    LiveData<TaskEntity> loadTaskById(int id);
}
