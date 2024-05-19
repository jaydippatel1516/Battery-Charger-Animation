package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;

public class SplashActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                gotoMain();
            }
        }, 3000);


    }

    private void gotoMain() {
        if (getIntent().getStringExtra("isLock") != null) {
            startActivity(new Intent(getApplicationContext(), LockScreenAnimActivity.class));
            finish();
            return;
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }


}
