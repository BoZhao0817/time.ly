package com.example.timely;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import dataStructures.Presentation;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainBackdropIndividualView extends Fragment implements View.OnClickListener {

    private Presentation datum;

    public MainBackdropIndividualView() {
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
        View root = inflater.inflate(R.layout.fragment_main_backdrop_individual_view, container, false);
        TextView duration = root.findViewById(R.id.main_backdrop_presentation_individual_duration);
        duration.setText(datum.getDurationString());

        Button delete = root.findViewById(R.id.main_delete_presentation);
        if (delete != null) {
            delete.setOnClickListener(this);
        }

        GridLayout g1 = root.findViewById(R.id.main_individual_start_button);
        GridLayout g2 = root.findViewById(R.id.main_individual_practice_button);
        GridLayout g3 = root.findViewById(R.id.main_individual_settings_button);
        GridLayout g4 = root.findViewById(R.id.main_individual_share_button);
        g1.setOnClickListener(this);
        g2.setOnClickListener(this);
        g3.setOnClickListener(this);
        g4.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        MainActivity mainActivity = (MainActivity)getActivity();
        if (mainActivity == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.main_individual_start_button: {
                Intent intent = new Intent(mainActivity, CountdownActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }
            case R.id.main_individual_practice_button: {
                Intent intent = new Intent(mainActivity, PracticeActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }
            case R.id.main_individual_settings_button: {
                Intent intent = new Intent(mainActivity, ConfigurationActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }
            case R.id.main_individual_share_button: {
                Intent intent = new Intent(mainActivity, GroupActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }
        }
    }
}
