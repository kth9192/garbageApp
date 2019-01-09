package com.taehoon.garbagealarm.viewmodel;

import android.app.Application;
import android.content.Context;

import com.taehoon.garbagealarm.MainActivity;
import com.taehoon.garbagealarm.model.cleanhouse.CleanHouseModel;
import com.taehoon.garbagealarm.repository.addrrepository.AddrRepository;
import com.taehoon.garbagealarm.repository.addrrepository.AddrRoom;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class AddrViewModel extends AndroidViewModel {

    private AddrRepository addrRepository;
    private LiveData<List<AddrRoom>> listLiveData;
    private MediatorLiveData<List<AddrRoom>> mlistLiveData = new MediatorLiveData<>();

    private GmapLogic gmapLogic;

    public AddrViewModel(@NonNull Application application) {
        super(application);
        this.addrRepository = new AddrRepository(application);
    }

    public LiveData<List<AddrRoom>> getListLiveData() {

        listLiveData = addrRepository.getAll();

        mlistLiveData.addSource(listLiveData, addrRooms -> {
            if (listLiveData != null) {
                mlistLiveData.removeSource(listLiveData);
                mlistLiveData.setValue(addrRooms);
            }
        });

        return mlistLiveData;
    }

    public void insert(CleanHouseModel cleanHouseModel) {
        addrRepository.insert(new AddrRoom(UUID.randomUUID().toString(), cleanHouseModel.getAddr(), cleanHouseModel.getDong(),
                cleanHouseModel.getLocation(), cleanHouseModel.getMapX(), cleanHouseModel.getMapY()
        ));
    }

    public List<AddrRoom> getAllAsync(){
        return addrRepository.getAllAsync();
    }

    public int getItemCount() {
        return addrRepository.getItemCount();
    }

    public GmapLogic getGmapLogic() {
        return gmapLogic;
    }

}
