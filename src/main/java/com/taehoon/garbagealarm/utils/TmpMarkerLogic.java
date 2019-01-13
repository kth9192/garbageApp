package com.taehoon.garbagealarm.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.taehoon.garbagealarm.DAO.NaverMapApi;
import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.model.cleanhouse.CleanHouseModel;
import com.taehoon.garbagealarm.model.cleanhouse.ItemModel;
import com.taehoon.garbagealarm.model.cleanhouse.NGeoDomain;
import com.taehoon.garbagealarm.model.cleanhouse.NgeoCodeModel;
import com.taehoon.garbagealarm.repository.addrrepository.AddrRoom;
import com.taehoon.garbagealarm.viewmodel.AddrViewModel;
import com.taehoon.garbagealarm.viewmodel.GmapLogic;
import com.taehoon.garbagealarm.viewmodel.apihelper.CleanHouseHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TmpMarkerLogic {

    //TODO : addr 값 없이 전체 api를 모두 불러오기
    private static String TAG = TmpMarkerLogic.class.getSimpleName();

    private NaverMapApi naverMapApi;
    private Gson gson = new Gson();
    private CleanHouseHelper cleanHouseHelper;
    private ItemModel.MapPoint mapPoint = new ItemModel.MapPoint();
    NgeoCodeModel nGeoItem;

    public TmpMarkerLogic(Context context) {
        naverMapApi = new NaverMapApi(context);
        cleanHouseHelper = new CleanHouseHelper(context.getString(R.string.cleanhouse_key));
    }

    public ArrayList<CleanHouseModel> getGeoList(String addr, AddrViewModel addrViewModel) {

        ArrayList<CleanHouseModel> answer = new ArrayList<>();

        for (AddrRoom tmp : addrViewModel.getAllAsync()) {
            if (tmp.getDong().contains(convertKORtoNum(addr))) {
                answer.add(new CleanHouseModel(tmp.getAddr(), tmp.getDong(), tmp.getLocation(),
                        tmp.getMapX(), tmp.getMapY()));
            }
        }

        return answer;
    }

    public ArrayList<CleanHouseModel> getNearHouseWithNaver(ArrayList<CleanHouseModel> apiAddr) {

        ArrayList<CleanHouseModel> result = new ArrayList<>();

        // TODO 걸러낸 값을 네이버 지오코딩 api에 검색해서 그 주소 좌표를 얻어낸 후 마커 리스트로 만듦.
        //TODO 좌표가 가질 값 : address, location , mapX, mapY

        for (int i = 0; i < apiAddr.size(); i++) {
            if (!apiAddr.get(i).getDong().isEmpty() && !apiAddr.get(i).getAddr().isEmpty()) {
                RoadApiTask roadApiTask = new RoadApiTask(apiAddr.get(i).getAddr());
                roadApiTask.start();

                try {
                    roadApiTask.join();
                } catch (InterruptedException e) {
                    Log.e(TAG, "roadApi thread error" + e);
                }
                mapPoint = roadApiTask.getTmpPoint();
//                Log.d(TAG, "mapPoint source" + mapPoint.getMapX());

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

                StringBuffer buffer = new StringBuffer();
                InputStream is = con.getErrorStream();
                byte[] b = new byte[4096];
                int i;
                while( (i = is.read(b)) != -1){
                    buffer.append(new String(b, 0, i));
                }
                String str = buffer.toString();

                Log.e(TAG, responseCode + "에러발생" + str);
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

}
