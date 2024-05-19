package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities.PreviewActivity;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.model.AnimationModel;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.utils.SharedPrefsUtils;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public boolean isLoad = false;
    ArrayList<AnimationModel> anims;
    int checked = -1;
    Context context;
    int raw;

    public ListAdapter(Context context2, ArrayList<AnimationModel> arrayList) {
        this.context = context2;
        this.anims = arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.animation_row, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final AnimationModel anim = this.anims.get(i);
        this.raw = SharedPrefsUtils.getIntegerPreference(this.context, "anim_id", R.raw.anim3);
        if (this.anims.get(i).getJsonRes() == this.raw) {
            this.checked = i;
        }
        if (anim.getCustom() != -1) {
            this.checked = 0;
        }
        if (this.checked == i) {
            viewHolder.imgChecked.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgChecked.setVisibility(View.GONE);
        }
        Log.d("custom", anim.getCustom() + " " + i);
        if (anim.getCustom() == -1 || anim.getUri() == null) {
            if (i == this.anims.size() - 1) {
                viewHolder.gifImageView.setPadding(0, 0, 0, 0);
                viewHolder.gifImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            viewHolder.gifImageView.setVisibility(View.VISIBLE);
            viewHolder.imgAnimation.setVisibility(View.GONE);
            Glide.with(this.context).load(Integer.valueOf(anim.getImgRes())).into(viewHolder.gifImageView);
            viewHolder.gifImageView.setOnClickListener(view -> {
                if (anim.getCustom() == -1) {
                    context.startActivity(new Intent(context, PreviewActivity.class).putExtra("raw", anim.getJsonRes()).putExtra("audio", anim.getAudioRes()));
                } else if (anim.getCustom() == 0) {
                    Intent intent = new Intent(context, PreviewActivity.class);
                    intent.putExtra("uri", anim.getUri().toString());
                    context.startActivity(intent);
                } else {
                    Intent intent2 = new Intent(context, PreviewActivity.class);
                    intent2.putExtra("img_uri", anim.getUri().toString());
                    context.startActivity(intent2);
                }
            });
            return;
        }
        viewHolder.imgAnimation.setVisibility(View.VISIBLE);
        viewHolder.animationView.setVisibility(View.GONE);
        viewHolder.gifImageView.setVisibility(View.GONE);
        Glide.with(context).load(anim.getUri()).into(viewHolder.imgAnimation);
        viewHolder.imgAnimation.setOnClickListener(view -> {
            if (anim.getCustom() == -1) {
                isLoad = false;
                context.startActivity(new Intent(context, PreviewActivity.class).putExtra("raw", anim.getJsonRes()).putExtra("audio", anim.getAudioRes()));
            } else if (anim.getCustom() == 0) {
                Intent intent = new Intent(context, PreviewActivity.class);
                intent.putExtra("uri", anim.getUri().toString());
                context.startActivity(intent);
            } else {
                Intent intent2 = new Intent(context, PreviewActivity.class);
                intent2.putExtra("img_uri", anim.getUri().toString());
                context.startActivity(intent2);
            }
        });
    }

    public int getItemCount() {
        return this.anims.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView animationView;
        ImageView gifImageView;
        ImageView imgAnimation;
        ImageView imgChecked;

        public ViewHolder(View view) {
            super(view);
            imgAnimation = view.findViewById(R.id.imgAnimation);
            imgChecked = view.findViewById(R.id.imgDone);
            animationView = view.findViewById(R.id.animationView);
            gifImageView = view.findViewById(R.id.gifView);
        }
    }
}
