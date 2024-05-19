package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;


public class Tutorial2Activity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtNext;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tutorial2);
        getWindow().setFlags(1024, 1024);

        initView();
        toolbarSetup();
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Tutorial3Activity.class));

            }
        });

    }

    private void initView() {
        txtNext = findViewById(R.id.txtNext);
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
