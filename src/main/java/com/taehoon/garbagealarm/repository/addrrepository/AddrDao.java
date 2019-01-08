package com.taehoon.garbagealarm.repository.addrrepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AddrDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AddrRoom... addrRooms);

    @Query("SELECT * FROM AlarmRoom")
    LiveData<List<AddrRoom>> getAll();

    @Query("DELETE FROM AlarmRoom WHERE tag = :tag ")
    void delete(String tag);
}
