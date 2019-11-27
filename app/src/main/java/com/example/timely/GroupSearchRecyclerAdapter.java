package com.example.timely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dataStructures.User;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class GroupSearchRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<User> data;
    private int lastTappedPosition = -1;
    private RecyclerView recyclerView;
    private final PublishSubject<User> onSelect = PublishSubject.create();

    public static class GroupSearchViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        public GroupSearchViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public GroupSearchRecyclerAdapter(ArrayList<User> users) {
        this.data = users;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public GroupSearchRecyclerAdapter.GroupSearchViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_group_search_user_item_view, parent, false);
        GroupSearchRecyclerAdapter.GroupSearchViewHolder vh = new GroupSearchRecyclerAdapter.GroupSearchViewHolder(l);
        return vh;
    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final User elem = data.get(position);
        final LinearLayout layout = ((GroupSearchRecyclerAdapter.GroupSearchViewHolder)holder).layout;
        TextView name = layout.findViewById(R.id.search_user_name);
        name.setText(elem.name);

        RadioButton radio = layout.findViewById(R.id.search_check_user);
        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastTappedPosition < 0) {
                    lastTappedPosition = position;
                } else {
                    RadioButton lastRadio = recyclerView.findViewHolderForAdapterPosition(lastTappedPosition).itemView.findViewById(R.id.search_check_user);
                    if (lastRadio != null) {
                        lastRadio.setChecked(false);
                    }
                    lastTappedPosition = position;
                }
                onSelect.onNext(elem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Observable<User> onSelect() {
        return onSelect.hide();
    }
}
