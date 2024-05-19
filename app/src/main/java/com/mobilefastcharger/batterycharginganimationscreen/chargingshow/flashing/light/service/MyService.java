package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities.MainActivity;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.interfaces.Callback;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.interfaces.DoubleTapCallback;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.interfaces.DoubleTapListener;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.utils.SharedPrefsUtils;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.view.FullScreenVideoView;

public class MyService extends Service implements DoubleTapCallback {
    public static int anim;
    public static int audio;
    public static Callback callback;
    public static int closing;
    public static int custom;
    public static int duration;
    public static boolean isLock;
    public static boolean isPer;
    public static boolean isSound;
    public static MediaPlayer mediaPlayer;
    public static TextView txtPercentage;
    static WindowManager.LayoutParams layoutParams;
    static RelativeLayout relativeLayout;
    static WindowManager windowManager;
    ImageView imgPreview;
    LottieAnimationView lottieAnimationView;
    String uriCustom;
    FullScreenVideoView videoView;

    public static int getBatteryPercentage(Context context) {
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return (int) ((((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1))) * 100.0f);
    }

    public static void setMediaPlayer(Context context) {
        MediaPlayer create = MediaPlayer.create(context, audio);
        mediaPlayer = create;
        create.setLooping(true);
        mediaPlayer.start();
    }

    public static int getNavigationBarHeight(Context context) {
        if (context.getResources().getIdentifier("config_showNavigationBar", "bool", "android") == 0) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("navigation_bar_height", "dimen", "android"));
    }

    public static void activateScreenFilter(Context context) {
        callback.callback();
        windowManager.addView(relativeLayout, layoutParams);
        if (isSound && custom == -1) {
            setMediaPlayer(context);
        }
        Log.d("filterActivated", "yus");
    }

    public static void removeScreenFilter() {
        RelativeLayout relativeLayout2 = relativeLayout;
        if (relativeLayout2 != null && relativeLayout2.getParent() != null) {
            MediaPlayer mediaPlayer2 = mediaPlayer;
            if (mediaPlayer2 != null) {
                mediaPlayer2.stop();
            }
            windowManager.removeViewImmediate(relativeLayout);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onSingleClick(View view) {
    }

    public void onCreate() {
        super.onCreate();
        createScreenFilter();
        addBroadcastReceiver();
        initData();
        callback = new Callback() {
            public void callback() {
                MyService.this.createScreenFilter();
            }
        };
    }

    private void addBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.BATTERY_LOW");
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(MainActivity.receiver, intentFilter);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        addBroadcastReceiver();
        initData();
        if (MainActivity.myNotification != null) {
            startForeground(1, MainActivity.myNotification.builder.build());
        }
        return START_STICKY;
    }

    private void initData() {
        anim = SharedPrefsUtils.getIntegerPreference(this, "anim_id", R.raw.anim3);
        audio = SharedPrefsUtils.getIntegerPreference(this, "audio_id", R.raw.music3_new);
        isSound = SharedPrefsUtils.getBooleanPreference(this, "sound", true);
        isLock = SharedPrefsUtils.getBooleanPreference(this, "lock", false);
        duration = SharedPrefsUtils.getIntegerPreference(this, "duration", 5);
        closing = SharedPrefsUtils.getIntegerPreference(this, "closing", 0);
        custom = SharedPrefsUtils.getIntegerPreference(this, "custom", -1);
        uriCustom = SharedPrefsUtils.getStringPreference(this, "custom_uri");
        isPer = SharedPrefsUtils.getBooleanPreference(this, "per", true);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MainActivity.receiver);
    }

    public void createScreenFilter() {
        initData();
        WindowManager windowManager2 = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            layoutParams.type = 2038;
        } else {
            layoutParams.type = 2010;
        }
        layoutParams.format = -3;
        if (isLock) {
            if (duration == -1) {
                layoutParams.flags = 6817152;
            } else {
                layoutParams.flags = 6817152;
            }
        } else if (duration == -1) {
            layoutParams.flags = 1408;
        } else {
            layoutParams.flags = 1280;
        }
        getNavigationBarHeight(getApplicationContext());
        windowManager2.getDefaultDisplay().getRealMetrics(new DisplayMetrics());
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.format = -3;
        relativeLayout = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.screen_filter, null);
        LinearLayout linearLayout = relativeLayout.findViewById(R.id.layoutBattery);
       videoView = relativeLayout.findViewById(R.id.videoView);
       imgPreview = relativeLayout.findViewById(R.id.imgAnim);
       lottieAnimationView = relativeLayout.findViewById(R.id.animationView);
        txtPercentage = relativeLayout.findViewById(R.id.txtPercentage);
        txtPercentage.setText(getBatteryPercentage(this) + "%");
        if (isPer) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        int i = custom;
        if (i == -1) {
            lottieAnimationView.setAnimation(anim);
            videoView.setVisibility(View.GONE);
            imgPreview.setVisibility(View.GONE);
        } else if (i == 0) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(this.uriCustom));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    if (!MyService.isSound) {
                        mediaPlayer.setVolume(0.0f, 0.0f);
                    }
                    if (MyService.duration == -1) {
                        mediaPlayer.setLooping(true);
                    }
                }
            });
        } else {
            this.imgPreview.setImageURI(Uri.parse(this.uriCustom));
        }
        if (duration != -1) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MyService.removeScreenFilter();
                }
            }, duration * 1000);
        }
        if (closing == 1) {
            this.lottieAnimationView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MyService.removeScreenFilter();
                }
            });
        } else {
            this.lottieAnimationView.setOnClickListener(new DoubleTapListener(this));
        }
    }

    public void onDoubleClick(View view) {
        removeScreenFilter();
    }
}
