package com.taehoon.garbagealarm.view.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.RecyclerDayitemBinding;
import com.taehoon.garbagealarm.viewmodel.MemoLogic;
import com.taehoon.garbagealarm.viewmodel.MemoObserver;

/**
 * Created by kth919 on 2017-06-02.
 */

public class GarbageAdapter extends RecyclerView.Adapter<GarbageAdapter.BindingHolder> {

    public static String TAG = GarbageAdapter.class.getName();

    private Activity activity;
    private Context mContext;

    public GarbageAdapter(Activity activity, Context context) {
        this.activity = activity;
        mContext = context;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dayitem, parent, false);
        return new BindingHolder(v);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {

        MemoLogic memoLogic = new MemoLogic(mContext);

        MemoObserver memoObserver = new MemoObserver(activity, memoLogic.getDayList().get(position), position);

        holder.getItemRecyclerBinding().setDayitem(memoObserver);
        holder.getItemRecyclerBinding().executePendingBindings();

        if (position == memoLogic.getDayNum()) {

            holder.getItemRecyclerBinding().itemTitle.setTextSize(14);
            holder.getItemRecyclerBinding().itemTitle.setText("오늘은 \"" + memoLogic.getDayList().get(position).getDay() + "\"");
            holder.getItemRecyclerBinding().itemTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.getItemRecyclerBinding().itemComment.setTextSize(13);
            holder.getItemRecyclerBinding().itemComment.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class BindingHolder extends RecyclerView.ViewHolder {

        private RecyclerDayitemBinding itemRecyclerBinding;

        BindingHolder(View itemView) {
            super(itemView);
            itemRecyclerBinding = DataBindingUtil.bind(itemView);
            itemRecyclerBinding.itemTitle.setPaintFlags(itemRecyclerBinding.itemTitle.getPaintFlags() |
                    Paint.FAKE_BOLD_TEXT_FLAG);

        }

        RecyclerDayitemBinding getItemRecyclerBinding() {return itemRecyclerBinding;}
    }
}
