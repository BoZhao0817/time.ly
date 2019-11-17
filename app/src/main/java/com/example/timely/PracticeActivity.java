package com.example.timely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Intent i = getIntent();
        String timeString = i.getStringExtra("time");
        int seconds = StringToSeconds(timeString);
        TextView t = findViewById(R.id.timeView);
        t.setText(timeString);



    }

    protected int StringToSeconds(String s) {
        int colonCount = s.length() - s.replaceAll(":","").length();
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        if(colonCount == 2) { //Dealing with hours
            hours = Integer.parseInt(s.substring(0, 2));
            minutes = Integer.parseInt(s.substring(3, 5));
            seconds = Integer.parseInt(s.substring(6, 8));
        } else { // no hours
            minutes = Integer.parseInt(s.substring(0, 2));
            seconds = Integer.parseInt(s.substring(3, 5));
        }
        return (hours * 60 * 60) + (minutes * 60) + seconds;
    }

    protected String intToTiemString(int seconds) {
        int minutes = 0;
        int hours = 0;
        while(seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while(minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        String h, m, s;
        if (hours < 10) {
            h = "0" + hours;
        } else {
            h = Integer.toString(hours);
        }
        if (minutes < 10) {
            m = "0" + minutes;
        } else {
            m = Integer.toString(minutes);
        }
        if (seconds < 10) {
            s = "0" + seconds;
        } else {
            s = Integer.toString(seconds);
        }
        if (hours > 0) {
            return "" + h + ":" + m + ":" + s;
        } else {
            return m + ":" + s;
        }
    }
}
