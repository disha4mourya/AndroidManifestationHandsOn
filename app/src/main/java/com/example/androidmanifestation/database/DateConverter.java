package com.example.androidmanifestation.database;

import android.util.Log;

import androidx.room.TypeConverter;

import java.util.Date;


public class DateConverter {

    //this method will be used by appDatabase while saving the date
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    //this method will be used by appDatabase while retrieving the date
    @TypeConverter
    public static Long toLong(Date date) {
        return date == null ? null : date.getTime();
    }
}
