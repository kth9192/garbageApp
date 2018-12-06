package com.taehoon.garbagealarm.viewmodel.apihelper;

import android.content.Context;
import android.util.Log;

import com.taehoon.garbagealarm.DAO.CleanHouseApi;
import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.model.cleanhouse.CleanHouseModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kth919 on 2017-10-29.
 */

public class CleanHouseHelper {

    private static String TAG = CleanHouseHelper.class.getName();
    private String apiKey;

    public CleanHouseHelper(String apikey) {
       this.apiKey = apikey;
    }

    public ArrayList<CleanHouseModel> apiParserInit() throws Exception{

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp =  factory.newPullParser();
        ArrayList<CleanHouseModel> tmpList = new ArrayList<>();
        int startPage = 1;
        int pagaeSize = 100;

        String addr = null;
        String dong = null;
        String location = null;
        double mapX = 0;
        double mapY = 0;

        while (startPage <= 19) {

            CleanHouseApi cleanHouseApi = new CleanHouseApi(apiKey, startPage, pagaeSize);
            URL url = new URL(cleanHouseApi.getAPIurl());
            Log.d(TAG, "URL확인 + "+ url);

            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            xpp.setInput(bis, "UTF-8");

            String tag = null;
            int event_type = xpp.getEventType();

            while (event_type != XmlPullParser.END_DOCUMENT) {

                if (event_type == XmlPullParser.START_TAG) {
                    tag = xpp.getName();
                    Log.d(TAG, "시작 로그" + tag);
                } else if (event_type == XmlPullParser.TEXT) {
                    Log.d(TAG, "로그" + tag);

                    if (tag != null && tag.equals("address")) {

                        addr = xpp.getText();
                        Log.d(TAG, "주소" + addr);
                    }else if (tag != null && tag.equals("dong")){
                        dong = xpp.getText();
                        Log.d(TAG, "동" + dong);
                    }else if (tag != null && tag.equals("location")){
                        location = xpp.getText();
                        Log.d(TAG, "위치" + location);
                    }
                    else if (tag != null && tag.equals("mapx")){

                        if (xpp.getText().equals("33.30325,126")){
                            mapX = 33.30325;
                            //제주특별자치도 제주시 한경면 고산리 250 좌표틀림
                        }else {
                            mapX = Double.parseDouble(xpp.getText());
                        }
                        Log.d(TAG, "x좌표" + mapX);
                    }else if (tag != null && tag.equals("mapy")){

                        mapY = Double.parseDouble(xpp.getText());
                        Log.d(TAG, "y좌표" + mapY);
                    }
                } else if (event_type == XmlPullParser.END_TAG) {
                    tag = xpp.getName();
                    Log.d(TAG, "닫기 로그" + tag);

                    if (tag.equals("list")) {
                        Log.d(TAG, "추가");
                        tmpList.add(new CleanHouseModel(addr, dong, location, mapX, mapY));
                    }
                }
                event_type = xpp.next();
            }
            startPage++;
        }
        return tmpList;
    }
}
