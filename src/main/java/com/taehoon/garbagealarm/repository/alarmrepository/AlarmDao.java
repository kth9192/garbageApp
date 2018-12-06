package com.taehoon.garbagealarm.repository.alarmrepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AlarmRoom... alarmRooms);

    @Query("SELECT * FROM AlarmRoom")
    LiveData<List<AlarmRoom>> getAll();

    @Query("DELETE FROM AlarmRoom WHERE tag = :tag ")
    void delete(String tag);
}
