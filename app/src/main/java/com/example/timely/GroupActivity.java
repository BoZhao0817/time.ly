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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import dataStructures.FakeDatabase;
import dataStructures.GroupMember;
import dataStructures.NamedSegments;
import dataStructures.Presentation;
import dataStructures.Section;
import dataStructures.Utilities;
import dataStructures.VizSegments;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Presentation currentPresentation;
    private Disposable onEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        createActionBar();

        currentPresentation = FakeDatabase.getInstance().findPresentation((UUID)(getIntent().getSerializableExtra("presentationID")));

        Button addButton = findViewById(R.id.group_add_member);
        addButton.setOnClickListener(this);

        RecyclerView users = findViewById(R.id.group_members_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        users.setLayoutManager(layoutManager);
        GroupRecyclerAdapter adapter = new GroupRecyclerAdapter(currentPresentation);
        users.setAdapter(adapter);
        onEdit = adapter.onEdit().subscribe(new Consumer<GroupMember>() {
            @Override
            public void accept(GroupMember member) throws Exception {
                Intent intent = new Intent(GroupActivity.this, GroupEditMemberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", member);
                bundle.putString("presentationName", currentPresentation.name);
                bundle.putString("presentationDuration", currentPresentation.getDurationString());
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
            case R.id.group_add_member:
                Intent intent = new Intent(this, GroupEditMemberActivity.class);
                Bundle bundle = new Bundle();
                GroupMember activeMember = GroupMember.newInstance();
                bundle.putSerializable("data", activeMember);
                bundle.putString("presentationName", currentPresentation.name);
                bundle.putString("presentationDuration", currentPresentation.getDurationString());
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                GroupMember passedMember = (GroupMember)(bundle.get("data"));
                switch ((FeedbackType)(bundle.get("actionType"))) {
                    case CANCEL: {
                        break;
                    }
                    case DELETE: {
                        int i = 0;
                        boolean found = false;
                        for (;i < currentPresentation.members.size(); i += 1) {
                            if (currentPresentation.members.get(i).id.equals(passedMember.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            currentPresentation.members.remove(i);
                        }
                    }
                    case SAVE: {
                        int i = 0;
                        boolean found = false;
                        for (;i < currentPresentation.members.size(); i += 1) {
                            if (currentPresentation.members.get(i).id.equals(passedMember.id)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            currentPresentation.members.set(i, passedMember);
                        } else {
                            currentPresentation.members.add(passedMember);
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
            actionBar.setTitle("Practice");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.show();
        }
    }
}
