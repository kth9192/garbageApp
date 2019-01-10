package com.taehoon.garbagealarm.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.gun0912.tedpermission.TedPermission;
import com.taehoon.garbagealarm.DAO.NaverMapApi;
import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.model.cleanhouse.CleanHouseModel;
import com.taehoon.garbagealarm.model.cleanhouse.ItemModel;
import com.taehoon.garbagealarm.model.cleanhouse.NGeoDomain;
import com.taehoon.garbagealarm.model.cleanhouse.NgeoCodeModel;
import com.taehoon.garbagealarm.repository.addrrepository.AddrRoom;
import com.taehoon.garbagealarm.viewmodel.apihelper.CleanHouseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kth919 on 2017-11-08.
 */

public class GmapLogic {

    private static String TAG = GmapLogic.class.getName();

//    private GoogleMap googleMap;
//    private boolean permissionCheck = true;
//    private LocationManager locationManager;

    private CleanHouseHelper cleanHouseHelper;

    ItemModel.MapPoint mapPoint = new ItemModel.MapPoint();

    private NaverMapApi naverMapApi;
    private Gson gson = new Gson();
    private NgeoCodeModel nGeoItem;

//    private static double longitudeStart;    //경도
//    private static double latitudeStart;

    public GmapLogic(Context context) {
//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        naverMapApi = new NaverMapApi(context);
        cleanHouseHelper = new CleanHouseHelper(context.getString(R.string.cleanhouse_key));
    }

    public ArrayList<MarkerOptions> getNearHouseMarker(String addr, AddrViewModel addrViewModel) {

        ArrayList<MarkerOptions> markerResult = new ArrayList<>();
        ArrayList<CleanHouseModel> cleanHouseList = new ArrayList<>();

        cleanHouseList = getGeoList(addr, addrViewModel);

        GetHouseTask getHouseTask = new GetHouseTask(cleanHouseList);
        getHouseTask.start();

        try {
            getHouseTask.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "finalResult thread error " + e);
        }

        markerResult = getHouseTask.getResult();

        return markerResult;
    }

    private ArrayList<CleanHouseModel> getGeoList(String addr, AddrViewModel addrViewModel) {

        ArrayList<CleanHouseModel> answer = new ArrayList<>();
        ArrayList<CleanHouseModel> cleanHouseList;

        if (addrViewModel.getItemCount() == 0) {

            HouseApiTask houseApiTask = new HouseApiTask();
            houseApiTask.start();

            try {
                houseApiTask.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "house thread error" + e);
            }

            cleanHouseList = houseApiTask.getTmpHouseModel();

            ArrayList<CleanHouseModel> tmplist = new ArrayList<>();

            for (int i = 0; i < cleanHouseList.size(); i++) {

                tmplist.add(new CleanHouseModel(cleanHouseList.get(i).getAddr(), cleanHouseList.get(i).getDong(), cleanHouseList.get(i).getLocation(),
                        cleanHouseList.get(i).getMapX(), cleanHouseList.get(i).getMapY()));
            }

            answer = getNearHouse(addr, tmplist);
            setLocalDB(cleanHouseList, addrViewModel);

            return answer;

        } else {

            for (AddrRoom tmp : addrViewModel.getAllAsync()) {
                answer.add(new CleanHouseModel(tmp.getAddr(), tmp.getDong(), tmp.getLocation(),
                        tmp.getMapX(), tmp.getMapY()));
            }

            return answer;
        }
    }

    private void setLocalDB(ArrayList<CleanHouseModel> sources, AddrViewModel addrViewModel) {

        for (CleanHouseModel e : sources) {
            addrViewModel.insert(e);
        }
    }

    private ArrayList<CleanHouseModel> getNearHouse(String addr, ArrayList<CleanHouseModel> apiAddr) {

        ArrayList<CleanHouseModel> result = new ArrayList<>();

        //TODO 구글 검색창으로 받은 검색어를 cleanhouse api 의 주소와 비교해서 필요한 것만 뽑은 다음
        // TODO 걸러낸 값을 네이버 지오코딩 api에 검색해서 그 주소 좌표를 얻어낸 후 마커 리스트로 만듦.
        //TODO 좌표가 가질 값 : address, location , mapX, mapY

        for (int i = 0; i < apiAddr.size(); i++) {
            if (checkCorrectAddr(addr, apiAddr.get(i))) {

                RoadApiTask roadApiTask = new RoadApiTask(apiAddr.get(i).getAddr());
                roadApiTask.start();

                try {
                    roadApiTask.join();
                } catch (InterruptedException e) {
                    Log.e(TAG, "roadApi thread error" + e);
                }
                mapPoint = roadApiTask.getTmpPoint();
                Log.d(TAG, "mapPoint source" + mapPoint.getMapX());

                if (mapPoint != null) {
                    result.add(new CleanHouseModel(apiAddr.get(i).getAddr(), apiAddr.get(i).getDong(), apiAddr.get(i).getLocation(),
                            mapPoint.getMapX(), mapPoint.getMapY()));
                } else {
                    Log.i(TAG, "맵포인트 null");
                }
            }
        }
        return result;
    }

    private boolean checkCorrectAddr(String addr, CleanHouseModel apiAddr) {

        boolean answer = false;

        if (apiAddr.getAddr().contains(convertKORtoNum(addr)) || apiAddr.getDong().contains(convertKORtoNum(addr))
                || apiAddr.getAddr().contains(addr) || apiAddr.getDong().contains(addr)) {
            answer = true;
        }
        return answer;
    }

    private String convertKORtoNum(String tmp) {

        StringBuilder sb = new StringBuilder(tmp);

        if (tmp.length() > 0 && sb.charAt(tmp.length() - 2) == '일') {
            sb.setCharAt(tmp.length() - 2, '1');
        } else if (tmp.length() > 0 && sb.charAt(tmp.length() - 2) == '이') {
            sb.setCharAt(tmp.length() - 2, '2');
        } else if (tmp.length() > 0 && sb.charAt(tmp.length() - 2) == '삼') {
            sb.setCharAt(tmp.length() - 2, '3');
        }

        return sb.toString();
    }

    private ItemModel.MapPoint convertAddr(String source) { // 잘못된 공공api 주소를 네이버 지오코딩을 거쳐 정상좌표로 반환

        try {
            String addr = URLEncoder.encode(source, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
            //String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", naverMapApi.getCLIENT_ID());
            con.setRequestProperty("X-Naver-Client-Secret", naverMapApi.getCLIENT_PW());
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = null;
//                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                Log.e(TAG, "에러발생" + new BufferedReader(new InputStreamReader(con.getErrorStream())));
            }

            if (br != null) {
                NGeoDomain nGeoDomain = gson.fromJson(br, NGeoDomain.class);
                nGeoItem = nGeoDomain.getHouseList();
            }

            ArrayList<ItemModel> itemList = nGeoItem.getItems();

            return itemList.get(0).getPoint();

        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
            return null;
        }
    }

