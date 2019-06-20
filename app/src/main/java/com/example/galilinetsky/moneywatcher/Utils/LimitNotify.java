package com.example.galilinetsky.moneywatcher.Utils;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.galilinetsky.moneywatcher.R;

public class LimitNotify {
    private static final String CHANNEL_ID = "edmt.dev.androidnotificationchannel.EDMTDEV";
    private static final String CHANNEL_NAME = "EDMTDEV channel";
    private NotificationManager notificationManager;
    private Context context;
    public LimitNotify(Context con,NotificationManager manager){
        this.notificationManager = manager;
        this.context = con;
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.enableLights(true);
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setLimit(PendingIntent pendingIntent){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( this.context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_dollar)
                .setContentTitle("Expense Limit")
                .setContentText("you have passed your limit").setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("you have passed your limit"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1, mBuilder.build());
    }

}
