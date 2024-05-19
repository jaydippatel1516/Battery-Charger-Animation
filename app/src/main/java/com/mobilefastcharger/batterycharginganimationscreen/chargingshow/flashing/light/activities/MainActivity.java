package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;


import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.notifications.Notification_;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.service.MyService;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.utils.SharedPrefsUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class MainActivity extends AppCompatActivity {
    public static Notification_ myNotification;
    public static BroadcastReceiver receiver;
    String currentVersion = "";
    boolean first;
    boolean isLock;
  ImageView layoutSelect;
  ImageView layoutTut;
    long mLastClickTime = 0;
    String packageName;
    PowerManager pm;
    Intent serviceIntent;

    public static RippleDrawable getBackgroundDrawable(int i, Drawable drawable) {
        return new RippleDrawable(getPressedState(i), drawable, null);
    }

    public static ColorStateList getPressedState(int i) {
        return new ColorStateList(new int[][]{new int[0]}, new int[]{i});
    }

    public static int getBatteryPercentage(Context context) {
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return (int) ((((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1))) * 100.0f);
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(1024, 1024);

        initView();
        findViewById(R.id.gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        first = SharedPrefsUtils.getBooleanPreference(this, "first", true);
        if (first) {
            startActivity(new Intent(getApplicationContext(), Tutorial1Activity.class));
            finish();
        }
        packageName = getPackageName();
        pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                overlayPermissionPopup();
            } else if (!pm.isIgnoringBatteryOptimizations(this.packageName)) {
                batteryOptimizationDialog();
            }
        }
        initReceiever();
        addBroadcastReceiver();
        if (Build.VERSION.SDK_INT < 23) {
            myNotification = new Notification_(this);
            myNotification.updateStatus("Charging Animation\"");
            prepareForService();
        } else if (Settings.canDrawOverlays(this) && this.pm.isIgnoringBatteryOptimizations(this.packageName)) {
            myNotification = new Notification_(this);
            myNotification.updateStatus("Charging Animation");
            prepareForService();
        }
        layoutSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListActivity.class));

            }
        });
        layoutTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Tutorial1Activity.class).putExtra("showBack", "yes"));

            }
        });
        PushDownAnim.setPushDownAnimTo(findViewById(R.id.rate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMarket();
            }
        });
        PushDownAnim.setPushDownAnimTo(findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        PushDownAnim.setPushDownAnimTo(findViewById(R.id.privacy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void launchMarket() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
        }
    }

    public void share() {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
            intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n");
            startActivity(Intent.createChooser(intent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        layoutSelect = findViewById(R.id.layoutSelectAnimation);
        layoutTut = findViewById(R.id.layoutTutorial);
    }

    private void overlayPermissionPopup() {
        new MaterialAlertDialogBuilder(this).setTitle("Permission Required").setCancelable(false).setMessage(getResources().getString(R.string.overlay)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 1);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }


    private void batteryOptimizationDialog() {

        new MaterialAlertDialogBuilder(this).setTitle("Background Running Permission").setMessage("Allow permission to run app in background").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                finish();
            }
        }).show();
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (i != 1 || !Settings.canDrawOverlays(this)) {
            if (i != 2 || !Settings.canDrawOverlays(this)) {
                finish();
            } else if (this.pm.isIgnoringBatteryOptimizations(this.packageName)) {
                Notification_ anim_Notification = new Notification_(this);
                myNotification = anim_Notification;
                anim_Notification.updateStatus("Charging Animation");
                prepareForService();
            }
        } else if (!this.pm.isIgnoringBatteryOptimizations(this.packageName)) {
            batteryOptimizationDialog();
        }
    }

    public void prepareForService() {
        this.serviceIntent = new Intent(this, MyService.class);
        if (isMyServiceRunning(MyService.class)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            ContextCompat.startForegroundService(this, this.serviceIntent);
        } else {
            startService(this.serviceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initReceiever() {
        receiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                    boolean booleanPreference = SharedPrefsUtils.getBooleanPreference(context, "show", true);
                    if (SystemClock.elapsedRealtime() - mLastClickTime >= 2000 && booleanPreference) {
                        mLastClickTime = SystemClock.elapsedRealtime();
                        isLock = SharedPrefsUtils.getBooleanPreference(context, "lock", false);
                        MyService.removeScreenFilter();
                        if (!isLock) {
                            startActivity(new Intent(getApplicationContext(), LockScreenAnimActivity.class).addFlags(268435456));
                        } else if (((KeyguardManager) context.getSystemService(KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode()) {
                            startActivity(new Intent(getApplicationContext(), LockScreenAnimActivity.class).addFlags(268435456));
                        }
                    }
                } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                    MyService.removeScreenFilter();
                } else if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                    float intExtra = ((float) (intent.getIntExtra("level", -1) * 100)) / ((float) intent.getIntExtra("scale", -1));
                    if (LockScreenAnimActivity.txtPercentage != null) {
                        TextView textView = LockScreenAnimActivity.txtPercentage;
                        textView.setText(((int) intExtra) + "%");
                    }
                }
            }
        };
    }

    private void addBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.BATTERY_LOW");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(receiver, intentFilter);
    }

        @Override
        public void onBackPressed() {
            showDialog();
        }


        public void showDialog() {
            final Dialog dialog = new Dialog(this, R.style.fulldialog);
            dialog.setContentView(R.layout.exit_dialog);
            dialog.setCancelable(false);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.findViewById(R.id.txt_yes).setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    dialog.dismiss();
                    finishAffinity();
                    System.exit(0);
                }
            });

            dialog.findViewById(R.id.txt_no).setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    dialog.dismiss();


                }
            });

        }
}
