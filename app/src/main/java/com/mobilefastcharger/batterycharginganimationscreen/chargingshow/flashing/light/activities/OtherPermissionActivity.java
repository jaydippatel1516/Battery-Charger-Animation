package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;


public class OtherPermissionActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtNext;
    TextView txtSet1;
    TextView txtSet2;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_other_permission);
        getWindow().setFlags(1024, 1024);
        initView();
        toolbarSetup();
        txtNext.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Tutorial2Activity.class)));

        txtSet1.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivityForResult(intent, 1000);
        });
        txtSet2.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivityForResult(intent, 1000);
        });
    }

    private void initView() {
        txtSet1 = findViewById(R.id.txtSet);
        txtSet2 = findViewById(R.id.txtSet2);
        txtNext = findViewById(R.id.txtNext);
        toolbar = findViewById(R.id.layoutActionBar);
    }

    private void toolbarSetup() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }
}
