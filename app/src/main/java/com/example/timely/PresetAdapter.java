package com.example.timely;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Section;
import dataStructures.Utilities;
import dataStructures.VibrationPattern;

public class PresetAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<ArrayList<VibrationPattern>> list = new ArrayList<ArrayList<VibrationPattern>>();
    private Context context;
    private TextView textview;
    public PresetAdapter(ArrayList<ArrayList<VibrationPattern>> list, TextView tv,Context context) {
        this.list = list;
        this.context = context;
        this.textview=tv;
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
        //return list.get(pos).getId();
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
        TextView pattern_name = (TextView)view.findViewById(R.id.pattern_name);

        pattern_name.setText("Pattern"+position);
        String merged_pattern="";
        ArrayList<Integer> pattern_values=new ArrayList<>();
        LinearLayout preset_display=view.findViewById(R.id.pattern_layout);
        for(VibrationPattern vib: list.get(position))
        {
            if (vib.equals(VibrationPattern.LONG)) {
                pattern_values.add(20);
                merged_pattern += "20 ";
            } else {
                pattern_values.add(10);
                merged_pattern += "10 ";
            }

        }
        //pattern_name.setText(merged_pattern);
        Utilities utilities2=new Utilities(context.getApplicationContext());
        utilities2.setChartPreset(preset_display,pattern_values);
        Button callbtn= (Button)view.findViewById(R.id.EditPreset);
        final Button checkButton = (Button)view.findViewById(R.id.checkButton);
        pattern_values.clear();
        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //do something
                //Intent inte=new Intent(context.getApplicationContext(),EditSection.class);
                //inte.putExtra("section_name", list.get(position).sectionName);
                //inte.putExtra("section_duration", list.get(position).duration.toString());
                //inte.putExtra("section_index",position);
                //inte.putExtra("user_name",list.get(position).ownerName);
               // inte.putExtra("user_id",list.get(position).userID);
               // context.startActivity(inte);
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkButton.setBackgroundResource(R.drawable.ic_check_circle_black2_24dp);
                textview.setText(String.valueOf("Pattern"+position));
            }
        });
        return view;
    }
}