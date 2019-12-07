package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import java.util.Iterator;
import java.util.UUID;

import dataStructures.FakeDatabase;
import dataStructures.GroupMember;
import dataStructures.NamedSegments;
import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Section;
import dataStructures.Utilities;
import dataStructures.VizSegments;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Presentation currentPresentation;
    private Disposable onEdit;
    private Disposable onReorder;
    private GroupRecyclerAdapter adapter;
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
        adapter = new GroupRecyclerAdapter(currentPresentation);
        users.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new DragHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(users);

        onEdit = adapter.onEdit().subscribe(new Consumer<GroupMember>() {
            @Override
            public void accept(GroupMember member) throws Exception {
                Intent intent = new Intent(GroupActivity.this, GroupEditMemberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", member);
                bundle.putString("presentationName", currentPresentation.name);
                bundle.putString("presentationDuration", currentPresentation.getRemainingTimeString());
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


        Button add = findViewById(R.id.group_add_member);
        add.setOnClickListener(this);

        createChart();
    }

    private void createChart() {
        LinearLayout glance = findViewById(R.id.group_glance);
        glance.removeAllViews();
        Utilities util = new Utilities(getApplicationContext());

        ArrayList<NamedSegments> args = new ArrayList<>();
        for (GroupMember s: currentPresentation.members) {
            args.add(new VizSegments(
                    s.memberName,
                    s.duration
            ));
        }
        Integer timeLeft = currentPresentation.getRemainingTime();
        boolean hasVacant = false;
        if (timeLeft > 0) {
            VizSegments viz = new VizSegments("EMPTY", timeLeft);
            viz.isVacant = true;
            args.add(viz);
            hasVacant = true;
        }
        util.setChart(glance, args, hasVacant);
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        createChart();
        ArrayList<NamedSegments> args = new ArrayList<>();
        int tot = 0;
        for (GroupMember s: currentPresentation.members) {
            args.add(new VizSegments(
                    s.memberName,
                    s.duration
            ));
            tot += s.duration;
        }
        currentPresentation.duration = tot;
        TextView total = findViewById(R.id.total_time);
        TextView name = findViewById(R.id.presentation_name);
        name.setText(currentPresentation.name);
        total.setText(Presentation.toStringTime(tot));
        LinearLayout glance = findViewById(R.id.group_glance);
        glance.removeAllViews();
        Utilities util = new Utilities(getApplicationContext());
        util.setChart(glance, args, false);
        currentPresentation.syncSections();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_add_member:
                Intent intent = new Intent(this, GroupEditMemberActivity.class);
                Bundle bundle = new Bundle();
                GroupMember activeMember = GroupMember.newInstance();
                activeMember.ownerID = currentPresentation.ownerID;
                bundle.putSerializable("data", activeMember);
                bundle.putString("presentationName", currentPresentation.name);
                bundle.putString("presentationDuration", currentPresentation.getRemainingTimeString());
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
        onReorder.dispose();
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

                        // need to change presentation type
                        boolean other = false;
                        for (GroupMember m: currentPresentation.members) {
                            if (!m.ownerID.equals(FakeDatabase.getInstance().currentUser.id)) {
                                other = true;
                                break;
                            }
                        }
                        Iterator<Section> iterator = currentPresentation.sections.iterator();
                        while (iterator.hasNext()) {
                            Section s = iterator.next();
                            if (s.userID.equals(passedMember.id)) {
                                iterator.remove();
                            }
                        }
                        if (!other) {
                            currentPresentation.type = PresentationType.INDIVIDUAL;
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case SAVE: {
                        // first need to change presentation type
                        currentPresentation.type = PresentationType.GROUP;
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
                            adapter.notifyItemChanged(i);
                        } else {
                            currentPresentation.members.add(passedMember);
                            adapter.notifyItemInserted(currentPresentation.members.size()-1);
                        }
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
                        "Help for Group Page",
                        "<b>Back button</b> Goes back to the previous page<br/><br/>" +
                                "<b>Add New Member\" button</b> Creates a new section<br/><br/>" +
                                "<b>Gear Icon</b> Change the setting of the chosen section<br/><br/>" +
                                "<b>Drag and drop</b> Click on the member’s name for 2 seconds and then drag to wanted position<br/><br/>"
                ).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
