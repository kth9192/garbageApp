package com.taehoon.garbagealarm.view.adapter;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.RecyclerAlarmBinding;
import com.taehoon.garbagealarm.repository.alarmrepository.AlarmRoom;
import com.taehoon.garbagealarm.view.handler.ItemTouchHelperListener;
import com.taehoon.garbagealarm.view.helper.AlarmHelper;
import com.taehoon.garbagealarm.view.helper.RecyclerHelper;
import com.taehoon.garbagealarm.viewmodel.AlarmObserver;
import com.taehoon.garbagealarm.viewmodel.AlarmViewModel;
import com.taehoon.garbagealarm.viewmodel.MemoRoomViewModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmViewAdapter extends ListAdapter<AlarmRoom, RecyclerView.ViewHolder> implements ItemTouchHelperListener {

    private static String TAG = AlarmViewAdapter.class.getSimpleName();
    private AlarmViewModel alarmViewModel;
    private MemoRoomViewModel memoRoomViewModel;
    private RecyclerHelper viewHelper;
    private AlarmHelper alarmHelper;

    public AlarmViewAdapter(AlarmViewModel alarmViewModel, MemoRoomViewModel memoRoomViewModel,
                            RecyclerHelper recyclerHelper, AlarmHelper alarmHelper) {
        super(DIFF_CALLBACK);
        this.alarmViewModel = alarmViewModel;
        this.memoRoomViewModel = memoRoomViewModel;
        viewHelper = recyclerHelper;
        this.alarmHelper = alarmHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_alarm, parent, false);
        return new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AlarmViewHolder){

            ((AlarmViewHolder) holder).getRecyclerAlarmBinding().setAlarmCard(new AlarmObserver(getItem(position)));
            if (getItemCount() > 0) {
                ((AlarmViewHolder) holder).getRecyclerAlarmBinding().dayBar.setBackgroundColor(Color.parseColor(viewHelper.getColorFromDay(getItem(position).getDaylist().get(0))));
            }
                TextView[] tmpTxt = new TextView[]{((AlarmViewHolder) holder).getRecyclerAlarmBinding().daylist1, ((AlarmViewHolder) holder).getRecyclerAlarmBinding().daylist2,
                        ((AlarmViewHolder) holder).getRecyclerAlarmBinding().daylist3, ((AlarmViewHolder) holder).getRecyclerAlarmBinding().daylist4,
                        ((AlarmViewHolder) holder).getRecyclerAlarmBinding().daylist5, ((AlarmViewHolder) holder).getRecyclerAlarmBinding().daylist6,
                        ((AlarmViewHolder) holder).getRecyclerAlarmBinding().daylist7};

            for (int i = 0; i< tmpTxt.length; i++){
                Log.d(TAG, "요일체크" + tmpTxt[i].getText().toString());
                if (getItem(position).getDaylist().contains(tmpTxt[i].getText().toString()) && getItemCount() > 0){

                    tmpTxt[i].setTextColor(Color.parseColor("#000000"));
                }
            }
        }
    }

    private static final DiffUtil.ItemCallback<AlarmRoom> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<AlarmRoom>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull AlarmRoom oldModel, @NonNull AlarmRoom newModel) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return (oldModel.getId().equals(newModel.getId()));
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull AlarmRoom oldModel, @NonNull AlarmRoom newModel) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldModel.equals(newModel);
                }
            };

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemRemove(int position) {
        memoRoomViewModel.delete(getItem(position).getTag());
        alarmViewModel.delete(getItem(position).getTag());
        alarmHelper.getAlarmLogic().unregisterAlarm(getItem(position).getTag().hashCode());
    }


    static class AlarmViewHolder extends RecyclerView.ViewHolder {

        private RecyclerAlarmBinding recyclerAlarmBinding;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerAlarmBinding = DataBindingUtil.bind(itemView);
            recyclerAlarmBinding.executePendingBindings();
        }

        public RecyclerAlarmBinding getRecyclerAlarmBinding() {
            return recyclerAlarmBinding;
        }

    }
}
