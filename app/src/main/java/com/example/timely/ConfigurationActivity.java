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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import dataStructures.VizSegments;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

enum FeedbackType {
    CANCEL, DELETE, SAVE
}

public class ConfigurationActivity extends AppCompatActivity {
    Presentation activePresentation;
    private Disposable listItemClicked;
    private Disposable onReorder;
    Utilities utilities;
    ArrayList<Section> currentSections;
    ConfigurationSectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        createActionBar();

        Intent i = getIntent();
        activePresentation = FakeDatabase.getInstance().findPresentation((UUID)(i.getSerializableExtra("presentationID")));

        utilities = new Utilities(getApplicationContext());
        TextView total_time = findViewById(R.id.total_time);
        TextView name = findViewById(R.id.presentation_name);
        name.setText(activePresentation.name);
        name.setText(activePresentation.name);
        Button add_section= findViewById(R.id.add_section);

        if (activePresentation.type == PresentationType.GROUP) {
            currentSections = activePresentation.getSectionsByUser(FakeDatabase.getInstance().currentUser.name);
        } else {
            currentSections = activePresentation.sections;
        }

        createChart();

        RecyclerView recyclerView = findViewById(R.id.configure_sections_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ConfigurationSectionAdapter(currentSections);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new DragHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        total_time.setText(activePresentation.getDurationString());
        add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigurationActivity.this, ConfigurationEditSectionActivity.class);
                Bundle bundle = new Bundle();
                Section activeSection = Section.newInstance();
                bundle.putSerializable("data", activeSection);
                bundle.putString("timeLeft", activePresentation.getRemainingTimeString());
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
                bundle.putString("timeLeft", activePresentation.getRemainingTimeString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        onReorder = adapter.onReorder().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                createChart();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        Intent main_page = new Intent(this, MainActivity.class);
        startActivity(main_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.simple_app_bar, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                Section passedSection = (Section)(bundle.get("data"));
                Log.v("deb1",bundle.get("actionType").toString());
                switch ((FeedbackType)(bundle.get("actionType"))) {
                    case CANCEL: {
                        break;
                    }
                    case DELETE: {
                        int i = 0;
                        boolean found = false;
                        for (;i < currentSections.size(); i += 1) {
                            if (currentSections.get(i).id.equals(passedSection.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            currentSections.remove(i);
                            adapter.notifyItemRemoved(i);
                            adapter.notifyItemRangeChanged(i,currentSections.size());
                        }
                        createChart();
                        break;
                    }
                    case SAVE: {
                        int i = 0;
                        boolean found = false;

                        for (;i < currentSections.size(); i += 1) {
                            if (currentSections.get(i).id.equals(passedSection.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            currentSections.set(i, passedSection);
                            adapter.notifyItemChanged(i);
                        } else {
                            currentSections.add(passedSection);
                            adapter.notifyItemInserted(currentSections.size()-1);
                        }
                        createChart();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_help: {
                new HelperDialogActivity(
                        this,
                        "Help for Settings Page",
                        "<b>Back button</b> Goes back to the previous page<br/><br/>" +
                                "<b>\"Add New Section\" button</b> Creates a new section<br/><br/>" +
                                "<b>Gear Icon</b> Change the setting of the chosen section<br/><br/>" +
                                "<b>Drag and drop</b> Click on section name for 2 seconds and then drag to wanted position<br/><br/>"
                ).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void createChart() {
        LinearLayout linearLayout1 = findViewById(R.id.ll1);
        ArrayList<NamedSegments>array1 = new ArrayList<>();
        linearLayout1.removeAllViews();
        array1.addAll(currentSections);
        Integer timeLeft = activePresentation.getRemainingTime();
        boolean hasVacant = false;
        if (timeLeft > 0) {
            array1.add(new VizSegments("EMPTY", timeLeft));
            hasVacant = true;
        }
        utilities.setChart(linearLayout1, array1, hasVacant);
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
        onReorder.dispose();
    }
}
