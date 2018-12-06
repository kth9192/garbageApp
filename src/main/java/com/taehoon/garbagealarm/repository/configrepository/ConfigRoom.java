package com.taehoon.garbagealarm.repository.configrepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ConfigRoom {

    @PrimaryKey
    @NonNull
    private String user;
    private boolean alarm_newbie;
    private boolean delete_newbie;
    private boolean memo_newbie;

    public ConfigRoom(String user, boolean alarm_newbie, boolean delete_newbie, boolean memo_newbie) {
        this.user = user;
        this.alarm_newbie = alarm_newbie;
        this.delete_newbie = delete_newbie;
        this.memo_newbie = memo_newbie;
    }


    public String getUser() {
        return user;
    }

    public boolean isAlarm_newbie() {
        return alarm_newbie;
    }

    public boolean isDelete_newbie() {
        return delete_newbie;
    }

    public boolean isMemo_newbie() {
        return memo_newbie;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAlarm_newbie(boolean alarm_newbie) {
        this.alarm_newbie = alarm_newbie;
    }

    public void setDelete_newbie(boolean delete_newbie) {
        this.delete_newbie = delete_newbie;
    }

    public void setMemo_newbie(boolean memo_newbie) {
        this.memo_newbie = memo_newbie;
    }
}
