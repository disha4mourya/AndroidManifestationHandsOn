package com.example.androidmanifestation.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

//Annotate as database and define version as 1
@Database(entities = {TaskEntity.class},version = 1,exportSchema = false)
//provide a type converter to deal with date data type
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase dbInstance;
    private static final Object LOCK=new Object();
    private static final String DATABASE_NAME="todolist";
    private static String TAG=AppDatabase.class.getSimpleName();

    public static AppDatabase getInstance(Context context){
        if (dbInstance==null){
            Log.d(TAG, "Creating new database instance");

            synchronized (LOCK) {
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME)
                        // COMPLETED (2) call allowMainThreadQueries before building the instance
                        // Queries should be done in a separate thread to avoid locking the UI
                        // We will allow this ONLY TEMPORALLY to see that our DB is working
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");

        return dbInstance;
    }
    public abstract TaskDao taskDao();
}
