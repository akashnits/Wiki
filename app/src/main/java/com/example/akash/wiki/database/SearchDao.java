package com.example.akash.wiki.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM Search ORDER BY createdAt DESC")
    LiveData<List<SearchEntry>> loadAllSearches();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSearch(SearchEntry searchEntry);

    @Delete
    void deleteSearch(SearchEntry searchEntry);

    @Query("DELETE FROM Search")
    public void deleteAllSearches();
}
