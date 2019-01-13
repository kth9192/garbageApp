package com.taehoon.garbagealarm.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.taehoon.garbagealarm.utils.MapUtil;
import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.TabFragment3Binding;
import com.taehoon.garbagealarm.gpshelpler.GpsReceiver;
import com.taehoon.garbagealarm.viewmodel.AddrViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kth919 on 2017-11-08.
 */

public class TabFragment3 extends Fragment implements OnMapReadyCallback {

    private static String TAG = TabFragment3.class.getName();
    private SupportMapFragment mapFragment;
    private static View view;

    private GoogleMap mGoogleMap;
    private ArrayList<MarkerOptions> tmp;
    private GpsReceiver gpsReceiver;
    private ProgressDialog progressDialog;
    private MultiThreading backgroundThread;
    private Bundle savedInstanceState;

    private AddrViewModel addrViewModel;
    private MapUtil mapUtil;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && savedInstanceState != null) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TabFragment3Binding tabFragment3Binding = DataBindingUtil.inflate(inflater, R.layout.tab_fragment_3, container, false);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapUtil = new MapUtil(getContext());

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        addrViewModel = ViewModelProviders.of(this).get(AddrViewModel.class);

        view = tabFragment3Binding.getRoot();

        progressDialog = new ProgressDialog(new ContextThemeWrapper(getContext(), R.style.myDialog));

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager()
                .findFragmentById(R.id.place_auto_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setHint(getContext().getResources().getString(R.string.search_hint));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.d(TAG, "장소 정보 : " + place.toString());

                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("로딩중...");
                    progressDialog.show();

                    backgroundThread = new MultiThreading(place.getName().toString());
                    backgroundThread.start();
                }

                @Override
                public void onError(Status status) {
                    Log.d(TAG, "장소 검색 실패" + status);
                }
            });
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getContext()).unregisterReceiver(gpsReceiver);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapUtil.doCheckPermission();
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> addrViewModel.setLocalDB());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        gpsReceiver = new GpsReceiver(mGoogleMap);

        if (!mapUtil.isPermissionCheck()) {
            Log.i(TAG, "GPS 권한 해제");

            LatLng defaultlocation = new LatLng(33.3759, 126.5266);
            CameraPosition defaultPosition = new CameraPosition.Builder()
                    .target(defaultlocation)      // Sets the center of the map to Mountain View
                    .zoom(10)                   // Sets the zoom
                    .build();

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(defaultPosition));

        } else {
            Log.i(TAG, "GPS 권한 허가");

//            Log.i(TAG, String.valueOf(mGoogleMap.getUiSettings().isMyLocationButtonEnabled()));

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mapUtil.updateMyLocation(mGoogleMap);
        }

        IntentFilter gpsFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        getContext().registerReceiver(gpsReceiver, gpsFilter);
    }

    private class MultiThreading extends Thread {

        private String addr;
        ArrayList<MarkerOptions> answer;

        MultiThreading(String addr) {
            this.addr = addr;
        }

        @Override
        public void run() {
            super.run();
//            answer = gmapLogic.getNearHouseMarker(addr, addrViewModel);
            answer = addrViewModel.getMarks(addr);
            handler.sendMessage(handler.obtainMessage());
        }

        public ArrayList<MarkerOptions> getAnswer() {
            return answer;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressDialog.dismiss();
            boolean retry = true;

            while (retry) {
                try {
                    backgroundThread.join();
                    retry = false;
                    Log.d(TAG, "background processing");
                } catch (InterruptedException e) {
                    Log.d(TAG, "background process error" + e);
                }
            }
            tmp = backgroundThread.getAnswer();

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    mGoogleMap.clear();

                    if (tmp.size() != 0) {
                        for (int i = 0; i < tmp.size(); i++) {
                            mGoogleMap.addMarker(tmp.get(i));
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("잘못된 주소입니다! 동, 리로 입력해 보세요.")
                                .setTitle("오류창");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    };
}