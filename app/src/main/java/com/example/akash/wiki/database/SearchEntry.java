package com.example.akash.wiki.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

import java.util.Date;

@Entity(tableName = "Search", indices = {@Index(value = {"keyword"},
        unique = true)})
public class SearchEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "keyword")
    private String keyword;
    private Date createdAt;

    @Ignore
    public SearchEntry(int id, String keyword, Date createdAt) {
        this.id = id;
        this.keyword = keyword;
        this.createdAt = createdAt;
    }

    public SearchEntry(String keyword, Date createdAt) {
        this.keyword = keyword;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
