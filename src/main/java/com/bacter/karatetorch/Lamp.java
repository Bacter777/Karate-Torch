package com.bacter.karatetorch;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

@SuppressLint("SetTextI18n")
public class Lamp extends Activity
{
    private View a;
    Context context;
    // Touch up and down position & screen brightness
    private float startX = -1;
    private float startY = -1;
    private int startBrightness = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pl.droidsonroids.gif.GifImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this::back2main);
        a = findViewById(R.id.m);
        a.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                h();
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean h)
    {
        super.onWindowFocusChanged(h);
        if (h)
        {
            s();
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void h()
    {
        a.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void s()
    {
        a.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
    public void back2main(View view)
    {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                startBrightness = getBrightness();
                break;
            case MotionEvent.ACTION_UP:
                startX = -1;
                startY = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                // Try to adjust screen brightness only when start position is valid
                if (startX < 0 || startY < 0)
                {
                    return false;
                }
                // Stop brightness adjusting when detect the scroll path is not vertical
                if (Math.abs(y - startY)> 50 && Math.abs(y - startY)< 2 * Math.abs(x - startX))
                {
                    startX = -1;
                    startY = -1;
                    return false;
                }
                // Scroll up (smaller y) increase the screen brightness
                float delta = startY - y;
                delta /= 2;
                adjustBrightness((int)delta);
                break;
        }
        return false;
    }
    // Get current screen brightness
    private int getBrightness()
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        return (int) (lp.screenBrightness * 255);
    }
    private void adjustBrightness(int delta)
    {
        int brightness = startBrightness + delta;
        if (brightness < 0) brightness = 0;
        if (brightness > 255) brightness = 255;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness * (1f / 255f);
        getWindow().setAttributes(lp);
    }
}
