package com.example.timely;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Section;

public class SectionAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Section> list = new ArrayList<Section>();
    private Context context;
    public SectionAdapter(ArrayList<Section> list, Context context) {
        this.list = list;
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
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }
    public String timeFormat(int secondCount)
    {
        int seconds = secondCount % 60;
        int minutes = secondCount / 60;
        String secondsStr = Integer.toString(seconds);
        if (secondsStr.length() == 1) {
            secondsStr = "0" + secondsStr;
        }
        String minutesStr = Integer.toString(minutes);
        if (minutesStr.length() == 1) {
            minutesStr = "0" + minutesStr;
        }
        return minutesStr + ":" + secondsStr;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.section_row, null);
        }

        //Handle TextView and display string from your list
        TextView section_name= (TextView)view.findViewById(R.id.section_name);
        section_name.setText(list.get(position).sectionName);
        TextView duration= (TextView)view.findViewById(R.id.duration);
        duration.setText(list.get(position).getDurationString());

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.Edit);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Intent inte=new Intent(context.getApplicationContext(),EditSection.class);
                inte.putExtra("section_name", list.get(position).sectionName);
                inte.putExtra("section_duration", list.get(position).duration.toString());
                inte.putExtra("section_index",position);
                inte.putExtra("user_name",list.get(position).ownerName);
                inte.putExtra("user_id",list.get(position).userID);
                context.startActivity(inte);
            }
        });


        return view;
    }
}