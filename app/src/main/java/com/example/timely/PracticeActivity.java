package com.example.timely;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.narayanacharya.waveview.WaveView;

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {
    boolean rec = false;
    WaveView wave;
    WaveView wave2;
    int duration = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        wave = findViewById(R.id.wave);
        wave2 = findViewById(R.id.wave2);
        wave.pause();
        wave2.pause();
        Button in = findViewById(R.id.recordInner);
        Button out = findViewById(R.id.recordOuter);
        in.setOnClickListener(this);
        out.setOnClickListener(this);
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
                } else {
                    wave.play();
                    wave2.play();
                }
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
