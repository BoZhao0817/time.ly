package com.example.timely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dataStructures.Presentation;
import dataStructures.Report;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PracticeRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<Report> data;
    private final PublishSubject<Report> onClickEvent = PublishSubject.create();

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        public MainViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public PracticeRecyclerAdapter(Presentation presentation) {
        this.data = presentation.reports;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public PracticeRecyclerAdapter.MainViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_presentation_item_view, parent, false);
        MainViewHolder vh = new MainViewHolder(l);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Report elem = data.get(position);
        LinearLayout layout = ((MainViewHolder)holder).layout;
        TextView name = layout.findViewById(R.id.main_presentation_name);
        name.setText(elem.name);
        TextView type = layout.findViewById(R.id.main_presentation_type);
        type.setText(elem.type.toString());
        TextView duration = layout.findViewById(R.id.main_presentation_duration);
        duration.setText(elem.getDurationString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEvent.onNext(elem);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

    public Observable<Report> onClick() {
        return onClickEvent.hide();
    }

    public void deleteData(Presentation datum) {
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

    public void addData(Report datum) {
        this.data.add(0, datum);
    }
}
