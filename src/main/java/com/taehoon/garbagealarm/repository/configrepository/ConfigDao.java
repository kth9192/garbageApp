package com.taehoon.garbagealarm.repository.configrepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ConfigDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ConfigRoom... configRooms);

    @Query("SELECT * FROM ConfigRoom")
    ConfigRoom getConfig();

    @Query("UPDATE ConfigRoom SET alarm_newbie = :value")
    void updateAlarmTutorial(boolean value);

    @Query("UPDATE ConfigRoom SET delete_newbie = :value")
    void updateDeleteTutorial(boolean value);

    @Query("UPDATE ConfigRoom SET memo_newbie = :value")
    void updateMemoTutorial(boolean value);
}
