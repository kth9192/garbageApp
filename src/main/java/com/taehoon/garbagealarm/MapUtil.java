package com.taehoon.garbagealarm;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class MapUtil {

    private Context context;
    private GoogleMap googleMap;
    private boolean permissionCheck = true;
    private LocationManager locationManager;

    public MapUtil(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void doCheckPermission() {
        if (!isPermissionCheck()) {
            onProviderEnabled();
            TedPermission.with(context)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage(R.string.permission_phrases_network)
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        }
    }

    private PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Toast.makeText(context, "권한 허가", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            permissionCheck = true;
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(context, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

        //GPS 설정 체크
    private void onProviderEnabled() {
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog))
                .setMessage(R.string.permission_phrases_gps)
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 설정 창을 띄운다
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("취소", null).show();

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100, 1 / 5, mMyLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    100, 1 / 5, mMyLocationListener);
            googleMap.setMyLocationEnabled(true);
        }
    }

    private LocationListener mMyLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)
                    || location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {

                double longitude = location.getLongitude();    //경도
                double latitude = location.getLatitude();       //위도
                float accuracy = location.getAccuracy();

//                longitudeStart = location.getLongitude();
//                latitudeStart = location.getLongitude();

                //// TODO: 2017-11-10 버튼누르면 위치 초기화하기
                LatLng me = new LatLng(latitude, longitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 15));

                getEnd(mMyLocationListener);

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.d(TAG, "onStatusChanged" + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
//            Log.d(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
//            Log.d(TAG, "onProviderDisabled");
            if (provider.equals("gps")) {
                locationManager.removeUpdates(mMyLocationListener);
            }
        }
    };

    public void updateMyLocation(GoogleMap googlemap) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.i(TAG, "권한 문제");

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.googleMap = googlemap;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, mMyLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0, 0, mMyLocationListener);

        googleMap.setMyLocationEnabled(true);
    }

    private void getEnd(LocationListener locationListener) {
        locationManager.removeUpdates(locationListener);
    }

    public boolean isPermissionCheck() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            permissionCheck = false;
        }
        return permissionCheck;
    }
}
