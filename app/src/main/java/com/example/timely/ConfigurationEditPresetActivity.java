package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import dataStructures.VibrationPattern;
import dataStructures.VibrationPatternType;

public class ConfigurationEditPresetActivity extends AppCompatActivity {
    private VibrationPattern pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_edit_preset);
        createActionBar();

        pattern = (VibrationPattern) (getIntent().getExtras().get("data"));
        EditText presetName = findViewById(R.id.configure_set_preset_name);
        presetName.setText(pattern.name);

        LinearLayout preset_display = findViewById(R.id.configure_display_preset);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (VibrationPatternType type: pattern.patterns){
            switch (type) {
                case LONG: {
                    preset_display.addView(inflater.inflate(R.layout.preset_rect_short, null));
                }
                case SHORT: {
                    preset_display.addView(inflater.inflate(R.layout.preset_rect_long, null));
                }
            }
        }
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
