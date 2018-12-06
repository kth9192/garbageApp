package com.taehoon.garbagealarm.viewmodel;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.model.day.DayDataDomain;
import com.taehoon.garbagealarm.model.day.DayModel;
import com.taehoon.garbagealarm.model.day.TodayModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kth919 on 2017-06-02.
 */

public class MemoLogic {

    private static String TAG = MemoLogic.class.getName();
    private Context context;

    private ArrayList<DayModel> dayModelList = new ArrayList<>();

    public MemoLogic(Context context) {
        this.context = context;
        insertDayList();
    }

    public void insertDayList() {
        Gson gson = new Gson();

        try {
            AssetManager am = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(am.open("sample.json")));
            DayDataDomain dayDataDomain = gson.fromJson(bf , DayDataDomain.class);
            dayModelList = dayDataDomain.getData();
        } catch (IOException e) {
            Log.e(TAG , String.valueOf(e));
        }
    }

    public int getDayNum() {

        int dayIs = 0;
        Calendar calendar = Calendar.getInstance();
        int whatDay = calendar.get(Calendar.DAY_OF_WEEK);

        switch (whatDay) {
            case 1:
                dayIs = 0;
                break;
            case 2:
                dayIs = 1;
                break;
            case 3:
                dayIs = 2;
                break;
            case 4:
                dayIs = 3;
                break;
            case 5:
                dayIs = 4;
                break;
            case 6:
                dayIs = 5;
                break;
            case 7:
                dayIs = 6;
                break;
        }
        return dayIs;
    }

    public TodayModel getTodayModel(){

        Calendar oCalendar = Calendar.getInstance( );

        final String[] week = { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };

//        System.out.println("현재 요일: " + week[oCalendar.get(Calendar.DAY_OF_WEEK) - 1]);
        return new TodayModel(week[oCalendar.get(Calendar.DAY_OF_WEEK) - 1]
                , getDayList().get(oCalendar.get(Calendar.DAY_OF_WEEK) - 1).getComment() , todayCover(week[oCalendar.get(Calendar.DAY_OF_WEEK) - 1])); //R.drawable.garbagecover2
    }

    public int todayCover(String day){

        if (day.equals("일요일") || day.equals("월요일")
                || day.equals("금요일")){
            return R.drawable.alarm_plastic;
        }else if (day.equals("화요일") || day.equals("목요일")){
            return R.drawable.alarm_bag;
        }else if (day.equals("수요일")){
            return R.drawable.alarm_can;
        }else {
            return R.drawable.alarm_paper;
        }
    }

    public ArrayList<DayModel> getDayList(){
        return dayModelList;
    }

}
