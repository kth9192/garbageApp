package com.taehoon.garbagealarm.repository.alarmrepository;

import android.app.Application;

import com.taehoon.garbagealarm.repository.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class AlarmRepository {

    private AlarmDao alarmDao;
    private ExecutorService executorService;

    public AlarmRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        alarmDao = db.alarmDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(AlarmRoom alarmRoom) {
        executorService.execute(() -> alarmDao.insert(alarmRoom));
    }

    public LiveData<List<AlarmRoom>> getAll(){
        return alarmDao.getAll();
    }

    public void delete(String key){
        executorService.execute(() -> alarmDao.delete(key));
    }
}
