package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.airbnb.lottie.LottieAnimationView;

import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.utils.SharedPrefsUtils;

import java.io.IOException;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {
    public  static TextView txtPer;
    public int anim;
    public int audio;
    public int closing;
    public int custom;
    public int duration;
    public boolean isLock;
    public boolean isPer;
    public boolean isSound;
    public MediaPlayer mediaPlayer;
    public boolean show;
    int audioId;
    ImageView imgAnim;
    ImageView imgBack;
    ImageView imgDismiss;
    ImageView imgOpenSettings;
    ImageView imgPreview;
    Uri imgUri;
    LinearLayout layoutBattery;
    RelativeLayout layoutVideo;
    LottieAnimationView lottieAnimationView;
    Dialog mainDialog;
    int raw = 0;
    TextView txtApply;
    Uri uri;
    String uriCustom;
    MediaPlayer videoMediaPlayer;
    VideoView videoView;


    public static Bitmap rotateImageIfRequired(Context context, Bitmap bitmap, Uri uri2) throws IOException {
        int attributeInt = new ExifInterface(context.getContentResolver().openInputStream(uri2)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        if (attributeInt == 3) {
            return rotateImageCamera(bitmap, 180);
        }
        if (attributeInt == 6) {
            return rotateImageCamera(bitmap, 90);
        }
        if (attributeInt != 8) {
            return bitmap;
        }
        return rotateImageCamera(bitmap, 270);
    }

    public static Bitmap rotateImageCamera(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) i);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return createBitmap;
    }

    public static Bitmap uriToBitmap(Context context, Uri uri2) {
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(uri2, "r");
            Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(openFileDescriptor.getFileDescriptor());
            openFileDescriptor.close();
            return decodeFileDescriptor;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getDuration(int i) {
        if (i == 0) {
            return 5;
        }
        if (i == 1) {
            return 10;
        }
        if (i == 2) {
            return 30;
        }
        return i == 3 ? 60 : -1;
    }

    private int getSpinnerPos(int i) {
        if (i == 5) {
            return 0;
        }
        if (i == 10) {
            return 1;
        }
        if (i == 30) {
            return 2;
        }
        return i == 60 ? 3 : 4;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_anim_preview);
        getWindow().setFlags(1024, 1024);
        initView();
        initData();
        TextView textView = txtPer;
        textView.setText(MainActivity.getBatteryPercentage(this) + "%");
        int intExtra = getIntent().getIntExtra("raw", 0);
        raw = intExtra;
        Log.i("rawIs", String.valueOf(intExtra));
        audioId = getIntent().getIntExtra("audio", 0);
        if (raw == R.raw.anim23) {
            lottieAnimationView.setPadding(0, 0, 0, 0);
            lottieAnimationView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        if (getIntent().getStringExtra("uri") != null) {
            uri = Uri.parse(getIntent().getStringExtra("uri"));
        }
        if (getIntent().getStringExtra("img_uri") != null) {
            imgUri = Uri.parse(getIntent().getStringExtra("img_uri"));
        }
        if (raw == anim) {
            txtApply.setText("Applied");
        }
        Uri uri2 = uri;
        if (uri2 != null && uri2.toString().equals(this.uriCustom)) {
            this.txtApply.setText("Applied");
        }
        Uri uri3 = this.imgUri;
        if (uri3 != null && uri3.toString().equals(this.uriCustom)) {
            this.txtApply.setText("Applied");
        }
        if (uri != null) {
            layoutVideo.setVisibility(View.VISIBLE);
            setVideoPlayer();
        } else if (raw != 0) {
            layoutVideo.setVisibility(View.GONE);
            imgAnim.setVisibility(View.GONE);
            lottieAnimationView.setAnimation(this.raw);
            lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }
            });
            setMediaPlayer();
        } else {
            layoutVideo.setVisibility(View.GONE);
            imgAnim.setVisibility(View.VISIBLE);
            Bitmap bitmap = null;
            try {
                bitmap = rotateImageIfRequired(this, uriToBitmap(this, this.imgUri), this.imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgAnim.setImageBitmap(bitmap);
        }
        imgPreview.setOnClickListener(view -> {
            imgPreview.setVisibility(View.GONE);
            imgBack.setVisibility(View.GONE);
            imgOpenSettings.setVisibility(View.GONE);
            txtApply.setVisibility(View.GONE);
            layoutBattery.setVisibility(View.VISIBLE);
            initData();
            if (isPer) {
                layoutBattery.setVisibility(View.VISIBLE);
            } else {
                layoutBattery.setVisibility(View.GONE);
            }
        });
        lottieAnimationView.setOnClickListener(view -> {
            imgPreview.setVisibility(View.VISIBLE);
            imgBack.setVisibility(View.VISIBLE);
            layoutBattery.setVisibility(View.GONE);
            imgOpenSettings.setVisibility(View.VISIBLE);
            txtApply.setVisibility(View.VISIBLE);
        });
        imgBack.setOnClickListener(view -> onBackPressed());
        imgOpenSettings.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                initData();
                showSettingPopup(true);
            }
        });
        txtApply.setOnClickListener(view -> {
            SharedPrefsUtils.setIntegerPreference(PreviewActivity.this, "custom", -1);
            SharedPrefsUtils.setIntegerPreference(PreviewActivity.this, "anim_id", raw);
            PreviewActivity anim_PreviewActivity2 = PreviewActivity.this;
            SharedPrefsUtils.setIntegerPreference(anim_PreviewActivity2, "audio_id", anim_PreviewActivity2.audioId);
            if (uri != null) {
                SharedPrefsUtils.setIntegerPreference(PreviewActivity.this, "custom", 0);
                SharedPrefsUtils.setStringPreference(PreviewActivity.this, "custom_uri", uri.toString());
                SharedPrefsUtils.setIntegerPreference(PreviewActivity.this, "anim_id", -1);
                initData();
                Log.d("uriCustom", uriCustom + "abc");
            }
            if (imgUri != null) {
                SharedPrefsUtils.setIntegerPreference(PreviewActivity.this, "custom", 1);
                SharedPrefsUtils.setStringPreference(PreviewActivity.this, "custom_uri", imgUri.toString());
                initData();
                Log.d("uriCustom", uriCustom + "abc");
            }
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            finish();
        });
    }


    private void initView() {
        lottieAnimationView = findViewById(R.id.animationView);
        videoView = findViewById(R.id.videoView);
        txtApply = findViewById(R.id.txtConfirm);
        imgAnim = findViewById(R.id.imgAnim);
        txtPer = findViewById(R.id.txtPercentage);
        imgBack = findViewById(R.id.imgSettings);
        imgOpenSettings = findViewById(R.id.imgOpenSettings);
        imgPreview = findViewById(R.id.imgPreview);
        layoutBattery = findViewById(R.id.layoutBattery);
        layoutVideo = findViewById(R.id.layoutVideo);
    }

    private void setVideoPlayer() {
        Uri uri2 = this.uri;
        if (uri2 != null) {
            videoView.setVideoURI(uri2);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    initData();
                    if (!isSound) {
                        mediaPlayer.setVolume(0.0f, 0.0f);
                    }
                    mediaPlayer.setLooping(true);
                    videoMediaPlayer = mediaPlayer;
                }
            });
        }
    }


    public void setMediaPlayer() {
        initData();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        boolean z = isSound;
        if (z) {
            int i = audioId;
            if (i != 0) {
                MediaPlayer create = MediaPlayer.create(this, i);
                this.mediaPlayer = create;
                create.setLooping(true);
                this.mediaPlayer.start();
                return;
            }
            if (videoMediaPlayer != null && z) {
                videoMediaPlayer.setVolume(1.0f, 1.0f);
                return;
            }
            return;
        }
        if (videoMediaPlayer != null) {
            videoMediaPlayer.setVolume(0.0f, 0.0f);
        }
    }

    public void onBackPressed() {
        if (imgPreview.getVisibility() == View.GONE) {
            imgPreview.setVisibility(View.VISIBLE);
            imgBack.setVisibility(View.VISIBLE);
            layoutBattery.setVisibility(View.GONE);
            txtApply.setVisibility(View.VISIBLE);
            imgOpenSettings.setVisibility(View.VISIBLE);
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        super.onBackPressed();
    }


    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        super.onDestroy();
    }


    public void initData() {
        anim = SharedPrefsUtils.getIntegerPreference(this, "anim_id", R.raw.anim3);
        audio = SharedPrefsUtils.getIntegerPreference(this, "audio_id", R.raw.music3_new);
        isSound = SharedPrefsUtils.getBooleanPreference(this, "sound", true);
        isLock = SharedPrefsUtils.getBooleanPreference(this, "lock", false);
        isPer = SharedPrefsUtils.getBooleanPreference(this, "per", true);
        show = SharedPrefsUtils.getBooleanPreference(this, "show", true);
        duration = SharedPrefsUtils.getIntegerPreference(this, "duration", 5);
        closing = SharedPrefsUtils.getIntegerPreference(this, "closing", 0);
        custom = SharedPrefsUtils.getIntegerPreference(this, "custom", -1);
        uriCustom = SharedPrefsUtils.getStringPreference(this, "custom_uri");
    }

    private void showSettingPopup(boolean z) {
        initData();
        if (mediaPlayer != null && this.isSound) {
            mediaPlayer.setVolume(0.0f, 0.0f);
        }
        if (videoMediaPlayer != null && this.isSound) {
            videoMediaPlayer.setVolume(0.0f, 0.0f);
        }
        View inflate = getLayoutInflater().inflate(R.layout.settings_layout, null);
        imgDismiss = inflate.findViewById(R.id.imgBack);

        ToggleButton toggleButton = inflate.findViewById(R.id.switchLock);
        ToggleButton toggleButton2 = inflate.findViewById(R.id.switchSound);
        ToggleButton toggleButton3 = inflate.findViewById(R.id.switchPer);
        Spinner spinner = inflate.findViewById(R.id.spinnerDuration);
        RelativeLayout relativeLayout = inflate.findViewById(R.id.layoutSound);
        Spinner spinner2 = inflate.findViewById(R.id.spinnerClosingMethod);
        ToggleButton toggleButton4 = inflate.findViewById(R.id.switchShow);
        mainDialog = new Dialog(this);
        ArrayList arrayList = new ArrayList();
        arrayList.add("5 secs");
        arrayList.add("10 secs");
        arrayList.add("30 secs");
        arrayList.add("1 min");
        arrayList.add("loop");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.anim_spinner_item, arrayList);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("Double click");
        arrayList2.add("Single click");
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.anim_spinner_item, arrayList2);
        spinner.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter2);
        if (this.imgUri != null) {
            relativeLayout.setVisibility(View.GONE);
        }
        if (z) {
            toggleButton2.setChecked(this.isSound);
            toggleButton.setChecked(this.isLock);
            toggleButton3.setChecked(this.isPer);
            toggleButton4.setChecked(this.show);
            spinner2.setSelection(this.closing);
            spinner.setSelection(getSpinnerPos(this.duration));
        }
        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SharedPrefsUtils.setBooleanPreference(PreviewActivity.this, "sound", z);
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SharedPrefsUtils.setBooleanPreference(PreviewActivity.this, "lock", z);
            }
        });
        toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SharedPrefsUtils.setBooleanPreference(PreviewActivity.this, "per", z);
            }
        });
        toggleButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SharedPrefsUtils.setBooleanPreference(PreviewActivity.this, "show", z);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                PreviewActivity anim_PreviewActivity = PreviewActivity.this;
                SharedPrefsUtils.setIntegerPreference(anim_PreviewActivity, "duration", anim_PreviewActivity.getDuration(i));
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                SharedPrefsUtils.setIntegerPreference(PreviewActivity.this, "closing", i);
            }
        });
        mainDialog.requestWindowFeature(1);
        imgDismiss.setOnClickListener(view -> mainDialog.dismiss());

        mainDialog.getWindow().setBackgroundDrawableResource(17170445);
        int i = getResources().getDisplayMetrics().heightPixels;
        mainDialog.setContentView(inflate, new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, i));
        mainDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                setMediaPlayer();
            }
        });
        mainDialog.show();
    }


    public void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }


    public void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onPause();
    }
}
