package com.taehoon.garbagealarm.repository.alarmrepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlarmRoom {

    @PrimaryKey
    @NonNull
    private String id;
    private ArrayList<String> daylist;
    private String memo;
    private String time;
    private String tag;

    public AlarmRoom(String id, ArrayList<String> daylist, String memo, String time, String tag) {
        this.id = id;
        this.daylist = daylist;
        this.memo = memo;
        this.time = time;
        this.tag = tag;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getDaylist() {
        return daylist;
    }

    public void setDaylist(ArrayList<String> daylist) {
        this.daylist = daylist;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
