package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dataStructures.FakeDatabase;
import dataStructures.NamedSegments;
import dataStructures.Presentation;
import dataStructures.Section;
import dataStructures.VizSegments;
import dataStructures.Utilities;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

enum GroupFeedbackType {
    CANCEL, DELETE, SAVE
}

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Presentation currentPresentation;
    private Disposable onEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        createActionBar();

        currentPresentation = (Presentation) (getIntent().getSerializableExtra("data"));

        Button addButton = findViewById(R.id.group_add_section);
        addButton.setOnClickListener(this);

        RecyclerView users = findViewById(R.id.group_members_list);
        GroupRecyclerAdapter adapter = new GroupRecyclerAdapter(currentPresentation);
        users.setAdapter(adapter);
        onEdit = adapter.onEdit().subscribe(new Consumer<Section>() {
            @Override
            public void accept(Section section) throws Exception {
                Intent intent = new Intent(GroupActivity.this, GroupAddMemberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", section);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        LinearLayout glance = findViewById(R.id.group_glance);
        Utilities util = new Utilities(getApplicationContext());

        ArrayList<NamedSegments> args = new ArrayList<>();
        for (Section s: currentPresentation.sections) {
            args.add(new VizSegments(
                    FakeDatabase.getInstance().findUser(s.userID).name,
                    s.duration
            ));
        }
        util.setChart(glance, args);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_add_section:
                Intent intent = new Intent(this, GroupAddMemberActivity.class);
                Bundle bundle = new Bundle();
                Section activeSection = Section.newInstance();
                bundle.putSerializable("data", activeSection);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onEdit.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.simple_app_bar, menu);
        return true;
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.icon_light_arrow_back);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.appBar)));
            actionBar.setTitle("Practice");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.show();
        }
    }
}
