package com.taehoon.garbagealarm.model.cleanhouse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kth919 on 2017-11-04.
 */

public class ItemModel {

    @SerializedName("address")
    private String addr;

    @SerializedName("addrdetail")
    private AddrDetail addrDetail;

    @SerializedName("isRoadAddress")
    private boolean isRoadAddress;

    @SerializedName("point")
    private MapPoint point;

    public String getAddr() {
        return addr;
    }

    public AddrDetail getAddrDetail() {
        return addrDetail;
    }

    public boolean isRoadAddress() {
        return isRoadAddress;
    }

    public MapPoint getPoint() {
        return point;
    }

    public static class AddrDetail{

        @SerializedName("country")
        private String country;

        @SerializedName("sido")
        private String sido;

        @SerializedName("sigugun")
        private String sigugun;

        @SerializedName("dongmyun")
        private String dongmyun;

        @SerializedName("ri")
        private String ri;

        @SerializedName("rest")
        private String rest;

        public String getCountry() {
            return country;
        }

        public String getSido() {
            return sido;
        }

        public String getSigugun() {
            return sigugun;
        }

        public String getDongmyun() {
            return dongmyun;
        }

        public String getRi() {
            return ri;
        }

        public String getRest() {
            return rest;
        }
    }

    public static class MapPoint{
        @SerializedName("x")
        private double mapX;
        @SerializedName("y")
        private double mapY;

        public double getMapX() {
            return mapX;
        }

        public double getMapY() {
            return mapY;
        }
    }
}
