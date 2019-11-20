package com.example.timely;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

public class ConfigurationPresetAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<VibrationPattern> list;
    private Context context;

    private final PublishSubject<VibrationPattern> onEdit = PublishSubject.create();
    private final PublishSubject<ConfigurationCheckedItem> onCheck = PublishSubject.create();

    public ConfigurationPresetAdapter(Context context) {
        this.list = FakeDatabase.getInstance().vibrationPatterns;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pattern_preset_row, null);
        }
        //Handle TextView and display string from your list
        final VibrationPattern currentPattern = list.get(position);

        TextView pattern_name = view.findViewById(R.id.pattern_name);
        pattern_name.setText(currentPattern.name);

        LinearLayout preset_display = view.findViewById(R.id.pattern_layout);
        LayoutInflater inflater = LayoutInflater.from(context);

        for (VibrationPatternType type: currentPattern.patterns){
            switch (type) {
                case LONG: {
                    preset_display.addView(inflater.inflate(R.layout.preset_line_short, null));
                }
                case SHORT: {
                    preset_display.addView(inflater.inflate(R.layout.preset_line_long, null));
                }
            }
        }

        ImageButton editButton = view.findViewById(R.id.EditPreset);
        final ImageButton checkButton = view.findViewById(R.id.checkButton);

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onEdit.onNext(currentPattern);
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onCheck.onNext(new ConfigurationCheckedItem(currentPattern, v.getId()));
            }
        });
        return view;
    }

    public Observable<VibrationPattern> onEditClicked() {
        return onEdit.hide();
    }
    public Observable<ConfigurationCheckedItem> onPatternSelected() {
        return onCheck.hide();
    }
}