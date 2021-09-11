package com.bacter.karatetorch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.shashank.sony.fancyaboutpagelib.FancyAboutPage;

public class AboutActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        about();
    }
    public void about()
    {
        FancyAboutPage fancyAboutPage = findViewById(R.id.fancyAboutPage);
        fancyAboutPage.setCover(R.drawable.coverimg);
        fancyAboutPage.setName("BdFreak777");
        fancyAboutPage.setDescription("Mobile App Developer");
        fancyAboutPage.setAppIcon(R.mipmap.ic_launcher_round);
        fancyAboutPage.setAppName("Karate Torch");
        fancyAboutPage.addEmailLink("bdfreak777@gmail.com");
        fancyAboutPage.addFacebookLink("https://www.facebook.com/BdFreak777");
        fancyAboutPage.addGitHubLink("https://www.github.com/Bacter777");
        fancyAboutPage.setAppDescription("Karate Torch provides a simple gesture to make easier to use your phone flashlight by shaking it");
    }
}