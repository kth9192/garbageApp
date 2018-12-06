package com.taehoon.garbagealarm.viewmodel;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;

import androidx.databinding.BaseObservable;

import android.view.View;

import com.taehoon.garbagealarm.model.day.DayModel;
import com.taehoon.garbagealarm.view.MemoActivity;

/**
 * Created by kth919 on 2018-02-08.
 */

public class MemoObserver extends BaseObservable {

    private static String TAG = MemoObserver.class.getName();
    private Activity activity;
    private DayModel dayModel;
    private int position;

    public MemoObserver(Activity activity, DayModel dayModel, int position) {
        this.activity = activity;
        this.dayModel = dayModel;
        this.position = position;
    }

    public View.OnClickListener onClickListenerForIntent(){

        return  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MemoActivity.class);
                intent.putExtra("position", position);

                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            }
        };
    }

    public String getImg(){return  dayModel.getImg();}

    public String getDay(){
        return dayModel.getDay();
    }

    public String getComment(){
        return dayModel.getComment();
    }
}
