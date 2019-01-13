package com.taehoon.garbagealarm.viewmodel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.taehoon.garbagealarm.alarmhelper.AlarmReceiver;
import com.taehoon.garbagealarm.model.day.DayModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kth919 on 2017-12-06.
 */

public class AlarmLogic {

    private static String TAG = AlarmLogic.class.getName();
    private Context context;
    private static AlarmManager am;
    private static PendingIntent sender;
    private static Intent alarmIntent;

    public AlarmLogic(Context context) {
        this.context = context;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private void setSender(int id, Intent intent) {
        sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void newAlarm(ArrayList<Integer> enableDays, int id, long setttingTime, String memo) {

        alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("daylist", enableDays);
        alarmIntent.putExtra("memo", memo);

        setSender(id, alarmIntent);

        if (System.currentTimeMillis() < setttingTime) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setttingTime,
                    AlarmManager.INTERVAL_DAY, sender);
        } else {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setttingTime + AlarmManager.INTERVAL_DAY,
                    AlarmManager.INTERVAL_DAY, sender);
        }
    }

    public void unregisterAlarm(int id) {

        setSender(id, alarmIntent);
        am.cancel(sender);
        sender.cancel();
        am = null;
        sender = null;
    }

    public int currentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public ArrayList<Integer> convertDayOfWeek(ArrayList<String> arr) {

        ArrayList<Integer> answer = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            answer.add(dayConvertToInt(arr.get(i)));
        }
        return answer;
    }

    public int dayConvertToInt(String s) {

        int answer = 0;
        switch (s) {
            case "일":
                answer = 1;
                break;
            case "월":
                answer = 2;
                break;
            case "화":
                answer = 3;
                break;
            case "수":
                answer = 4;
                break;
            case "목":
                answer = 5;
                break;
            case "금":
                answer = 6;
                break;
            case "토":
                answer = 7;
                break;
        }
        return answer;
    }

    public long convertAlarmTime(int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTimeInMillis();
    }

    public String getContent(ArrayList<DayModel> dayModelList) {

        String content = null;

        for (int i = 0; i < dayModelList.size(); i++) {
            if (dayModelList.get(i).getDay().equals(setNotifyText())) {
//                Log.d(TAG , "json 내장 : " + dayModelList.get(i).getDay() + " 오늘 : " + NotifyTextLogic() );
                content = dayModelList.get(i).getComment();
            }
        }
        return content;
    }

    public String setNotifyText() {

        String dayIs = null;
        Calendar calendar = Calendar.getInstance();
        int whatDay = calendar.get(Calendar.DAY_OF_WEEK);

        switch (whatDay) {
            case 1:
                dayIs = "일요일";
                break;
            case 2:
                dayIs = "월요일";
                break;
            case 3:
                dayIs = "화요일";
                break;
            case 4:
                dayIs = "수요일";
                break;
            case 5:
                dayIs = "목요일";
                break;
            case 6:
                dayIs = "금요일";
                break;
            case 7:
                dayIs = "토요일";
                break;
        }
        return dayIs;
    }

}
