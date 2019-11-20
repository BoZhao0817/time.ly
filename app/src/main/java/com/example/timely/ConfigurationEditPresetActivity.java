package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import dataStructures.Utilities;
import dataStructures.VibrationPattern;
import dataStructures.VibrationPatternType;

public class ConfigurationEditPresetActivity extends AppCompatActivity implements View.OnClickListener {
    private VibrationPattern pattern;
    private LinearLayout preset_display;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_edit_preset);
        createActionBar();
        pattern = (VibrationPattern) (getIntent().getExtras().get("data"));
        final EditText presetName = findViewById(R.id.configure_set_preset_name);
        presetName.setText(pattern.name);

        preset_display = findViewById(R.id.configure_display_preset);
        inflater = LayoutInflater.from(this);
        for (VibrationPatternType type: pattern.patterns){
            switch (type) {
                case LONG: {
                    View vw = inflater.inflate(R.layout.preset_rect_long, preset_display, false);
                    Log.d("MAKE", ""+vw.getId());
                    vw.setTag(0);
                    vw.setOnClickListener(this);
                    preset_display.addView(vw);
                }
                case SHORT: {
                    View vw = inflater.inflate(R.layout.preset_rect_short, preset_display, false);
                    Log.d("MAKE", ""+vw.getId());
                    vw.setTag(1);
                    vw.setOnClickListener(this);
                    preset_display.addView(vw);
                }
            }
        }

        Button longButton = findViewById(R.id.configure_long_button);
        Button shortButton = findViewById(R.id.configure_short_button);
        Button playButton = findViewById(R.id.configure_play_preset);
        playButton.setOnClickListener(this);
        longButton.setOnClickListener(this);
        shortButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.e("TAG", ""+v.getTag());
        if (v.getTag() != null) {
            if ((int) v.getTag() == 0) {
                preset_display.removeView(v);
            } else if ((int) v.getTag() == 1) {
                preset_display.removeView(v);
            }
        }
        switch(v.getId()) {
            case R.layout.preset_rect_short:
            case R.layout.preset_rect_long:
                Log.e("REMOVE", "");
                preset_display.removeView((View) (v.getParent()));
                preset_display.invalidate();
                break;
            case R.id.configure_long_button:
                View vw = inflater.inflate(R.layout.preset_rect_long, preset_display, false);
                Log.e("MAKE LONG", "" + vw.getId());
                vw.setOnClickListener(this);
                vw.setTag(0);
                preset_display.addView(vw);
                break;
            case R.id.configure_short_button:
                View vw2 = inflater.inflate(R.layout.preset_rect_short, preset_display, false);
                Log.e("MAKE SHORT", "" + vw2.getId());
                vw2.setOnClickListener(this);
                vw2.setTag(1);
                preset_display.addView(vw2);
            case R.id.configure_play_preset:
                Utilities util = new Utilities(getApplicationContext());
                util.playVibration(getPattern(preset_display));
                break;
        }
    }

    public VibrationPattern getPattern(LinearLayout ll) {
        VibrationPattern vp = new VibrationPattern(pattern.name);
        final int childCount = ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = ll.getChildAt(i);
            if ((int) v.getTag() == 0) {
                vp.patterns.add(VibrationPatternType.LONG);
            } else if ((int) v.getTag() == 1) {
                vp.patterns.add(VibrationPatternType.SHORT);
            }
        }
        return vp;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_section_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", pattern);
                bundle.putSerializable("actionType", ConfigurationFeedbackType.CANCEL);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            case R.id.save_button: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", pattern);
                bundle.putSerializable("actionType", ConfigurationFeedbackType.SAVE);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable cancel = ContextCompat.getDrawable(this, R.drawable.icon_close);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.appBar)));
            actionBar.setTitle("Edit Pattern");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(cancel);
            actionBar.show();
        }
    }
}
