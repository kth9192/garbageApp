package com.taehoon.garbagealarm.repository.addrrepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AddrRoom {

    @PrimaryKey
    @NonNull
    private String id;

    private String addr;
    private String dong;
    private String location;

    private Double mapX;
    private Double mapY;

    public AddrRoom(String id, String addr, String dong, String location, Double mapX, Double mapY) {
        this.id = id;
        this.addr = addr;
        this.dong = dong;
        this.location = location;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public String getDong() {
        return dong;
    }

    public String getLocation() {
        return location;
    }

    public Double getMapX() {
        return mapX;
    }

    public Double getMapY() {
        return mapY;
    }


}
