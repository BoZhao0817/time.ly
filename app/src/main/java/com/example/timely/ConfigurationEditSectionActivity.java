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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import dataStructures.FakeDatabase;
import dataStructures.Section;
import dataStructures.VibrationPattern;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ConfigurationEditSectionActivity extends AppCompatActivity {
    Section section;
    private Disposable onEditClicked;
    private Disposable onPatternSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_edit_section);
        createActionBar();

        section = (Section) (getIntent().getExtras().get("data"));

        final EditText section_name = findViewById(R.id.sec_name);
        final EditText section_duration = findViewById(R.id.sec_duration);
        final TextView selectedPattern = findViewById(R.id.selectedPattern);
        final RecyclerView recyclerView = findViewById(R.id.listview2);

        section_name.setText(section.sectionName);
        section_duration.setText(section.getDurationString());
        Button delete_button= findViewById(R.id.DeleteSection);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent go_back = new Intent(getApplicationContext(), ConfigurationActivity.class);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", section);
                bundle.putSerializable("actionType", ConfigurationFeedbackType.DELETE);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final ConfigurationPresetAdapter adapter = new ConfigurationPresetAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        onEditClicked = adapter.onEditClicked().subscribe(new Consumer<VibrationPattern>() {
            @Override
            public void accept(VibrationPattern vibrationPattern) throws Exception {
                Intent intent = new Intent(ConfigurationEditSectionActivity.this, ConfigurationEditPresetActivity.class);
                Bundle bundle = new Bundle();
                // deep copy
                Gson gson = new Gson();
                VibrationPattern copiedPattern = gson.fromJson(gson.toJson(vibrationPattern), VibrationPattern.class);
                bundle.putSerializable("data", copiedPattern);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        onPatternSelected = adapter.onPatternSelected().subscribe(new Consumer<ConfigurationCheckedItem>() {
            @Override
            public void accept(ConfigurationCheckedItem checkedItem) throws Exception {
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    View v = recyclerView.getChildAt(i);
                    ImageButton button = v.findViewById(R.id.checkButton);
                    if (button.getId() == checkedItem.elementID) {
                        button.setImageResource(R.drawable.icon_check_circle_orange);
                        selectedPattern.setText(checkedItem.pattern.name);
                    } else {
                        button.setImageResource(R.drawable.icon_check_circle_light);
                    }
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                VibrationPattern passedPattern = (VibrationPattern)(bundle.get("data"));
                ArrayList<VibrationPattern> allPatterns = FakeDatabase.getInstance().vibrationPatterns;
                switch ((ConfigurationFeedbackType)(bundle.get("actionType"))) {
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
                            // TODO: Need to change associated sections as well
                        }
                    }
                    case SAVE: {
                        int i = 0;
                        boolean found = false;
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
                    }
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
                bundle.putSerializable("data", section);
                bundle.putSerializable("actionType", ConfigurationFeedbackType.CANCEL);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            case R.id.save_button: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", section);
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

