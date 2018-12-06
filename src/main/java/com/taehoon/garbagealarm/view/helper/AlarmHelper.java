package com.taehoon.garbagealarm.view.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TimePicker;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.DialogMemoBinding;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;
import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;
import com.taehoon.garbagealarm.viewmodel.AlarmLogic;
import com.taehoon.garbagealarm.viewmodel.AlarmViewModel;
import com.taehoon.garbagealarm.viewmodel.MemoRoomViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by kth919 on 2018-01-18.
 */

public class AlarmHelper {

    private static String TAG = AlarmHelper.class.getName();
    
    private Activity activity;
    private Context context;

    private DialogMemoBinding dialogMemoBinding;
    private AlertDialog.Builder builder = null;
    private AlertDialog dialog;

    private ArrayList<String> checkDay = new ArrayList<>();

    public AlarmLogic getAlarmLogic() {
        return alarmLogic;
    }

    private AlarmLogic alarmLogic;
    private Button[] source;

    public AlarmHelper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        alarmLogic = new AlarmLogic(context);
    }

    public void showDialog(){

        createDayBtn();

        keyBoardOpen();
        if (builder == null) {
            createBuilderForAlarm();
            dialog = builder.create();  // 알림창 객체 생성
        }
        dialog.show();
    }

    private void createDayBtn() {

        if (dialogMemoBinding == null) {
            dialogMemoBinding = DataBindingUtil.inflate(LayoutInflater.from(new ContextThemeWrapper(activity, R.style.myDialog)), R.layout.dialog_memo, null, false);
            source = new Button[]{dialogMemoBinding.dayBtn1, dialogMemoBinding.dayBtn2, dialogMemoBinding.dayBtn3,
                    dialogMemoBinding.dayBtn4, dialogMemoBinding.dayBtn5, dialogMemoBinding.dayBtn6, dialogMemoBinding.dayBtn7};
            for (int i = 0; i<source.length; i++){
                source[i].setPaintFlags(source[i].getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
            }
        }

        for (int i = 0; i<source.length; i++) {

            final int tmp = i;

            source[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (source[tmp].getCurrentTextColor() == context.getResources().getColor(R.color.unclickedTxt)) {
                        source[tmp].setTextColor(ContextCompat.getColor(context, R.color.defaultColor));
                        source[tmp].setBackgroundResource(R.drawable.rounded_btn);
                        checkDay.add(source[tmp].getText().toString());
                    } else {
                        source[tmp].setTextColor(ContextCompat.getColor(context, R.color.unclickedTxt));
                        source[tmp].setBackgroundResource(R.drawable.rounded_btn_unclick);
                        checkDay.remove(source[tmp].getText().toString());
                    }
                }
            });
        }

        dialogMemoBinding.timer.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "알람 시 : " + hourOfDay + "분 : " + minute);
            }
        });
    }

    private void createBuilderForAlarm(){
        builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.myDialog));

        builder.setTitle("알림 설정")
                .setView(dialogMemoBinding.getRoot())
                .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        positiveClicked();
                        turnOverday();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        negativeClicked();
                        turnOverday();
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                turnOverday();
            }
        });
    }

    private void positiveClicked(){

        if (checkDay.size() != 0){

            String KEY = String.valueOf(Calendar.getInstance().getTimeInMillis());
            Log.d(TAG, "알람 tag" + KEY.hashCode());

            AlarmViewModel alarmViewModel = ViewModelProviders.of((FragmentActivity) activity).get(AlarmViewModel.class);
            alarmViewModel.insert(new AlarmRoom(UUID.randomUUID().toString(), checkDay, dialogMemoBinding.editMemo.getText().toString(),
                    dialogMemoBinding.timer.getHour() + ":" + dialogMemoBinding.timer.getMinute(), KEY));

            MemoRoomViewModel memoRoomViewModel = ViewModelProviders.of((FragmentActivity) activity).get(MemoRoomViewModel.class);

            for (int i = 0; i<checkDay.size(); i++) {
                memoRoomViewModel.insert(new MemoRoom(UUID.randomUUID().toString(), checkDay.get(i), dialogMemoBinding.editMemo.getText().toString(),
                        dialogMemoBinding.timer.getHour() + ":" + dialogMemoBinding.timer.getMinute(), KEY));
                Log.d(TAG, "알람추가" + checkDay.get(i));
            }

            alarmLogic.newAlarm(alarmLogic.convertDayOfWeek(checkDay), KEY.hashCode(),
                    alarmLogic.convertAlarmTime(dialogMemoBinding.timer.getHour(), dialogMemoBinding.timer.getMinute())
            , dialogMemoBinding.editMemo.getText().toString());

        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.myDialog));
            builder.setMessage("설정된 요일이 없습니다.")
                    .setTitle("알림");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.dismiss();
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });

            AlertDialog innerdialog = builder.create();
            innerdialog.show();
        }

        keyBoardClose();
        dialogMemoBinding.editMemo.setText("");
        dialog.cancel();
    }

    private void negativeClicked() {
        keyBoardClose();
        dialog.cancel();
    }

    private void turnOverday(){
        for (int i = 0; i<source.length; i++){
            source[i].setTextColor(ContextCompat.getColor(context, R.color.unclickedTxt));
            source[i].setBackgroundResource(R.drawable.rounded_btn_unclick);
        }
    }

    private void keyBoardOpen(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void keyBoardClose(){
        InputMethodManager immhide = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

}
