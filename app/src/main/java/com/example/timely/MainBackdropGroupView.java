package com.example.timely;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import dataStructures.FakeDatabase;
import dataStructures.Presentation;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainBackdropGroupView extends Fragment implements View.OnClickListener {

    private Presentation datum;

    public MainBackdropGroupView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            datum = (Presentation) bundle.getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_backdrop_group_view, container, false);
        TextView groupDuration = root.findViewById(R.id.main_backdrop_presentation_group_duration);
        groupDuration.setText(datum.getDurationString());
        TextView portionDuration = root.findViewById(R.id.main_backdrop_presentation_portion_duration);
        portionDuration.setText(datum.getPortionDurationString(FakeDatabase.getInstance().currentUser.userID));
        GridLayout g = root.findViewById(R.id.main_group_start_button);
        g.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_backdrop_delete_button) {
            MainActivity mainActivity = (MainActivity)getActivity();
            if (mainActivity != null) {
                mainActivity.deleteData(this.datum);
            }
        }
        if (v.getId() == R.id.main_group_start_button) {
            MainActivity mainActivity = (MainActivity)getActivity();
            Log.d("WARN", "aaaaa");
            Intent intent = new Intent(mainActivity, CountdownActivity.class);
            intent.putExtra("data", mainActivity.activePresentation);
            startActivity(intent);
        }
    }
}
