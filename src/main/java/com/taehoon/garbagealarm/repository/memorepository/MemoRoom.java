package com.taehoon.garbagealarm.repository.memorepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MemoRoom {

    @PrimaryKey
    @NonNull
    private String id;
    private String dayTxt;
    private String memo;
    private String time;
    private String tag;

    public MemoRoom(String id, String dayTxt, String memo, String time, String tag) {
        this.id = id;
        this.dayTxt = dayTxt;
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

    public String getDayTxt() {
        return dayTxt;
    }

    public void setDayTxt(String dayTxt) {
        this.dayTxt = dayTxt;
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
