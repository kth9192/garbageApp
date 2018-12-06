package com.taehoon.garbagealarm.viewmodel;

import android.app.Application;

import com.taehoon.garbagealarm.repository.memorepository.MemoRepository;
import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MemoRoomViewModel extends AndroidViewModel {

    private MemoRepository alarmRoomRepo;
    private LiveData<List<MemoRoom>> listLiveData;

    public MemoRoomViewModel(@NonNull Application application) {
        super(application);
        this.alarmRoomRepo = new MemoRepository(application);
    }

    public LiveData<List<MemoRoom>> getListLiveData() {

        if (listLiveData == null){
            listLiveData = alarmRoomRepo.getAll();
        }

        return listLiveData;
    }

    public LiveData<List<MemoRoom>> getDayLiveData(String dayTxt){

        return alarmRoomRepo.getDayList(dayTxt);
    }

    public void insert(MemoRoom alarmRoom) { alarmRoomRepo.insert(alarmRoom); }

    public void delete(String key){alarmRoomRepo.delete(key);}

}
