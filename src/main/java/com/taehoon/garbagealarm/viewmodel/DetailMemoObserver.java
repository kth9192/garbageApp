package com.taehoon.garbagealarm.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import com.taehoon.garbagealarm.repository.memorepository.MemoRoom;

/**
 * Created by kth919 on 2017-12-11.
 */

public class DetailMemoObserver extends BaseObservable {

    private MemoRoom memoRoom;

    public DetailMemoObserver(MemoRoom memoRoom) {
        this.memoRoom = memoRoom;
    }

    @Bindable
    public String getMemo() {
        return memoRoom.getMemo();
    }

    @Bindable
    public String getTime(){
        return memoRoom.getTime();
    }

}
