package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dataStructures.Utilities;
import dataStructures.VibrationPattern;
import dataStructures.VibrationPatternType;

public class ConfigurationEditPresetActivity extends AppCompatActivity implements View.OnClickListener {
    private VibrationPattern pattern;
    private ConfigurationVibrationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_edit_preset);
        createActionBar();
        pattern = (VibrationPattern) (getIntent().getExtras().getSerializable("data"));
        final EditText presetName = findViewById(R.id.configure_set_preset_name);
        presetName.setText(pattern.name);

        RecyclerView recyclerView = findViewById(R.id.configure_display_preset);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ConfigurationVibrationAdapter(pattern.patterns);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new DragHelperCallback(adapter, false);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        Button longButton = findViewById(R.id.configure_long_button);
        Button shortButton = findViewById(R.id.configure_short_button);
        Button playButton = findViewById(R.id.configure_play_preset);
        Button deleteButton = findViewById(R.id.configure_delete_preset);
        playButton.setOnClickListener(this);
        longButton.setOnClickListener(this);
        shortButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.configure_short_button: {
                pattern.patterns.add(VibrationPatternType.SHORT);
                adapter.notifyItemInserted(pattern.patterns.size()-1);
                break;
            }
            case R.id.configure_long_button: {
                pattern.patterns.add(VibrationPatternType.LONG);
                adapter.notifyItemInserted(pattern.patterns.size()-1);
                break;
            }
            case R.id.configure_play_preset: {
                Utilities util = new Utilities(getApplicationContext());
                util.playVibration(pattern);
                break;
            }
            case R.id.configure_delete_preset : {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", pattern);
                bundle.putSerializable("actionType", FeedbackType.DELETE);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", pattern);
                bundle.putSerializable("actionType", FeedbackType.CANCEL);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            case R.id.save_button: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", pattern);
                bundle.putSerializable("actionType", FeedbackType.SAVE);
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