//    private PermissionListener permissionlistener = new PermissionListener() {
//        @Override
//        public void onPermissionGranted() {
////            Toast.makeText(context, "권한 허가", Toast.LENGTH_SHORT).show();
//
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            permissionCheck = true;
//        }
//
//        @Override
//        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            Toast.makeText(context, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    //GPS 설정 체크
//    private void onProviderEnabled() {
//        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
//                .setMessage(R.string.permission_phrases_gps)
//                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 설정 창을 띄운다
//                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        context.startActivity(intent);
//                    }
//                })
//                .setNegativeButton("취소", null).show();
//
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    100, 1 / 5, mMyLocationListener);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    100, 1 / 5, mMyLocationListener);
//            googleMap.setMyLocationEnabled(true);
//        }
//    }
//
//    private LocationListener mMyLocationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(Location location) {
//
//            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)
//                    || location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
//
//                double longitude = location.getLongitude();    //경도
//                double latitude = location.getLatitude();       //위도
//                float accuracy = location.getAccuracy();
//
////                longitudeStart = location.getLongitude();
////                latitudeStart = location.getLongitude();
//
//                //// TODO: 2017-11-10 버튼누르면 위치 초기화하기
//                LatLng me = new LatLng(latitude, longitude);
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 15));
//
//                getEnd(mMyLocationListener);
//
////                Log.d(TAG, " 위치" + longitude + " : " + latitude + ":" + accuracy);
//            }
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
////            Log.d(TAG, "onStatusChanged" + status);
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
////            Log.d(TAG, "onProviderEnabled");
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
////            Log.d(TAG, "onProviderDisabled");
//            if (provider.equals("gps")) {
//                locationManager.removeUpdates(mMyLocationListener);
//            }
//        }
//    };
//
//    public void updateMyLocation(GoogleMap googlemap) {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            Log.i(TAG, "권한 문제");
//
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        this.googleMap = googlemap;
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                0, 0, mMyLocationListener);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                0, 0, mMyLocationListener);
//
//        googleMap.setMyLocationEnabled(true);
//    }
//
//    private void getEnd(LocationListener locationListener) {
//        locationManager.removeUpdates(locationListener);
//    }

    private class HouseApiTask extends Thread { // 제주공공 api

        private ArrayList<CleanHouseModel> tmpHouseModel;

        @Override
        public void run() {
            super.run();
            try {
                tmpHouseModel = cleanHouseHelper.apiParserInit();
            } catch (Exception e) {
                Log.e(TAG, "api async 에러" + String.valueOf(e));
            }
        }

        public ArrayList<CleanHouseModel> getTmpHouseModel() {
            return tmpHouseModel;
        }
    }

    private class RoadApiTask extends Thread { //naver api

        private String TAG2 = RoadApiTask.class.getName();
        private ItemModel.MapPoint tmpPoint;
        private String addr;

        public RoadApiTask(String addr) {
            this.addr = addr;
        }

        @Override
        public void run() {
            super.run();
            tmpPoint = convertAddr(addr);

            if (tmpPoint != null) {
                Log.d(TAG2, "load after" + tmpPoint.getMapX() + ": " + tmpPoint.getMapY());
            } else {
                Log.d(TAG2, "tmpPoint null");
            }
        }

        public ItemModel.MapPoint getTmpPoint() {
            return tmpPoint;
        }
    }

    private class GetHouseTask extends Thread { // 구글맵 마커

        ArrayList<CleanHouseModel> tmpGeoList;

        ArrayList<MarkerOptions> result = new ArrayList<>();

        public GetHouseTask(ArrayList<CleanHouseModel> tmpGeoList) {
            this.tmpGeoList = tmpGeoList;
        }

        @Override
        public void run() {
            super.run();
            StringBuilder tmptitle = new StringBuilder();
            LatLng tmpLocation;

            for (int i = 0; i < tmpGeoList.size(); i++) {
                tmpLocation = new LatLng(tmpGeoList.get(i).getMapY(), tmpGeoList.get(i).getMapX());
                Log.d(TAG, "좌표확인" + tmpLocation.toString());

                tmptitle.append(tmpGeoList.get(i).getLocation());
                result.add(new MarkerOptions().position(tmpLocation).title(tmptitle.toString()));
                Log.d(TAG, "좌표확인" + result.get(i).getPosition() + " : " + result.get(i).getTitle());
                tmptitle.setLength(0);
            }
        }

        public ArrayList<MarkerOptions> getResult() {
            return result;
        }
    }

}
