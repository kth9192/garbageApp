package com.taehoon.garbagealarm.repository.memorepository;

import java.util.List;
import java.util.ListIterator;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MemoRoom... memoRooms);

    @Query("SELECT * FROM MemoRoom")
    LiveData<List<MemoRoom>> getAll();

    @Query("SELECT * FROM MemoRoom WHERE dayTxt = :dayTxt")
    LiveData<List<MemoRoom>> getDayList(String dayTxt);

    @Query("DELETE FROM MemoRoom WHERE tag = :key")
    void delete(String key);

}
