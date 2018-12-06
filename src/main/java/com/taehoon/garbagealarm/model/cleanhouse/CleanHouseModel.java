package com.taehoon.garbagealarm.model.cleanhouse;

/**
 * Created by kth919 on 2017-10-27.
 */

public class CleanHouseModel {

    private String addr;
    private String dong;
    private String location;

    private Double mapX;
    private Double mapY;

    public CleanHouseModel(String addr, String dong , String location,  Double mapX, Double mapY) {
        this.addr = addr;
        this.dong = dong;
        this.location = location;
        this.mapX = mapX;
        this.mapY = mapY;
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
