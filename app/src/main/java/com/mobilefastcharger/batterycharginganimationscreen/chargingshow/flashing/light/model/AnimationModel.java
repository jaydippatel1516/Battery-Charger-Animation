package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.model;

import android.net.Uri;

public class AnimationModel {
    public int audioRes;
    public int custom = -1;
    public int jsonRes;
    int imgRes;
    Uri uri;

    public AnimationModel(int i, Uri uri2) {
        this.custom = i;
        this.uri = uri2;
    }

    public AnimationModel(int i, int i2, int i3) {
        this.imgRes = i;
        this.jsonRes = i2;
        this.audioRes = i3;
    }

    public int getImgRes() {
        return this.imgRes;
    }

    public void setImgRes(int i) {
        this.imgRes = i;
    }

    public int getJsonRes() {
        return this.jsonRes;
    }

    public void setJsonRes(int i) {
        this.jsonRes = i;
    }

    public int getAudioRes() {
        return this.audioRes;
    }

    public int getCustom() {
        return this.custom;
    }

    public Uri getUri() {
        return this.uri;
    }
}
