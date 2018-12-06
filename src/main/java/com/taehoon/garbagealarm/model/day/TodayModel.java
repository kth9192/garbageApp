package com.taehoon.garbagealarm.model.day;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by kth919 on 2017-12-26.
 */

public class TodayModel extends BaseObservable {

    private String day;
    private String comment;
    private int image;

    public TodayModel(String day, String comment, int image) {
        this.day = day;
        this.comment = comment;
        this.image = image;
    }

    @Bindable
    public String getDay() {
        return day;
    }

    @Bindable
    public String getComment() {
        return comment;
    }

    @Bindable
    public int getImage() {
        return image;
    }

}
