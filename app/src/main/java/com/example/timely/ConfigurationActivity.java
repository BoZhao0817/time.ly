package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import dataStructures.FakeDatabase;
import dataStructures.NamedSegments;
import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Section;
import dataStructures.Utilities;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

enum FeedbackType {
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
        activePresentation = FakeDatabase.getInstance().findPresentation((UUID)(i.getSerializableExtra("presentationID")));

        Utilities utilities = new Utilities(getApplicationContext());
        TextView total_time = findViewById(R.id.total_time);
        LinearLayout linearLayout1=findViewById(R.id.ll1);
        Button add_section= findViewById(R.id.add_section);

        ArrayList<Section> list = new ArrayList<>();
        ArrayList<NamedSegments> array1 = new ArrayList<>();

        ArrayList<Section> currentSections;
        if (activePresentation.type == PresentationType.GROUP) {
            currentSections = activePresentation.getSectionsByUser(FakeDatabase.getInstance().currentUser.id);
        } else {
            currentSections = activePresentation.sections;
        }
        for (Section temp: currentSections) {
            list.add(temp);
            array1.add(temp);
        }

        utilities.setChart(linearLayout1, array1);

        RecyclerView recyclerView = findViewById(R.id.configure_sections_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ConfigurationSectionAdapter adapter = new ConfigurationSectionAdapter(currentSections);
        recyclerView.setAdapter(adapter);

        total_time.setText(activePresentation.getDurationString());
        add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigurationActivity.this, ConfigurationEditSectionActivity.class);
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
                bundle.putSerializable("data", section);
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
                switch ((FeedbackType)(bundle.get("actionType"))) {
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
