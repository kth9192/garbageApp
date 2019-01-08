package com.taehoon.garbagealarm.repository.addrrepository;

import android.app.Application;

import com.taehoon.garbagealarm.repository.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class AddrRepository {

    private AddrDao alarmDao;
    private ExecutorService executorService;

    public AddrRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        alarmDao = db.addrDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(AddrRoom addrRoom) {
        executorService.execute(() -> alarmDao.insert(addrRoom));
    }

    public LiveData<List<AddrRoom>> getAll(){
        return alarmDao.getAll();
    }

    public void delete(String key){
        executorService.execute(() -> alarmDao.delete(key));
    }
}
