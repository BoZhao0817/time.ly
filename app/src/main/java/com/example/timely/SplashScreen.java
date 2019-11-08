package com.example.timely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Make Database/Network Calls and Transfer to Intent

        // For now, just a timer and send to next activity
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        Intent mainScreen = new Intent(this, MainActivity.class);
        startActivity(mainScreen);

    }
}
