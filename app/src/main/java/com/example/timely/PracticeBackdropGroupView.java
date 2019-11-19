package com.example.timely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import dataStructures.Presentation;
import dataStructures.Report;
import dataStructures.ReportGroupType;

public class PracticeBackdropGroupView extends PracticeCommonView {

    private Presentation currentPresentation;
    private Report currentReport;

    public PracticeBackdropGroupView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentPresentation = (Presentation) bundle.getSerializable("data");
            if (currentPresentation != null){
                currentReport = currentPresentation.reports.get(currentPresentation.reports.size() - 1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_practice_backdrop_group_view, container, false);
        init(root, currentPresentation, currentReport);
        TextView name = root.findViewById(R.id.practice_backdrop_recording_name);
        if (name != null) {
            name.setText(currentReport.name);
        }
        return root;
    }

    @Override
    protected void onStarted() {
        TextView type = root.findViewById(R.id.practice_backdrop_recording_type);
        if (type != null) {
            type.setText(currentReport.group_type.toString());
        }
        RadioGroup radios = root.findViewById(R.id.practice_radios);
        if (type != null) {
            ((LinearLayout)radios.getParent()).removeView(radios);
        }

        PracticeActivity parent = (PracticeActivity) getActivity();
        if (parent != null) {
            parent.closeBottomSheet();
        }
    }

    @Override
    protected void toReport() {
        PracticeActivity parent = (PracticeActivity) getActivity();
        if (parent != null) {
            parent.updateBackdrop(PracticeBackdropType.REPORT, currentReport);
        }
    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        if (!checked) { return; }
        switch(v.getId()) {
            case R.id.practice_radio_individual:
                currentReport.group_type = ReportGroupType.PORTION;
                break;
            case R.id.practice_radio_group:
                currentReport.group_type = ReportGroupType.GROUP;
                break;
        }
    }
}
