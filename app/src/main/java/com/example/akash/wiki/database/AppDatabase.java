package com.example.akash.wiki.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {SearchEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG= AppDatabase.class.getSimpleName();
    private static final Object LOCK= new Object();
    private static final String DATABASE_NAME= "recentSearchesDb";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.v(TAG, "Creating database instance");
                sInstance= Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract SearchDao searchDao();
}
