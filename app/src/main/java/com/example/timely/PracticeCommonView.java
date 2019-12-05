package com.example.timely;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.narayanacharya.waveview.WaveView;

import dataStructures.Presentation;
import dataStructures.Report;
import dataStructures.State;
import dataStructures.Timer;

public abstract class PracticeCommonView extends Fragment implements View.OnClickListener {
    boolean rec = false;
    int duration = 1000;
    Timer timer;
    boolean finished = false;
    int delay = 1000;
    String currentSectionName;

    Presentation presentation;
    Report report;
    View root;


    TextView section;
    TextView target;
    TextView time;
    WaveView wave;
    WaveView wave2;
    TextView name;
    float amp = 0;
    protected abstract void toReport();
    protected abstract void onStarted();

    protected void init (final View root, final Presentation presentation, final Report report) {
        this.presentation = presentation;
        this.report = report;
        this.root = root;

        name = root.findViewById(R.id.practice_backdrop_recording_name);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                report.name = v.getText().toString();
                return true;
            }
        });
        wave = root.findViewById(R.id.wave);
        wave2 = root.findViewById(R.id.wave2);
        amp = wave.getAmplitude();
        wave.setAmplitude(0);
        wave2.setAmplitude(0);
        wave.pause();
        wave2.pause();
        Button in = root.findViewById(R.id.recordInner);
        Button out = root.findViewById(R.id.recordOuter);
        ImageButton next = root.findViewById(R.id.nextButton);
        section = root.findViewById(R.id.countdown_current_section);
        target = root.findViewById(R.id.practice_current_target);
        time = root.findViewById(R.id.countdown_current_time);
        initializeText(0);
        in.setOnClickListener(this);
        out.setOnClickListener(this);
        next.setOnClickListener(this);
        final Handler handler = new Handler();
        timer = new Timer(0);
        timer.state = State.PAUSED;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.tickUp();
                time.setText(timer.timeString);
                if (timer.done) {
                    if (timer.count < presentation.sections.size()) {
                        timer.seconds = 0;
                        timer.done = false;
                        timer.timeString = "00:00";
                        initializeText(timer.count);
                    } else {
                        finished = true;
                    }
                }
                if (!finished) {
                    handler.postDelayed(this, delay);
                } else {
                    toReport();
                }
            }
        }, delay);
    }

    private void initializeText(int curr) {
        if (presentation != null) {
            section.setText(presentation.sections.get(curr).sectionName);
            target.setText(Presentation.toStringTime(presentation.sections.get(curr).duration));
            time.setText("00:00");
            this.currentSectionName = presentation.sections.get(curr).sectionName;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.recordOuter:
            case R.id.recordInner:
                animate();
                onStarted();
                if(wave.isPlaying()) {
                    wave.setAmplitude(0);
                    wave2.setAmplitude(0);
                    wave.pause();
                    wave2.pause();
                    timer.state = State.PAUSED;
                } else {
                    wave.setAmplitude(amp);
                    wave2.setAmplitude(amp);
                    wave.play();
                    wave2.play();
                    timer.state = State.PLAYING;
                }
                break;
            case R.id.nextButton:
                timer.count++;
                timer.done = true;
                report.addEstimate(currentSectionName, timer.seconds);
                break;
            default:
                break;
        }
    }

    private void animate() {
        if (!rec) {
            Button in = root.findViewById(R.id.recordInner);
            Button out = root.findViewById(R.id.recordOuter);
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
            Button in = root.findViewById(R.id.recordInner);
            Button out = root.findViewById(R.id.recordOuter);
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
