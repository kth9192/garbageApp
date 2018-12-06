package com.taehoon.garbagealarm.view;


import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.DialogMemoBinding;
import com.taehoon.garbagealarm.databinding.TabFragment1Binding;
import com.taehoon.garbagealarm.view.adapter.GarbageAdapter;

/**
 * Created by kth919 on 2017-05-06.
 */

public class TabFragment1 extends Fragment {

    private static String TAG = TabFragment1.class.getName();

    private TabFragment1Binding tab1_binding;
    private RecyclerView recyclerView;
    private DialogMemoBinding dialogMemoBinding;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        tab1_binding = DataBindingUtil.inflate(inflater, R.layout.tab_fragment_1, container, false);

        dialogMemoBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_memo, container, false);

        GarbageAdapter adapter = new GarbageAdapter(getActivity(), getContext());
        tab1_binding.recycler.setAdapter(adapter);
        tab1_binding.recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return tab1_binding.getRoot();
    }

}
