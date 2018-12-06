package com.taehoon.garbagealarm.viewmodel;

import android.app.Application;

import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRepository;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;
import com.taehoon.garbagealarm.repository.memorepository.MemoRepository;
import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class AlarmViewModel extends AndroidViewModel {

    private AlarmRepository alarmRoomRepo;
    private LiveData<List<AlarmRoom>> listLiveData;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        this.alarmRoomRepo = new AlarmRepository(application);
    }

    public LiveData<List<AlarmRoom>> getListLiveData() {

        if (listLiveData == null){
            listLiveData = alarmRoomRepo.getAll();
        }

        return listLiveData;
    }

    public void insert(AlarmRoom alarmRoom) { alarmRoomRepo.insert(alarmRoom); }

    public void delete(String key){alarmRoomRepo.delete(key);}
}
