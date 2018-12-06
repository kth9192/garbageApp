package com.taehoon.garbagealarm.gpshelpler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

import com.taehoon.garbagealarm.viewmodel.GmapLogic;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by kth919 on 2017-11-12.
 */

public class GpsReceiver extends BroadcastReceiver {

    private static String TAG = GpsReceiver.class.getName();
    GmapLogic gmapLogic;
    GoogleMap googleMap;

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        gmapLogic = new GmapLogic(context);

        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED") && gmapLogic.isPermissionCheck() ) {

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
            Toast.makeText(context, "gps켜짐", Toast.LENGTH_LONG).show();
            gmapLogic.updateMyLocation(googleMap);
        }else {
            googleMap.setMyLocationEnabled(false);
        }
    }
}
