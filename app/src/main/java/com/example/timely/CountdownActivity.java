package com.example.timely;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.UUID;

import dataStructures.FakeDatabase;
import dataStructures.Presentation;
import dataStructures.Section;
import dataStructures.State;
import dataStructures.Timer;
import dataStructures.VibrationPattern;
import dataStructures.VibrationPatternType;

public class CountdownActivity extends AppCompatActivity implements View.OnClickListener {
    private Presentation presentation;
    boolean finished = false;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        Intent i = getIntent();
        presentation = FakeDatabase.getInstance().findPresentation((UUID)(i.getSerializableExtra("presentationID")));

        this.createActionBar();
        this.initializeText(0);
        timer = new Timer(presentation.sections.get(0).duration);
        final Handler handler = new Handler();
        final int delay = 1000;
        final TextView timeView = findViewById(R.id.countdown_current_time);
        ImageButton play = findViewById(R.id.playButton);
        ImageButton pause = findViewById(R.id.pauseButton);
        ImageButton stop = findViewById(R.id.stopButton);
        ImageButton next = findViewById(R.id.nextButton);
        final ProgressBar progress = findViewById(R.id.progressBar);
        progress.setMax(timer.seconds);
        progress.setProgress(0);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.updateTimer();
                timeView.setText(timer.timeString);
                progress.setProgress(progress.getMax() - timer.seconds);
                if(timer.done) {
                    if (timer.count < presentation.sections.size()) {
                        Section section = presentation.sections.get(timer.count);
                        vibrate(presentation.sections.get(timer.count-1));
                        timer.seconds = section.duration;
                        progress.setMax(timer.seconds);
                        progress.setProgress(0);
                        timer.done = false;
                        initializeText(timer.count);
                    } else {
                        vibrate(presentation.sections.get(timer.count-1));
                        finished = true;
                    }
                }
                if (!finished) {
                    handler.postDelayed(this, delay);
                } else {

                }
            }
        }, delay);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "DESTROYED");
        timer.state = State.PAUSED;
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LIFECYCLE", "STOPPED");
    }
    private void vibrate(Section section) {
        long shortDuration = 300L;
        long longDuration = 700L;
        long delay = 200L;
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        VibrationPattern currentVibration = FakeDatabase.getInstance().findPattern(section.patternID);
        long[] pattern = new long[currentVibration.patterns.size()*2];
        pattern[0] = 0;
        for (int i = 1; i <= currentVibration.patterns.size(); i++) {
            VibrationPatternType vp = currentVibration.patterns.get(i-1);
            if (vp == VibrationPatternType.LONG) {
                pattern[2 * i - 1] = shortDuration;
                if (i != currentVibration.patterns.size())
                    pattern[2 * i] = delay;
            } else {
                pattern[2 * i - 1] = longDuration;
                if (i != currentVibration.patterns.size())
                    pattern[2 * i] = delay;
            }
        }
        v.vibrate(VibrationEffect.createWaveform(pattern, -1));
    }
    private void initializeText(int curr) {
        if (presentation != null) {
            TextView t = findViewById(R.id.countdown_current_section);
            t.setText(presentation.sections.get(curr).sectionName);
            TextView time = findViewById(R.id.countdown_current_time);
            time.setText(Presentation.toStringTime(presentation.sections.get(curr).duration));
            if (presentation.sections.size() > curr + 1) {
                TextView nextSection = findViewById(R.id.countdown_next_section);
                String str = "Next: " + presentation.sections.get(curr+1).sectionName;
                nextSection.setText(str);
            } else {
                TextView nextSection = findViewById(R.id.countdown_next_section);
                String str = "Next: End of Presentation";
                nextSection.setText(str);
            }
        }
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.icon_blue_arrow_back);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.frontLayer)));
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.show();
        }
    }

    // this method is necessary for appbar's back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("KEY", ""+keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case 1082:
                if (timer.state == State.PAUSED)
                    timer.state = State.PLAYING;
                else
                    timer.state = State.PAUSED;
                break;
            default:
                break;
        }
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pauseButton:
                timer.state = State.PAUSED;
                break;
            case R.id.playButton:
                timer.state = State.PLAYING;
                break;
            case R.id.stopButton:
                finished = true;
            case R.id.nextButton:
                timer.count++;
                timer.done = true;
            default:
                break;
        }
    }
}
