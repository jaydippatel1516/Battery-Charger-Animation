package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.interfaces.DoubleTapCallback;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.interfaces.DoubleTapListener;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.utils.SharedPrefsUtils;

import java.io.IOException;

public class LockScreenAnimActivity extends AppCompatActivity implements DoubleTapCallback {
    public static TextView txtPercentage;
    public int anim;
    public int audio;
    public int closing;
    public int custom;
    public int duration;
    public boolean isLock;
    public boolean isSound;
    public MediaPlayer mediaPlayer;
    BroadcastReceiver broadcastReceiver;
    ImageView imgClick;
    ImageView imgPreview;
    LinearLayout layoutBattery;
    RelativeLayout layoutVideo;
    LottieAnimationView lottieAnimationView;
    String uriCustom;
    VideoView videoView;
    private boolean isPer;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setStatusBarColor(getResources().getColor(R.color.color_toolbar));
        getWindow().getDecorView().setSystemUiVisibility(5890);
        getWindow().addFlags(2621440);
        setContentView(R.layout.activity_lock_screen_anim);
        initView();
        initData();
        showAnim();
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                finish();
            }
        };
        addBroadcastReceiver();
    }

    private void addBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerReceiver(this.broadcastReceiver, intentFilter);
    }

    public void setMediaPlayer(Context context) {
        mediaPlayer = MediaPlayer.create(context, this.audio);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void showAnim() {
        if (anim == R.raw.anim23) {
            lottieAnimationView.setPadding(0, 0, 0, 0);
            lottieAnimationView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        if (this.isPer) {
            layoutBattery.setVisibility(View.VISIBLE);
        } else {
            layoutBattery.setVisibility(View.GONE);
        }
        TextView textView = txtPercentage;
        textView.setText(MainActivity.getBatteryPercentage(this) + "%");
        if (isSound && custom == -1) {
            setMediaPlayer(this);
        }
        int i = this.custom;
        if (i == -1) {
            lottieAnimationView.setAnimation(this.anim);
            layoutVideo.setVisibility(View.GONE);
            imgPreview.setVisibility(View.GONE);
        } else if (i == 0) {
            this.layoutVideo.setVisibility(View.VISIBLE);
            Log.d("uriCustom", this.uriCustom + "abc");
            videoView.setVideoURI(Uri.parse(this.uriCustom));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    if (!isSound) {
                        mediaPlayer.setVolume(0.0f, 0.0f);
                    }
                    if (duration == -1) {
                        mediaPlayer.setLooping(true);
                    }
                }
            });
        } else {
            Bitmap bitmap = null;
            try {
                bitmap = PreviewActivity.rotateImageIfRequired(this, PreviewActivity.uriToBitmap(this, Uri.parse(this.uriCustom)), Uri.parse(this.uriCustom));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgPreview.setImageBitmap(bitmap);
        }
        if (duration != -1) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    finish();
                }
            }, this.duration * 1000);
        }
        if (closing == 1) {
            lottieAnimationView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d("clickAnim", "single");
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    finish();
                }
            });
        } else {
            this.lottieAnimationView.setOnClickListener(new DoubleTapListener(this));
        }
    }

    private void initView() {
        videoView = findViewById(R.id.videoView);
        imgPreview = findViewById(R.id.imgAnim);
        lottieAnimationView = findViewById(R.id.animationView);
        txtPercentage = findViewById(R.id.txtPercentage);
        layoutBattery = findViewById(R.id.layoutBattery);
        imgClick = findViewById(R.id.imgClick);
        layoutVideo = findViewById(R.id.layoutVideo);
    }

    private void initData() {
        anim = SharedPrefsUtils.getIntegerPreference(this, "anim_id", R.raw.anim3);
        audio = SharedPrefsUtils.getIntegerPreference(this, "audio_id", R.raw.music3_new);
        isSound = SharedPrefsUtils.getBooleanPreference(this, "sound", true);
        isLock = SharedPrefsUtils.getBooleanPreference(this, "lock", false);
        isPer = SharedPrefsUtils.getBooleanPreference(this, "per", true);
        duration = SharedPrefsUtils.getIntegerPreference(this, "duration", 5);
        closing = SharedPrefsUtils.getIntegerPreference(this, "closing", 0);
        custom = SharedPrefsUtils.getIntegerPreference(this, "custom", -1);
        uriCustom = SharedPrefsUtils.getStringPreference(this, "custom_uri");
    }

    public void onDoubleClick(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        finish();
        Log.d("clickAnim", "single");
    }

    public void onSingleClick(View view) {
        imgClick.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                imgClick.setVisibility(View.GONE);
            }
        }, 1000);
    }


    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
