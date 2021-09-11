package com.bacter.karatetorch;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

@SuppressLint("UnspecifiedImmutableFlag")
public class NotificationUtils extends ContextWrapper
{
    private NotificationManager mManager;
    private static final int NOTIFICATION_ID = 1;
    public static final String ANDROID_CHANNEL_ID = "com.bacter.karatetorch.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    NotificationCompat.Builder builder;

    public NotificationUtils(Context base)
    {
        super(base);
        createChannels();
    }
    public void createChannels()
    {
        NotificationChannel androidChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getmManager().createNotificationChannel(androidChannel);

        }
    }
    public NotificationCompat.Builder getAndroidChannelNotification(String title, String body)
    {
        Intent intent = new Intent(this,(MainActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pi = PendingIntent.getActivity((this),(0),(intent),(0));
            return new NotificationCompat.Builder(getApplicationContext(),ANDROID_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pi)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(false);
        }
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
            builder = new NotificationCompat.Builder(this,ANDROID_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(101,builder.build());
        }
        return builder;
    }
    public NotificationManager getmManager()
    {
        if (mManager == null)
        {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}
