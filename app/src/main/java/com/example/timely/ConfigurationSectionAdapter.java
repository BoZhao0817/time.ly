package com.example.timely;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dataStructures.Section;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ConfigurationSectionAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Section> list;
    private Context context;

    private final PublishSubject<Section> onClickEvent = PublishSubject.create();

    public ConfigurationSectionAdapter(ArrayList<Section> list, Context context) {
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
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view = inflater.inflate(R.layout.section_row, null);
            }
        }

        //Handle TextView and display string from your list
        final Section currentSection = list.get(position);
        TextView section_name= view.findViewById(R.id.section_name);
        section_name.setText(currentSection.sectionName);
        TextView duration= view.findViewById(R.id.duration);
        duration.setText(currentSection.getDurationString());

        //Handle buttons and add onClickListeners
        Button callbtn = view.findViewById(R.id.Edit);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                onClickEvent.onNext(currentSection);
            }
        });


        return view;
    }

    public Observable<Section> onEditClicked() {
        return onClickEvent.hide();
    }
}