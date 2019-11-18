package com.example.timely;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.narayanacharya.waveview.WaveView;

import org.w3c.dom.Text;

import dataStructures.Presentation;
import dataStructures.State;
import dataStructures.Timer;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {
    boolean rec = false;
    WaveView wave;
    WaveView wave2;
    int duration = 1000;
    TextView section;
    TextView target;
    TextView time;
    Presentation presentation;
    Timer timer;
    boolean finished = false;
    int delay = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Intent i = getIntent();
        presentation = (Presentation) i.getSerializableExtra("data");
        initializeText(0);
        wave = findViewById(R.id.wave);
        wave2 = findViewById(R.id.wave2);
        wave.pause();
        wave2.pause();
        Button in = findViewById(R.id.recordInner);
        Button out = findViewById(R.id.recordOuter);
        ImageButton next = findViewById(R.id.nextButton);
        section = findViewById(R.id.countdown_current_section);
        target = findViewById(R.id.countdown_current_target);
        time = findViewById(R.id.countdown_current_time);
        in.setOnClickListener(this);
        out.setOnClickListener(this);
        next.setOnClickListener(this);
        final Handler handler = new Handler();
        timer = new Timer(0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.tickUp();
                time.setText(timer.timeString);
                if(timer.done) {
                    if (timer.count < presentation.sections.size()) {
                        timer.seconds = presentation.sections.get(timer.count).duration;
                        timer.done = false;
                        initializeText(timer.count);
                    } else {
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

    private void initializeText(int curr) {
        if (presentation != null) {
            section.setText(presentation.sections.get(curr).sectionName);
            target.setText(Presentation.toStringTime(presentation.sections.get(curr).duration));
            time.setText("0:00");
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.recordOuter:
            case R.id.recordInner:
                animate();
                if(wave.isPlaying()) {
                    wave.pause();
                    wave2.pause();
                    timer.state = State.PAUSED;
                } else {
                    wave.play();
                    wave2.play();
                    timer.state = State.PLAYING;
                }
                break;
            case R.id.nextButton:
                timer.count++;
                timer.done = true;
                break;
            default:
                break;
        }
    }

    private void animate() {
        if (!rec) {
            Button in = findViewById(R.id.recordInner);
            Button out = findViewById(R.id.recordOuter);
            final float fromRadius = 10;
            final float toRadius = 1000;
            final GradientDrawable gd = (GradientDrawable) in.getBackground();
            final ValueAnimator animator = ValueAnimator.ofFloat(toRadius, fromRadius);
            animator.setDuration(duration)
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            gd.setCornerRadius(value);
                        }
                    });

            final GradientDrawable gd2 = (GradientDrawable) out.getBackground();
            final ValueAnimator animator2 = ValueAnimator.ofFloat(toRadius, fromRadius);
            animator2.setDuration(duration)
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            gd2.setCornerRadius(value);
                        }
                    });
            animator.start();
            animator2.start();
            rec = true;
        } else {
            Button in = findViewById(R.id.recordInner);
            Button out = findViewById(R.id.recordOuter);
            final float fromRadius = 10;
            final float toRadius = 1000;
            final GradientDrawable gd = (GradientDrawable) in.getBackground();
            final ValueAnimator animator = ValueAnimator.ofFloat(fromRadius, toRadius);
            animator.setDuration(duration)
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            gd.setCornerRadius(value);
                        }
                    });

            final GradientDrawable gd2 = (GradientDrawable) out.getBackground();
            final ValueAnimator animator2 = ValueAnimator.ofFloat(fromRadius, toRadius);
            animator2.setDuration(duration)
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            gd2.setCornerRadius(value);
                        }
                    });
            animator.start();
            animator2.start();
            rec = false;
        }
    }
}
