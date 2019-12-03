package com.example.timely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dataStructures.Section;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ConfigurationSectionAdapter extends RecyclerView.Adapter {

    private ArrayList<Section> list;

    private final PublishSubject<Section> onClickEvent = PublishSubject.create();

    public static class ConfigurationSectionViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layout;
        public ConfigurationSectionViewHolder(RelativeLayout l) {
            super(l);
            layout = l;
        }
    }

    public ConfigurationSectionAdapter(ArrayList<Section> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ConfigurationSectionAdapter.ConfigurationSectionViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout l = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_configuration_section_item_view, parent, false);
        ConfigurationSectionAdapter.ConfigurationSectionViewHolder vh = new ConfigurationSectionAdapter.ConfigurationSectionViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RelativeLayout view = ((ConfigurationSectionAdapter.ConfigurationSectionViewHolder)holder).layout;

        final Section currentSection = list.get(position);
        TextView section_name= view.findViewById(R.id.section_name);
        section_name.setText(currentSection.sectionName);
        TextView duration= view.findViewById(R.id.duration);
        duration.setText(currentSection.getDurationString());

        //Handle buttons and add onClickListeners
        ImageButton callbtn = view.findViewById(R.id.Edit);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                onClickEvent.onNext(currentSection);
            }
        });
    }

    public Observable<Section> onEditClicked() {
        return onClickEvent.hide();
    }
}