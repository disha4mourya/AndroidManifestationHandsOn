package com.example.androidmanifestation.simple_sqlite_calls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "todoold";
    private static final String TASK_ONE_TABLE = "task1";

    private static final int DATABASE_VERSION = 1;

    private final Context context;

    //define table columns
    public static final String KEY_ID = "id";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_UPDATED_AT = "updated_at";


    //create table
    private static final String TABLE_TASK =
            "create table task1 (id integer primary key autoincrement, "
                    + "description text not null, priority text not null, updated_at text not null);";


    public static class DatabaseHelper extends SQLiteOpenHelper {

        //defining table and version
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_TASK);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS task1");
            onCreate(db);
        }
    }


    public DbAdapter(Context context) {
        this.context = context;
    }

    //open the database for querying
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    //closing the connection
    public void close() {
        mDbHelper.close();
    }

    //insert the values in the table
    public long insertTask(String description, String priority, String updatedAt) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_PRIORITY, priority);
        initialValues.put(KEY_UPDATED_AT, updatedAt);

        return mDb.insert(TASK_ONE_TABLE, null, initialValues);
    }


    public Integer getTaskLength() {
        Cursor mCount = mDb.rawQuery("select count(*) from task1", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }


    //fetch all the tasks
    public Cursor fetchAllTask() {

        return mDb.query(TASK_ONE_TABLE, new String[]{KEY_ID, KEY_PRIORITY, KEY_DESCRIPTION, KEY_UPDATED_AT
        }, null, null, null, null, null);
    }

    public Cursor fetchTaskById(String id) {

        return mDb.query(TASK_ONE_TABLE, new String[]{KEY_ID, KEY_PRIORITY, KEY_DESCRIPTION, KEY_UPDATED_AT
        }, KEY_ID + " = '" + id + "'", null, null, null, null);
    }


    //delete the task by id
    public void deleteTaskById(String id) {
        mDb.delete(TASK_ONE_TABLE, KEY_ID + " = ?", new String[]{id});
    }


    //truncate the table
    public void truncateAll() {
        mDb.delete(TASK_ONE_TABLE, null, null);
    }


}