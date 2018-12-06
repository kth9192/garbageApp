package com.taehoon.garbagealarm.repository.memorepository;

import android.app.Application;

import com.taehoon.garbagealarm.repository.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class MemoRepository {

    private MemoDao memoDao;
    private ExecutorService executorService;

    public MemoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        memoDao = db.chatDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(MemoRoom memoRoom) {
        executorService.execute(() -> memoDao.insert(memoRoom));
    }

    public LiveData<List<MemoRoom>> getAll(){
        return memoDao.getAll();
    }

    public LiveData<List<MemoRoom>> getDayList(String dayTxt){
        return memoDao.getDayList(dayTxt);
    }

    public void delete(String key){
        executorService.execute(() -> memoDao.delete(key));}
}
