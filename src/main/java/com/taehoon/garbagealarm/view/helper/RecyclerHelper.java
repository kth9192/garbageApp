package com.taehoon.garbagealarm.view.helper;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.taehoon.garbagealarm.viewmodel.AlarmLogic;
import com.taehoon.garbagealarm.viewmodel.MemoLogic;

/**
 * Created by kth919 on 2017-12-17.
 */

public class RecyclerHelper {

    private static String TAG = RecyclerHelper.class.getName();

    private Context mContext;

    private RecyclerView recyclerView;
    private TextView textView;

    private MemoLogic memoLogic;
    private AlarmLogic alarmLogic;

    public RecyclerHelper(Context mContext, MemoLogic memoLogic) {
        this.mContext = mContext;
        alarmLogic = new AlarmLogic(mContext);
        this.memoLogic = memoLogic;
    }

    public int dayConvertToInt(String s){
       return alarmLogic.dayConvertToInt(s)-1;
    }

    public String getColorFromDay(String s){

        String answer = "";

        switch (s){
            case "일" :
                answer = "#E500E5";
                break;
            case "월" :
                answer = "#FF0D0D";
                break;
            case "화" :
                answer = "#FF8C00";
                break;
            case "수" :
                answer = "#FFFF36";
                break;
            case "목" :
                answer = "#36EBE0";
                break;
            case "금" :
                answer = "#4C9AD1";
                break;
            case "토" :
                answer = "#BAFF43";
                break;
        }
        return answer;
    }

    public String getDayFromInt(int s){

        String answer = "";

        switch(s){
            case 1:
                answer = "일";
                break ;
            case 2:
                answer = "월";
                break ;
            case 3:
                answer = "화";
                break ;
            case 4:
                answer = "수";
                break ;
            case 5:
                answer = "목";
                break ;
            case 6:
                answer = "금";
                break ;
            case 7:
                answer = "토";
                break ;
        }

        return answer;
    }

    public AlarmLogic getAlarmLogic() {
        return alarmLogic;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
