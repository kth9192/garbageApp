package com.taehoon.garbagealarm.repository.configrepository;

import android.app.Application;

import com.taehoon.garbagealarm.repository.AppDatabase;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmDao;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class ConfigRepository {

    private ConfigDao configDao;
    private ExecutorService executorService;

    public ConfigRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        configDao = db.configDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(ConfigRoom configRoom) {
        executorService.execute(() -> configDao.insert(configRoom));
    }

    public ConfigRoom getConfig(){
        return configDao.getConfig();
    }

    public void updateAlarmTutorial(boolean value){
        executorService.execute(() -> {
        configDao.updateAlarmTutorial(value);
    });}

    public void updateDeleteTutorial(boolean value){
        executorService.execute(() -> {
        configDao.updateDeleteTutorial(value);
    });}

    public void updateMemoTutorial(boolean value){
        executorService.execute(() -> {
        configDao.updateMemoTutorial(value);
    });}

}
