package com.bacter.karatetorch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import pl.droidsonroids.gif.GifImageButton;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity
{
    private static final int TIPO_SENSOR = Sensor.TYPE_ACCELEROMETER;
    private int count = 0;
    boolean isTorchOn = false;
    private pl.droidsonroids.gif.GifImageButton button;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Boolean temFlash;
    private Boolean flashlightOn = true;
    private Boolean shakeServiceOn;
    private static final String SERVICE_STATE = "serviceState";
    private SmoothBottomBar bottomBar;
    private NotificationUtils mNotificationUtils;
    private Vibrator vibe;
    private MediaPlayer ml;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationUtils = new NotificationUtils(this);
        button = (GifImageButton)findViewById(R.id.button);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public boolean onItemSelect(int i)
            {
                switch (i)
                {
                    case 0:
                        startService(new Intent(MainActivity.this,MyService.class));
                        return true;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),Lamp.class));
                        return true;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        return true;
                    case 3:
                        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
                        return true;
                    case 4:
                        help();
                        return true;

                }
                return true;
            }
        });
        temFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(TIPO_SENSOR);
        if (savedInstanceState != null)
        {
            shakeServiceOn = savedInstanceState.getBoolean(SERVICE_STATE);
            if (shakeServiceOn)
            {
                button.setImageResource(R.drawable.karate_chop);

            }
            else {
                button.setImageResource(R.drawable.karate_chop);
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (MyService.running)
            {
                shakeServiceOn = false;
                button.setImageResource(R.drawable.karate_chop);
            }
            else{
                shakeServiceOn = true;
                button.setImageResource(R.drawable.karate_chop);
            }
        }
        if (sensor == null)
        {
            Toast.makeText(MainActivity.this,"Sensor Not Available",Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!temFlash)
        {
            Toast.makeText(MainActivity.this,"Your Device Does not Have A Flash!",Toast.LENGTH_SHORT).show();
            finish();
        }
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (shakeServiceOn)
                {
                    laser();
                    startService(new Intent(MainActivity.this,MyService.class));
                    Toast.makeText(MainActivity.this, "KARATE TORCH ACTIVATED", Toast.LENGTH_SHORT).show();
                    button.setImageResource(R.drawable.karate_chop);
                    shakeServiceOn = false;
                    Log.e("service","Service Started");
                }
                else {
                    laser();
                    stopService(new Intent(MainActivity.this,MyService.class));
                    Toast.makeText(MainActivity.this, "KARATE TORCH DEACTIVATED", Toast.LENGTH_SHORT).show();
                    button.setImageResource(R.drawable.karate_chop);
                    shakeServiceOn = true;
                    Log.e("service", "Service Has Stopped");
                }
            }
        });
    }
    public void laser()
    {
        ml = MediaPlayer.create(getApplicationContext(),R.raw.laser);
        if (ml != null)
        {
            ml.start();
        }
        else
            {
                ml.stop();
                ml.release();
            }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        count++;
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    if (count == 2) {
                        onFlashlight();
                        count = 0;
                    }
                }
            }, 500);
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean(SERVICE_STATE, shakeServiceOn);
        super.onSaveInstanceState(outState);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onFlashlight() {
        if (flashlightOn) {
            try {
                CameraManager cameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
                for (String id : cameraManager.getCameraIdList()) {

                    // Turn on the flash if camera has one
                    if (cameraManager.getCameraCharacteristics(id)
                            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE))
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            cameraManager.setTorchMode(id, true);

                        }
                        flashlightOn = false;
                    }
                }
            } catch (CameraAccessException e) {
                Log.e("tag", "Failed to interact with camera.", e);
                Toast.makeText(getApplicationContext(), "Torch Failed: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            try {
                CameraManager cameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
                for (String id : cameraManager.getCameraIdList()) {

                    // Turn on the flash if camera has one
                    if (cameraManager.getCameraCharacteristics(id)
                            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cameraManager.setTorchMode(id, false);

                        }
                        flashlightOn = true;
                    }
                }
            } catch (CameraAccessException e)
            {
                Log.e("tag", "Failed to interact with camera.", e);
                Toast.makeText(getApplicationContext(), "Torch Failed: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    public void help()
    {
        AlertDialog.Builder hlp = new AlertDialog.Builder(this);
        hlp.setTitle("How To Use Karate Torch");
        hlp.setCancelable(false);
        hlp.setMessage("1.Tap Karate Toch Icon to activate flashlight then shake your phone."
                + "\n"
                + "\n"
                + "\n"
                + "2.Open Settings, If you want to Adjust the Shake SENSITIVITY."
                + "\n"
                + "\n"
                + "\n"
                + "3.Added Screen Lamp"
                + "\n"
                + "\n"
                + "\n"
                + "4.Added Sound Effect"
                + "\n"
                + "\n"
                + "\n"
                + "Enjoy!");
        hlp.setNegativeButton("Dismiss", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        hlp.create().show();
    }
    public void Choose()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder((this));
        builder.setTitle("Keep Running: ");
        builder.setMessage("This Will Keep Karate Torch Running in Background");
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setNeutralButton("Keep Running", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface2, int dialogInterface)
            {
                NotificationCompat.Builder nb = mNotificationUtils.getAndroidChannelNotification("Karate Torch","Running");
                mNotificationUtils.getmManager().notify(101,nb.build());
                finish();
            }
        });
        builder.setPositiveButton("Minimize", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface2, int dialogInterface)
            {
                MainActivity.this.finish();
            }
        });
        builder.create().show();
    }
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler != null)
        {
            mHandler.removeCallbacks(mRunnable);
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }
    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this,"Please click BACK again to exit",Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable,2000);
    }
}