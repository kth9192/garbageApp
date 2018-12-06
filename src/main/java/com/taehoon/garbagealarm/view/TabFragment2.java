package com.taehoon.garbagealarm.view;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.TabFragment2Binding;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;
import com.taehoon.garbagealarm.view.adapter.AlarmViewAdapter;
import com.taehoon.garbagealarm.view.handler.ItemTouchHelperCallback;
import com.taehoon.garbagealarm.view.helper.AlarmHelper;
import com.taehoon.garbagealarm.view.helper.RecyclerHelper;
import com.taehoon.garbagealarm.viewmodel.AlarmLogic;
import com.taehoon.garbagealarm.viewmodel.AlarmViewModel;
import com.taehoon.garbagealarm.viewmodel.MemoLogic;
import com.taehoon.garbagealarm.viewmodel.MemoRoomViewModel;

import java.util.List;

/**
 * Created by kth919 on 2017-05-07.
 */

public class TabFragment2 extends Fragment {

    private static String TAG = TabFragment2.class.getName();
    private TabFragment2Binding tab2_Binding;

    private AlarmLogic alarmLogic;
    private AlarmViewAdapter alarmViewAdapter;

    private MemoLogic memoLogic;
    private RecyclerHelper viewHelper;

    private SnapHelper snapHelper;
    private AlarmHelper alarmHelper;
    private boolean onResumed = false;
    private AlarmViewModel alarmViewModel;
    private MemoRoomViewModel memoRoomViewModel;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        Log.d(TAG, "뷰새로고침 호출");

//        if (isVisibleToUser && alarmViewAdapter.getItemCount() == 0 && onResumed) {
//            new SpotlightView.Builder(getActivity())
//                    .introAnimationDuration(250)
//                    .enableRevealAnimation(true)
//                    .performClick(true)
//                    .fadeinTextDuration(250)
//                    .headingTvColor(Color.parseColor("#eb273f"))
//                    .headingTvSize(32)
//                    .headingTvText("알람 추가")
//                    .subHeadingTvColor(Color.parseColor("#ffffff"))
//                    .subHeadingTvSize(16)
//                    .subHeadingTvText("버튼을 누르면 알람 설정 알림창을 띄웁니다.")
//                    .maskColor(Color.parseColor("#dc000000"))
//                    .target(getActivity().findViewById(R.id.addAlarm))
//                    .lineAnimDuration(250)
//                    .lineAndArcColor(Color.parseColor("#eb273f"))
//                    .dismissOnTouch(true)
//                    .dismissOnBackPress(true)
//                    .enableDismissAfterShown(true)
//                    .usageId("ADDALARM") //UNIQUE ID
//                    .show();
//
//        }

        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        memoLogic = new MemoLogic(getContext());
        viewHelper = new RecyclerHelper(getContext(), memoLogic);
        alarmLogic = viewHelper.getAlarmLogic();

        snapHelper = new PagerSnapHelper();
        alarmHelper = new AlarmHelper(getActivity(), getContext());

        tab2_Binding = DataBindingUtil.inflate(inflater, R.layout.tab_fragment_2, container , false);
        tab2_Binding.setModel(memoLogic.getTodayModel());
        tab2_Binding.setLifecycleOwner(TabFragment2.this);

        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        memoRoomViewModel = ViewModelProviders.of(this).get(MemoRoomViewModel.class);

//        tab2_Binding.recycler.addItemDecoration(new VerticalOffsetDecoration(getActivity()));
        alarmViewAdapter = new AlarmViewAdapter(alarmViewModel, memoRoomViewModel, viewHelper, alarmHelper);
        tab2_Binding.recycler.setAdapter(alarmViewAdapter);
        tab2_Binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        alarmViewModel.getListLiveData().observe(this, new Observer<List<AlarmRoom>>() {
            @Override
            public void onChanged(List<AlarmRoom> alarmRooms) {
                alarmViewAdapter.submitList(alarmRooms);
                if (alarmRooms.size() == 0) {
                    tab2_Binding.emptyTxt.setVisibility(View.VISIBLE);
                }else {
                    tab2_Binding.emptyTxt.setVisibility(View.INVISIBLE);
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(alarmViewAdapter));
        itemTouchHelper.attachToRecyclerView(tab2_Binding.recycler);


        return tab2_Binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(snapHelper != null) {
            snapHelper.attachToRecyclerView(tab2_Binding.recycler);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        onResumed = true;
    }

    private class VerticalOffsetDecoration extends RecyclerView.ItemDecoration {

        private Activity context;

        public VerticalOffsetDecoration(Activity activity) {
            this.context = activity;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int total = parent.getAdapter().getItemCount();

            if (position != 0 && position != total - 1) {
                return;
            }

            Display display = context.getWindowManager().getDefaultDisplay();
            Point displaySize = new Point();
            display.getSize(displaySize);
            int displayWidth = displaySize.x;
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            float viewWidth = params.width;

            int offset = (int)(displayWidth - viewWidth) / 2 ;

            if (position == 0) {
                outRect.left = offset - params.getMarginStart();
            }else if (position == total - 1) {
                outRect.right = offset - params.getMarginEnd();
            }
        }
    }
}