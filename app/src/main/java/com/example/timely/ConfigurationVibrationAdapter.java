package com.example.timely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import dataStructures.VibrationPatternType;

public class ConfigurationVibrationAdapter extends RecyclerView.Adapter implements DragHelperAdapter {
    private ArrayList<VibrationPatternType> list;

    private final static int SHORT = 0;
    private final static int LONG = 1;

    public static class ShortViewHolder extends RecyclerView.ViewHolder {
        public View v;

        public ShortViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }

    public static class LongViewHolder extends RecyclerView.ViewHolder {
        public View v;

        public LongViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }

    public ConfigurationVibrationAdapter(ArrayList<VibrationPatternType> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == VibrationPatternType.SHORT) {
            return SHORT;
        } else {
            return LONG;
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == SHORT) {
            View l = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.preset_rect_short, parent, false);
            return new ConfigurationVibrationAdapter.ShortViewHolder(l);
        } else {
            View l = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.preset_rect_long, parent, false);
            return new ConfigurationVibrationAdapter.LongViewHolder(l);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = holder.getAdapterPosition();
        if (holder.getItemViewType() == SHORT) {
            ShortViewHolder s = (ShortViewHolder) holder;
            s.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(pos);
                    notifyItemRemoved(pos);
                }
            });

        } else {
            LongViewHolder l = (LongViewHolder) holder;
            l.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(pos);
                    notifyItemRemoved(pos);
                }
            });
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
}
