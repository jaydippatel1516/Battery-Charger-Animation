package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.R;
import com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.activities.MainActivity;

public class Notification_ extends Notification {
    public NotificationCompat.Builder builder;
    String CHANNEL_ID = "channel_name_battery";
    Context context;
    NotificationManager notificationManager;

    public Notification_(Context context2) {
        this.context = context2;
        Intent putExtra = new Intent(context2, MainActivity.class).putExtra("optimize", "yes");
        putExtra.setFlags(268468224);
        this.builder = new NotificationCompat.Builder(context2, this.CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Charging Animation").setContentText("App is running").setStyle(new NotificationCompat.BigTextStyle()).setAutoCancel(true).setPriority(1).setContentIntent(PendingIntent.getActivity(context2, 0, putExtra, PendingIntent.FLAG_IMMUTABLE));
        this.notificationManager = (NotificationManager) context2.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            this.notificationManager.createNotificationChannel(new NotificationChannel(this.CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_HIGH));
        }
    }

    public void updateStatus(String str) {
        this.builder.setContentTitle(str);
        Notification build = this.builder.build();
        build.flags |= 32;
        this.notificationManager.notify(1, build);
    }
}
