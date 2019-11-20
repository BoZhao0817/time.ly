package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.util.ArrayList;

import dataStructures.Presentation;
import dataStructures.Section;
import dataStructures.Utilities;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

enum ConfigurationFeedbackType {
    CANCEL, DELETE, SAVE
}

public class ConfigurationActivity extends AppCompatActivity {
    Presentation activePresentation;
    private Disposable listItemClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        createActionBar();

        Intent i = getIntent();
        activePresentation = (Presentation) i.getSerializableExtra("data");

        Utilities utilities = new Utilities(getApplicationContext());
        TextView total_time = findViewById(R.id.total_time);
        ListView lView = findViewById(R.id.listview);
        LinearLayout linearLayout1=findViewById(R.id.ll1);
        Button add_section= findViewById(R.id.add_section);

        ArrayList<Section> list = new ArrayList<>();
        ArrayList<Integer> array1=new ArrayList<>();

        for (Section temp: activePresentation.sections) {
            list.add(temp);
            array1.add(temp.duration);
        }

        ConfigurationSectionAdapter adapter = new ConfigurationSectionAdapter(list, this);
        utilities.setChart(linearLayout1,array1,320);
        lView.setAdapter(adapter);
        total_time.setText(activePresentation.getDurationString());
        add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConfigurationEditSectionActivity.class);
                Bundle bundle = new Bundle();
                Section activeSection = Section.newInstance();
                bundle.putSerializable("data", activeSection);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                // Do something
            }
        });
        listItemClicked = adapter.onEditClicked().subscribe(new Consumer<Section>() {
            @Override
            public void accept(Section section) throws Exception {
                Intent intent = new Intent(getApplicationContext(), ConfigurationEditSectionActivity.class);
                Bundle bundle = new Bundle();
                // deep copy
                Gson gson = new Gson();
                Section activeSection = gson.fromJson(gson.toJson(section), Section.class);
                bundle.putSerializable("data", activeSection);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent main_page = new Intent(this, MainActivity.class);
        startActivity(main_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                Section passedSection = (Section)(bundle.get("data"));
                switch ((ConfigurationFeedbackType)(bundle.get("actionType"))) {
                    case CANCEL: {
                        break;
                    }
                    case DELETE: {
                        int i = 0;
                        boolean found = false;
                        for (;i < activePresentation.sections.size(); i += 1) {
                            if (activePresentation.sections.get(i).id.equals(passedSection.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            activePresentation.sections.remove(i);
                        }
                    }
                    case SAVE: {
                        int i = 0;
                        boolean found = false;
                        for (;i < activePresentation.sections.size(); i += 1) {
                            if (activePresentation.sections.get(i).id.equals(passedSection.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            activePresentation.sections.set(i, passedSection);
                        } else {
                            activePresentation.sections.add(passedSection);
                        }
                    }
                }
            }
        }
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.icon_light_arrow_back);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.appBar)));
            actionBar.setTitle("Settings");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listItemClicked.dispose();
    }
}
