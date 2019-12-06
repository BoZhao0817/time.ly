package com.example.timely;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.UUID;

import dataStructures.FakeDatabase;
import dataStructures.GroupMember;
import dataStructures.Invitation;
import dataStructures.InvitationType;
import dataStructures.Presentation;
import dataStructures.Section;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class InviteActivity extends AppCompatActivity {
    int count = FakeDatabase.getInstance().invitations.size();

    private InviteRecyclerAdapter adapter;
    private Disposable onAccept;
    private Disposable onDecline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        createActionBar();
        createRecyclerView();

        if (count <= 0) {
            findViewById(R.id.invite_recycler_view).setVisibility(View.GONE);
            findViewById(R.id.invite_all_read).setVisibility(View.VISIBLE);
        }
    }

    private void deleteItem(int idx) {
        FakeDatabase.getInstance().invitations.remove(idx);
        adapter.notifyItemRemoved(idx);
        count -= 1;
        if (count <= 0) {
            findViewById(R.id.invite_recycler_view).setVisibility(View.GONE);
            findViewById(R.id.invite_all_read).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createActionBar() {
        Toolbar toolbar = findViewById(R.id.invite_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.icon_blue_arrow_back);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.frontLayer)));
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.setTitle("Invitations");
            actionBar.show();
        }
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.invite_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InviteRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        onAccept = adapter.onAccept().subscribe(new Consumer<UUID>() {
            @Override
            public void accept(UUID uuid) throws Exception {
                int idx = FakeDatabase.getInstance().getInvitationIndex(uuid);
                if (idx >= 0) {
                    Presentation p = FakeDatabase.getInstance().invitations.get(idx).presentation;
                    for (GroupMember m: p.members) {
                        if (m.ownerID.equals(FakeDatabase.getInstance().currentUser.id)) {
                            m.isAccepted = true;
                        }
                    }
                    FakeDatabase.getInstance().presentations.add(p);
                    deleteItem(idx);
                }
            }
        });

        onDecline = adapter.onDecline().subscribe(new Consumer<UUID>() {
            @Override
            public void accept(UUID uuid) throws Exception {
                int idx = FakeDatabase.getInstance().getInvitationIndex(uuid);
                if (idx >= 0) {
                    Invitation inv = FakeDatabase.getInstance().invitations.get(idx);
                    Presentation p = inv.presentation;
                    if (inv.type == InvitationType.NEW) {
                        for (GroupMember m: p.members) {
                            if (m.ownerID.equals(inv.receiverID)) {
                                m.isAccepted = false;
                            }
                        }
                    } else if (inv.type == InvitationType.DECLINED) {
                        // dismiss: remove all traces
                        p = FakeDatabase.getInstance().findPresentation(p.id);
                        if (p != null) {
                            {
                                Iterator<GroupMember> i = p.members.iterator();
                                while (i.hasNext()) {
                                    GroupMember m = i.next();
                                    if (m.ownerID.equals(inv.receiverID)) {
                                        i.remove();
                                    }
                                }
                            }
                            {
                                Iterator<Section> i = p.sections.iterator();
                                while (i.hasNext()) {
                                    Section s = i.next();
                                    if (s.userID.equals(inv.receiverID)) {
                                        i.remove();
                                    }
                                }
                            }
                        }
                    }
                    deleteItem(idx);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDecline.dispose();
        onAccept.dispose();
    }
}
