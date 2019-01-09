package com.taehoon.garbagealarm.repository.addrrepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AddrDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AddrRoom... addrRooms);

    @Query("SELECT * FROM AddrRoom")
    LiveData<List<AddrRoom>> getAll();

    @Query("SELECT * FROM AddrRoom")
    List<AddrRoom> getAllAsync();

    @Query("SELECT COUNT(id) FROM AddrRoom")
    int getItemCount();

}
