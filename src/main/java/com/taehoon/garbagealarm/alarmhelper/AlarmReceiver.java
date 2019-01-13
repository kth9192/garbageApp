package com.taehoon.garbagealarm.alarmhelper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.util.Log;

import com.taehoon.garbagealarm.MainActivity;
import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.viewmodel.AlarmLogic;
import com.taehoon.garbagealarm.viewmodel.MemoLogic;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kth919 on 2017-06-07.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static String TAG = AlarmReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        String id = UUID.randomUUID().toString();

        AlarmLogic alarmLogic = new AlarmLogic(context);
        MemoLogic memoLogic = new MemoLogic(context);

        String comment = alarmLogic.getContent(memoLogic.getDayList());
        String memo = intent.getStringExtra("memo");
        ArrayList<Integer> days = intent.getIntegerArrayListExtra("daylist");

        for (int i = 0; i < days.size(); i++) {
            Log.d(TAG, "요일 확인" + days.get(i));
        }

        Log.d(TAG, String.valueOf(alarmLogic.currentDayOfWeek()));

        NotificationManager notificationManager = null;

        if (days.contains(alarmLogic.currentDayOfWeek())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "test";
                String description = "알람입니다";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel channel = new NotificationChannel(id, name, importance);
                channel.setDescription(description);
                channel.enableVibration(true);

                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                notificationManager = context.getSystemService(NotificationManager.class);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }else {
                notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            Intent tmpIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(tmpIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, id)
                            .setSmallIcon(R.drawable.ic_info_white_24dp)
                            .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                            .setContentTitle("오늘 분리수거")
                            .setContentText(comment)
                            .setSound(RingtoneManager
                                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(resultPendingIntent);

            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context, id);
            builder2.setSmallIcon(R.drawable.ic_info_white_24dp)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setContentTitle("메모를 확인하려면 드래그하세요")
                    .setSmallIcon(R.drawable.ic_info_white_24dp)
                    .setContentText(memo)
                    .setTicker("알림")
                    .setContentIntent(resultPendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("할일 알림")
                            .bigText(memo));

            notificationManager.notify(1, mBuilder.build());
            notificationManager.notify(2, builder2.build());

        }
    }
}
