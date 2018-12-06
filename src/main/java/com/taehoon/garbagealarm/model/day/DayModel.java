package com.taehoon.garbagealarm.model.day;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kth919 on 2017-06-02.
 */

public class DayModel extends BaseObservable {

    @SerializedName("img")
    String img;
    @SerializedName("day")
    String day;
    @SerializedName("comment")
    String comment;

    public DayModel(String img, String day , String comment){
        this.img = img;
        this.day = day;
        this.comment = comment;
    }

    @Bindable
    public String getImg() {
        return img;
    }

    @Bindable
    public String getDay() {
        return day;
    }

    @Bindable
    public String getComment() {
        return comment;
    }

}
