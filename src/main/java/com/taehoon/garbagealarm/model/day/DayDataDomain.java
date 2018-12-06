package com.taehoon.garbagealarm.model.day;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kth919 on 2017-06-05.
 */

public class DayDataDomain {

    @SerializedName("data")
    private ArrayList<DayModel> data;

    public ArrayList<DayModel> getData() {
        return data;
    }

}
