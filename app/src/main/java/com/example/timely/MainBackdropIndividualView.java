package com.example.timely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }
}
