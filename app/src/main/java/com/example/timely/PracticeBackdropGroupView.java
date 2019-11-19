package com.example.timely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
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
                currentReport = new Report(currentPresentation);
                currentPresentation.reports.add(currentReport);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_practice_backdrop_group_view, container, false);
        init(root, currentPresentation, currentReport);
        return root;
    }

    @Override
    protected void onStarted() {
        TextView name = root.findViewById(R.id.practice_backdrop_recording_name);
        if (name != null) {
            name.setText(currentReport.name);
        }
        TextView type = root.findViewById(R.id.practice_backdrop_recording_type);
        if (type != null) {
            type.setText(currentReport.group_type.toString());
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
