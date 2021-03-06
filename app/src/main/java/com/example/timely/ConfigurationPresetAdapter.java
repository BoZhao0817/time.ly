package com.example.timely;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import dataStructures.FakeDatabase;
import dataStructures.VibrationPattern;
import dataStructures.VibrationPatternType;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

class ConfigurationCheckedItem {
    VibrationPattern pattern;
    int elementID;

    public ConfigurationCheckedItem(VibrationPattern pattern, int elementID) {
        this.pattern = pattern;
        this.elementID = elementID;
    }
}

public class ConfigurationPresetAdapter extends RecyclerView.Adapter {
    private ArrayList<VibrationPattern> list;
    private Context context;
    private UUID initialPatternID;
    private int lastCheckedPosition = -1;
    private RecyclerView recyclerView;

    private final PublishSubject<VibrationPattern> onEdit = PublishSubject.create();
    private final PublishSubject<ConfigurationCheckedItem> onCheck = PublishSubject.create();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public ViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public ConfigurationPresetAdapter(Context context, UUID initialPatternID) {
        this.list = FakeDatabase.getInstance().vibrationPatterns;
        this.context = context;
        this.initialPatternID = initialPatternID;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ConfigurationPresetAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_configuration_pattern_item_view, parent, false);
        ConfigurationPresetAdapter.ViewHolder vh = new ConfigurationPresetAdapter.ViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LinearLayout view = ((ViewHolder)holder).layout;
        //Handle TextView and display string from your list
        final VibrationPattern currentPattern = list.get(position);

        TextView pattern_name = view.findViewById(R.id.pattern_name);
        pattern_name.setText(currentPattern.name);

        LinearLayout preset_display = view.findViewById(R.id.pattern_layout);
        preset_display.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(context);

        for (VibrationPatternType type: currentPattern.patterns){
            switch (type) {
                case LONG: {
                    preset_display.addView(inflater.inflate(R.layout.preset_line_long, view, false));
                    break;
                }
                case SHORT: {
                    preset_display.addView(inflater.inflate(R.layout.preset_line_short, view, false));
                    break;
                }
            }
        }

        ImageButton editButton = view.findViewById(R.id.EditPreset);
        final RadioButton checkButton = view.findViewById(R.id.checkButton);

        if (currentPattern.id.equals(initialPatternID)) {
            checkButton.setChecked(true);
            initialPatternID = null;
            lastCheckedPosition = position;
        }

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onEdit.onNext(currentPattern);
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (lastCheckedPosition < 0) {
                    lastCheckedPosition = position;
                } else {
                    RadioButton lastRadio = recyclerView.findViewHolderForAdapterPosition(lastCheckedPosition).itemView.findViewById(R.id.checkButton);
                    if (lastRadio != null) {
                        lastRadio.setChecked(false);
                    }
                    lastCheckedPosition = position;
                }
                onCheck.onNext(new ConfigurationCheckedItem(currentPattern, v.getId()));
            }
        });
    }

    public Observable<VibrationPattern> onEditClicked() {
        return onEdit.hide();
    }
    public Observable<ConfigurationCheckedItem> onPatternSelected() {
        return onCheck.hide();
    }
}