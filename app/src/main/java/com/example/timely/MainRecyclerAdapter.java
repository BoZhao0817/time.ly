package com.example.timely;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainRecyclerAdapter extends RecyclerView.Adapter {
    private Presentation[] data;

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        public MainViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }
    public MainRecyclerAdapter(Presentation[] data) {
        data = data;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MainRecyclerAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        Presentation elem = data[position];
        LinearLayout layout = ((MainViewHolder)holder).layout;
        TextView name = layout.findViewById(R.id.main_presentation_name);
        name.setText(elem.name);
        TextView type = layout.findViewById(R.id.main_presentation_type);
        type.setText(elem.type.getVal());
        TextView duration = layout.findViewById(R.id.main_presentation_duration);
        duration.setText(elem.getDuration());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.length;
    }
}
