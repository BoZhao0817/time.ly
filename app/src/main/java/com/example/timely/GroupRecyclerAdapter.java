package com.example.timely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import dataStructures.GroupMember;
import dataStructures.Presentation;
import dataStructures.Section;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class GroupRecyclerAdapter extends RecyclerView.Adapter implements DragHelperAdapter {
    private ArrayList<GroupMember> data;
    private UUID ownerID;
    private final PublishSubject<GroupMember> onEdit = PublishSubject.create();
    private final PublishSubject<Boolean> onReorder = PublishSubject.create();

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        public GroupViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public GroupRecyclerAdapter(Presentation presentation) {
        this.data = presentation.members;
        this.ownerID = presentation.ownerID;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public GroupRecyclerAdapter.GroupViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_group_member_item_view, parent, false);
        GroupRecyclerAdapter.GroupViewHolder vh = new GroupRecyclerAdapter.GroupViewHolder(l);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GroupMember elem = data.get(position);
        LinearLayout layout = ((GroupRecyclerAdapter.GroupViewHolder)holder).layout;
        TextView name = layout.findViewById(R.id.group_members_list_member_name);
        name.setText(elem.memberName);
        TextView type = layout.findViewById(R.id.group_members_list_role);
        if (elem.isAccepted) {
            if (elem.ownerID == this.ownerID) {
                type.setText("Organizer");
            } else {
                type.setText("Member");
            }
        } else {
            type.setText("Pending");
        }


        TextView duration = layout.findViewById(R.id.group_members_list_duration);
        duration.setText(elem.getDurationString());

        layout.findViewById(R.id.group_members_list_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEdit.onNext(elem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteData(Section datum) {
        int idx = 0;
        boolean found = false;
        for (; idx < this.data.size(); idx += 1) {
            if (data.get(idx).id.equals(datum.id)) {
                found = true;
                break;
            }
        }
        if (found) {
            this.data.remove(idx);
        }
        notifyItemRemoved(idx);
        notifyItemRangeChanged(idx, this.data.size());
    }

    public void addData(GroupMember datum) {
        this.data.add(0, datum);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        onReorder.onNext(true);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Observable<GroupMember> onEdit() {
        return onEdit.hide();
    }
    public Observable<Boolean> onReorder() {
        return onReorder.hide();
    }
}
