package com.taehoon.garbagealarm.repository;

import android.content.Context;

import com.taehoon.garbagealarm.repository.alarmrepository.AlarmDao;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmConverter;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;
import com.taehoon.garbagealarm.repository.configrepository.ConfigDao;
import com.taehoon.garbagealarm.repository.configrepository.ConfigRoom;
import com.taehoon.garbagealarm.repository.memorepository.MemoDao;
import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MemoRoom.class, AlarmRoom.class, ConfigRoom.class}, version = 1, exportSchema = false)
@TypeConverters({AlarmConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MemoDao chatDao();
    public abstract AlarmDao alarmDao();
    public abstract ConfigDao configDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context){
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database")
                            .build();
                }
            }
        }
        return INSTANCE;

    }
}
