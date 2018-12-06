package com.taehoon.garbagealarm.view.adapter;

import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.RecyclerMemoBinding;
import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;
import com.taehoon.garbagealarm.viewmodel.DetailMemoObserver;
import com.taehoon.garbagealarm.view.helper.RecyclerHelper;

/**
 * Created by kth919 on 2017-10-17.
 */

public class MemoAdapter extends ListAdapter<MemoRoom, RecyclerView.ViewHolder> {

    private static String TAG = MemoAdapter.class.getName();
    private String dayTag;
    private RecyclerHelper recyclerHelper;

    public MemoAdapter(int dayTag, RecyclerHelper recyclerHelper) {
        super(DIFF_CALLBACK);
        this.dayTag = recyclerHelper.getDayFromInt(dayTag);
        this.recyclerHelper = recyclerHelper;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_memo, parent,  false);
        return new BindingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BindingHolder){

            ((BindingHolder) holder).getMemoBinding().setAlarmCard(new DetailMemoObserver(getItem(position)));
            ((BindingHolder) holder).getMemoBinding().executePendingBindings();
        }
    }

    private static final DiffUtil.ItemCallback<MemoRoom> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MemoRoom>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull MemoRoom oldModel, @NonNull MemoRoom newModel) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return (oldModel.getId() == newModel.getId());
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull MemoRoom oldModel, @NonNull MemoRoom newModel) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldModel.equals(newModel);
                }
            };


    public class BindingHolder extends RecyclerView.ViewHolder {

        private RecyclerMemoBinding recyclerMemoBinding;

        public BindingHolder(View itemView) {
            super(itemView);
            recyclerMemoBinding = DataBindingUtil.bind(itemView);

            GradientDrawable bgShape = (GradientDrawable) recyclerMemoBinding.alarmImg.getBackground().getCurrent();
            bgShape.setColor(Color.parseColor(recyclerHelper.getColorFromDay(dayTag)));
            recyclerMemoBinding.alarmImg.setText(dayTag);
        }

        public RecyclerMemoBinding getMemoBinding(){ return recyclerMemoBinding;}

//        public void modify(int position, ImprovedMemoModel model) {
//            mMemoModels.set(position, model);
//            notifyItemChanged(position);
//        }
    }
}