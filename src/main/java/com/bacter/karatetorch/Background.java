package com.bacter.karatetorch;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Background extends Service
{
    public Notification noti;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public void onCreate()
    {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity((this),(0),(intent), (0));
        noti = new Notification();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(false);
        builder.setTicker("Karate Torch");
        builder.setContentText("Running");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pi);
        builder.setNumber(100);
        builder.build();
        startForeground(1,noti);
    }
    @Override
    public void onDestroy()
    {
        stopForeground(true);
        super.onDestroy();
    }
}
