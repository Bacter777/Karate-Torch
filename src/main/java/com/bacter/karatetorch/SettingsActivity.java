package com.bacter.karatetorch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.Objects;
import pl.droidsonroids.gif.GifImageView;

public class SettingsActivity extends AppCompatActivity
{
    SeekBar seekBar;
    TextView textView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button btnSave = findViewById(R.id.btnSave);
        seekBar = findViewById(R.id.skbThreshold);
        /* SOLVED APP FORCE CLOSED ON-CLICK */
        SharedPreferences pref = getApplicationContext().getSharedPreferences("appPref", Context.MODE_PRIVATE);
        float shakeThresHold = pref.getFloat("shakeThresHold",2.0f)*100f;
        seekBar.setProgress((int) shakeThresHold);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                textView.setText(new StringBuilder().append(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                float shakeThresHold = seekBar.getProgress()/100f;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("appPref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putFloat("shakeThresHold",shakeThresHold);
                editor.apply();
                Toast.makeText(getApplicationContext(),"SETTINGS SAVED",Toast.LENGTH_SHORT).show();
                saveSettings(view);
            }
        });
        textView = findViewById(R.id.tvThresholdValue);
        GifImageView imageView = findViewById(R.id.ivShakeGif);
        Glide.with(getApplicationContext())
                .load(R.drawable.karate_chop)
                .into(imageView);
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    public void saveSettings(View view)
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}