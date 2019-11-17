package com.example.timely;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.narayanacharya.waveview.WaveView;

public class PracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        WaveView wave = findViewById(R.id.wave);
        WaveView wave2 = findViewById(R.id.wave2);
        wave.play();
        wave2.play();
    }
}
