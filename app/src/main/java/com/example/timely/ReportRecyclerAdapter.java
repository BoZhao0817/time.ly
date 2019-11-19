package com.example.timely;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dataStructures.FakeDatabase;
import dataStructures.Presentation;
import dataStructures.Report;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ReportRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<Presentation> data;
    private final PublishSubject<Report> onClickEvent = PublishSubject.create();

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        public MainViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public ReportRecyclerAdapter() {
        this.data = FakeDatabase.getInstance().presentations;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ReportRecyclerAdapter.MainViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_report_presentation_item_view, parent, false);
        MainViewHolder vh = new MainViewHolder(l);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Presentation elem = data.get(position);
        LinearLayout layout = ((MainViewHolder)holder).layout;
        TextView name = layout.findViewById(R.id.presentationName);
        name.setText(elem.name);
        ReportListView reports = layout.findViewById(R.id.listView);
        reports.setAdapter(new ReportAdapter(name.getContext(), data.get(position).reports));
        reports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("POSITION", elem.name+" "+position);
                onClickEvent.onNext(elem.reports.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    public void addData(Presentation datum) {
        this.data.add(0, datum);
    }
}
