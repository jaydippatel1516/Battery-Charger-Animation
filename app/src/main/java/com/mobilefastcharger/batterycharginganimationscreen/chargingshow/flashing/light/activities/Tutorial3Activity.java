package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.utils.SharedPrefsUtils;

public class Tutorial3Activity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtNext;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tutorial3);
        getWindow().setFlags(1024, 1024);

        initView();
        toolbarSetup();
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtils.setBooleanPreference(getApplicationContext(), "first", false);
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(335577088));
            }
        });


    }

    private void initView() {
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
