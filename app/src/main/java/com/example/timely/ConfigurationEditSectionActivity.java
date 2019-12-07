package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dataStructures.FakeDatabase;
import dataStructures.Presentation;
import dataStructures.Section;
import dataStructures.VibrationPattern;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ConfigurationEditSectionActivity extends AppCompatActivity implements View.OnClickListener {
    Section section;
    private Disposable onEditClicked;
    private Disposable onPatternSelected;
    private ConfigurationPresetAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_edit_section);
        createActionBar();

        Bundle bundle = getIntent().getExtras();
        section = (Section) (bundle.getSerializable("data"));

        ((TextView)findViewById(R.id.edit_section_remaining_time)).setText(bundle.getString("timeLeft"));

        final EditText section_name = findViewById(R.id.sec_name);
        final EditText section_duration = findViewById(R.id.sec_duration);
        final TextView selectedPattern = findViewById(R.id.selectedPattern);
        selectedPattern.setText(FakeDatabase.getInstance().findPattern(section.patternID).name);
        recyclerView = findViewById(R.id.listview2);

        section_name.setText(section.sectionName);
        section_duration.setText(section.getDurationString());

        Button add_button = findViewById(R.id.addPresetbutton);
        add_button.setOnClickListener(this);

        Button delete_button= findViewById(R.id.DeleteSection);
        delete_button.setOnClickListener(this);

        adapter = new ConfigurationPresetAdapter(this, section.patternID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        onEditClicked = adapter.onEditClicked().subscribe(new Consumer<VibrationPattern>() {
            @Override
            public void accept(VibrationPattern vibrationPattern) throws Exception {
                Intent intent = new Intent(ConfigurationEditSectionActivity.this, ConfigurationEditPresetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", vibrationPattern);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        onPatternSelected = adapter.onPatternSelected().subscribe(new Consumer<ConfigurationCheckedItem>() {
            @Override
            public void accept(ConfigurationCheckedItem checkedItem) throws Exception {
                selectedPattern.setText(checkedItem.pattern.name);
                section.patternID = checkedItem.pattern.id;
            }
        });
    }
    public static int toIntTime(String time) {
        int seconds = Integer.parseInt(time.split(":")[1]);
        int minutes = Integer.parseInt(time.split(":")[0]);
        return seconds+minutes*60;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPresetbutton: {
                Intent intent = new Intent(ConfigurationEditSectionActivity.this, ConfigurationEditPresetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", VibrationPattern.newInstance());
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.DeleteSection: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", section);
                bundle.putSerializable("actionType", FeedbackType.DELETE);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                VibrationPattern passedPattern = (VibrationPattern)(bundle.get("data"));
                ArrayList<VibrationPattern> allPatterns = FakeDatabase.getInstance().vibrationPatterns;
                switch ((FeedbackType)(bundle.get("actionType"))) {
                    case CANCEL: {
                        break;
                    }
                    case DELETE: {
                        int i = 0;
                        boolean found = false;
                        for (;i < allPatterns.size(); i += 1) {
                            if (allPatterns.get(i).id.equals(passedPattern.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            allPatterns.remove(i);
//                            adapter.notifyItemRemoved(i);

                            VibrationPattern active;
                            if (allPatterns.size() == 0) {
                                active = VibrationPattern.newInstance();
                                allPatterns.add(active);
//                                adapter.notifyItemInserted(0);
                            } else {
                                active = allPatterns.get(0);
                            }
                            // change all sections
                            for (Presentation p: FakeDatabase.getInstance().presentations) {
                                for (Section s: p.sections) {
                                    if (s.patternID.equals(passedPattern.id)) {
                                        s.patternID = active.id;
                                    }
                                }
                            }
                            if (section.patternID.equals(passedPattern.id)) {
                                section.patternID = active.id;
                            }
                            adapter.notifyDataSetChanged();
                        }

                        break;
                    }
                    case SAVE: {
                        int i = 0;
                        boolean found = false;
                        Log.e("SAVE", "Saved Section");
                        for (;i < allPatterns.size(); i += 1) {
                            if (allPatterns.get(i).id.equals(passedPattern.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            allPatterns.set(i, passedPattern);
                        } else {
                            allPatterns.add(passedPattern);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                refreshReyclerView();
            }
        }
    }

    private void refreshReyclerView() {
        final TextView selectedPattern = findViewById(R.id.selectedPattern);
        selectedPattern.setText(FakeDatabase.getInstance().findPattern(section.patternID).name);
        recyclerView = findViewById(R.id.listview2);

        Button add_button = findViewById(R.id.addPresetbutton);
        add_button.setOnClickListener(this);

        Button delete_button= findViewById(R.id.DeleteSection);
        delete_button.setOnClickListener(this);

        adapter = new ConfigurationPresetAdapter(this, section.patternID);
        recyclerView.swapAdapter(adapter, false);
        adapter.notifyDataSetChanged();

        onEditClicked.dispose();
        onEditClicked = adapter.onEditClicked().subscribe(new Consumer<VibrationPattern>() {
            @Override
            public void accept(VibrationPattern vibrationPattern) throws Exception {
                Intent intent = new Intent(ConfigurationEditSectionActivity.this, ConfigurationEditPresetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", vibrationPattern);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        onPatternSelected.dispose();
        onPatternSelected = adapter.onPatternSelected().subscribe(new Consumer<ConfigurationCheckedItem>() {
            @Override
            public void accept(ConfigurationCheckedItem checkedItem) throws Exception {
                selectedPattern.setText(checkedItem.pattern.name);
                section.patternID = checkedItem.pattern.id;
            }
        });
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
                bundle.putSerializable("data", section);
                bundle.putSerializable("actionType", FeedbackType.CANCEL);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            case R.id.save_button: {
                final EditText section_name = findViewById(R.id.sec_name);
                final EditText section_duration = findViewById(R.id.sec_duration);
                section.sectionName=section_name.getText().toString();
                section.duration=toIntTime(section_duration.getText().toString());
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", section);
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
            actionBar.setTitle("Edit Section");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(cancel);
            actionBar.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onPatternSelected.dispose();
        onEditClicked.dispose();
    }
}

