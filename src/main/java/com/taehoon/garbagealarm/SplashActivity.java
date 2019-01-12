package com.taehoon.garbagealarm;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by kth919 on 2017-10-11.
 */

public class SplashActivity extends AppCompatActivity {

    private static String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
