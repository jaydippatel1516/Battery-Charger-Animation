package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.adapters.ListAdapter;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.model.AnimationModel;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    public int anim;
    public int audio;
    public int closing;
    public int custom;
    public int duration;
    public boolean isLock;
    public boolean isSound;
    ListAdapter animListAdapter;
    ArrayList<AnimationModel> animationModels = new ArrayList<>();
    ImageView imgVideo;
    RecyclerView rcAnims;
    Toolbar toolbar;
    String uriCustom;
    String[] strings = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    String[] per13 = {Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.RECORD_AUDIO};


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_animation_list_activtiy);
        getWindow().setFlags(1024, 1024);
        initView();
        toolbarSetup();
        loadData();
        imgVideo.setOnClickListener(v -> requestPermission(1));

    }


    public void requestPermission(final int i) {

        Dexter.withActivity(ListActivity.this)
                .withPermissions(Build.VERSION.SDK_INT >= 33 ? per13 : strings)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (i == 1) {
                            Intent intent = new Intent("android.intent.action.PICK", MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("video/*");
                            intent.addFlags(1);
                            intent.addFlags(64);
                            intent.setAction("android.intent.action.OPEN_DOCUMENT");
                            intent.addCategory("android.intent.category.OPENABLE");
                            startActivityForResult(intent, 1);
                        }
                        if (i == 2) {
                            pickImage();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                })
                .onSameThread().check();


    }


    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1 && (data = intent.getData()) != null) {
            try {
                getContentResolver().takePersistableUriPermission(intent.getData(), intent.getFlags() & 3);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            Intent intent2 = new Intent(ListActivity.this, PreviewActivity.class);
            intent2.putExtra("uri", data.toString());
            startActivity(intent2);
        }
    }

    private void initView() {
        this.rcAnims = findViewById(R.id.rcAnimations);
        this.imgVideo = findViewById(R.id.imgVideo);
        this.toolbar = findViewById(R.id.layoutActionBar);
    }

    private void loadData() {
        initData();
        this.animationModels.clear();
        int i = this.custom;
        if (i != -1) {
            this.animationModels.add(new AnimationModel(i, Uri.parse(this.uriCustom)));
        }
        animationModels.add(new AnimationModel(R.drawable.gif1, R.raw.anim3, R.raw.music3_new));
        animationModels.add(new AnimationModel(R.drawable.gif2, R.raw.charging, R.raw.music1));
        animationModels.add(new AnimationModel(R.drawable.gif3, R.raw.anim13, R.raw.music2));
        animationModels.add(new AnimationModel(R.drawable.gif4, R.raw.anim4, R.raw.music4));
        animationModels.add(new AnimationModel(R.drawable.gif5, R.raw.anim5, R.raw.music5_new));
        animationModels.add(new AnimationModel(R.drawable.gif6, R.raw.anim6, R.raw.music6));
        animationModels.add(new AnimationModel(R.drawable.gif7, R.raw.anim7, R.raw.music7_new));
        animationModels.add(new AnimationModel(R.drawable.gif8, R.raw.anim10, R.raw.music8));
        animationModels.add(new AnimationModel(R.drawable.gif9, R.raw.anim11, R.raw.music9));
        animationModels.add(new AnimationModel(R.drawable.gif10, R.raw.anim12, R.raw.music10));
        animationModels.add(new AnimationModel(R.drawable.gif11, R.raw.anim14, R.raw.music11));
        animationModels.add(new AnimationModel(R.drawable.gif12, R.raw.anim15, R.raw.music12));
        animationModels.add(new AnimationModel(R.drawable.gif13, R.raw.anim_16_new, R.raw.music13));
        animationModels.add(new AnimationModel(R.drawable.gif14, R.raw.anim_17_new, R.raw.music14));
        animationModels.add(new AnimationModel(R.drawable.gif15, R.raw.anim18, R.raw.music15));
        animationModels.add(new AnimationModel(R.drawable.gif16, R.raw.anim19, R.raw.music16));
        animationModels.add(new AnimationModel(R.drawable.gif17, R.raw.anim20, R.raw.music17));
        animationModels.add(new AnimationModel(R.drawable.gif18, R.raw.anim21, R.raw.audio21));
        animationModels.add(new AnimationModel(R.drawable.gif19, R.raw.anim22, R.raw.audio22));
        animationModels.add(new AnimationModel(R.drawable.gif20, R.raw.anim23, R.raw.audio23));
        animListAdapter = new ListAdapter(this, this.animationModels);
        rcAnims.setLayoutManager(new GridLayoutManager(this, 2));
        rcAnims.setAdapter(this.animListAdapter);
    }

    private void toolbarSetup() {
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }


    public void onResume() {
        super.onResume();
        loadData();
        if (animListAdapter != null) {
            animListAdapter.notifyDataSetChanged();
        }
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
    }
}
