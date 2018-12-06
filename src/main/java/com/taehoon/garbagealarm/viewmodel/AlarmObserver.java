package com.taehoon.garbagealarm.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;


public class AlarmObserver extends BaseObservable {

    private static String TAG = AlarmObserver.class.getSimpleName();
    private AlarmRoom alarmRoom;

    public AlarmObserver(AlarmRoom alarmRoom) {
        this.alarmRoom = alarmRoom;
    }

    @Bindable
    public String getDay(){
        return alarmRoom.getDaylist().get(0);
    }

    public String getTime(){
        return alarmRoom.getTime();
    }

    @Bindable
    public String getMemo(){
        return alarmRoom.getMemo();
    }
}
