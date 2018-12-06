package com.taehoon.garbagealarm.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.transition.Explode;
import android.util.Log;
import android.view.Window;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.ActivityMemoBinding;
import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;
import com.taehoon.garbagealarm.view.adapter.MemoAdapter;
import com.taehoon.garbagealarm.view.helper.RecyclerHelper;
import com.taehoon.garbagealarm.viewmodel.AlarmViewModel;
import com.taehoon.garbagealarm.viewmodel.MemoLogic;
import com.taehoon.garbagealarm.viewmodel.MemoRoomViewModel;

import java.util.Calendar;
import java.util.List;

/**
 * Created by kth919 on 2017-11-07.
 */

public class MemoActivity extends AppCompatActivity{

    private static String TAG = MemoActivity.class.getName();
    private int position;
    private ActivityMemoBinding activityMemoBinding;
    private MemoLogic memoLogic;
    private MemoRoomViewModel memoRoomViewModel;
    private AlarmViewModel alarmViewModel;

    private RecyclerHelper recyclerHelper;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        getWindow().setAllowEnterTransitionOverlap(true);

        activityMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_memo);

        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        memoRoomViewModel = ViewModelProviders.of(this).get(MemoRoomViewModel.class);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, position+1);

        memoLogic = new MemoLogic(getApplicationContext());
        recyclerHelper = new RecyclerHelper(getApplicationContext(), memoLogic);

        MemoAdapter memoAdapter = new MemoAdapter(calendar.get(Calendar.DAY_OF_WEEK), recyclerHelper);
        activityMemoBinding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activityMemoBinding.recycler.setAdapter(memoAdapter);

        memoRoomViewModel.getListLiveData().observe(this, new Observer<List<MemoRoom>>() {

            @Override
            public void onChanged(List<MemoRoom> memoRooms) {

                Log.d(TAG, "요일" + recyclerHelper.getDayFromInt(position+1));
                memoRoomViewModel.getDayLiveData(recyclerHelper.getDayFromInt(position+1)).observe(MemoActivity.this, new Observer<List<MemoRoom>>() {
                    @Override
                    public void onChanged(List<MemoRoom> memoRooms) {
                        memoAdapter.submitList(memoRooms);
                        Log.d(TAG, "사이즈" + memoRooms.size());

                        if (memoRooms.size() == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
                            builder.setMessage("해당 요일에 메모가 없습니다.")
                                    .setTitle("알림");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    finish();
                                }
                            });
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        });


    }

}
