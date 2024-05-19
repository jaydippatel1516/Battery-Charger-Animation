package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

public class Tutorial1Activity extends AppCompatActivity {
    String packageName;
    PowerManager pm;
    String showBack;
    ToggleButton toggleBattery;
    ToggleButton toggleOverlay;
    Toolbar toolbar;
    TextView txtNext;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tutorial1);
        getWindow().setFlags(1024, 1024);

        packageName = getPackageName();
        pm = (PowerManager) getSystemService(POWER_SERVICE);
        showBack = getIntent().getStringExtra("showBack");
        initView();
        if (showBack != null) {
            toolbarSetup();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                toggleOverlay.setChecked(true);
                toggleOverlay.setEnabled(false);
            }
            if (pm.isIgnoringBatteryOptimizations(packageName)) {
                toggleBattery.setChecked(true);
                toggleBattery.setEnabled(false);
            }
            toggleOverlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (z && !Settings.canDrawOverlays(Tutorial1Activity.this)) {
                        overlayPermissionPopup();
                    }
                }
            });
            toggleBattery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (z && !pm.isIgnoringBatteryOptimizations(packageName)) {
                        batteryOptimizationDialog();
                    }
                }
            });
        } else {
            toggleBattery.setChecked(true);
            toggleOverlay.setChecked(true);
            toggleOverlay.setEnabled(false);
            toggleBattery.setEnabled(false);
        }
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT))) {
                    startActivity(new Intent(getApplicationContext(), Tutorial2Activity.class));
                } else if (Build.VERSION.SDK_INT <= 28) {
                    startActivity(new Intent(getApplicationContext(), OtherPermissionActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), Tutorial2Activity.class));
                }
            }
        });
    }

    private void toolbarSetup() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }


    public void overlayPermissionPopup() {
        new MaterialAlertDialogBuilder(this).setTitle("Permission Required").setCancelable(false).setMessage(getResources().getString(R.string.overlay)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 1);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                toggleOverlay.setChecked(false);
            }
        }).show();
    }


    public void batteryOptimizationDialog() {
        new MaterialAlertDialogBuilder(this).setTitle("Battery Optimization").setMessage("Allow permission to optimize battery").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent();
                intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 2);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                toggleBattery.setChecked(false);
            }
        }).show();
    }


    private void initView() {
        toggleOverlay = findViewById(R.id.toggleDrawing);
        toggleBattery = findViewById(R.id.toggleBattery);
        txtNext = findViewById(R.id.txtNext);
        toolbar = findViewById(R.id.layoutActionBar);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (Build.VERSION.SDK_INT >= 23) {
            if (i == 1) {
                if (Settings.canDrawOverlays(this)) {
                    toggleOverlay.setEnabled(false);
                } else {
                    toggleOverlay.setChecked(false);
                }
            }
            if (i != 2) {
                return;
            }
            if (pm.isIgnoringBatteryOptimizations(this.packageName)) {
                toggleBattery.setEnabled(false);
            } else {
                toggleBattery.setChecked(false);
            }
        }
    }
}
