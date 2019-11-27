package com.example.timely;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import dataStructures.NamedSegments;
import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Report;
import dataStructures.Utilities;


public class PracticeBackdropReportView extends Fragment implements View.OnClickListener {
    private Report datum;
    private View root;
    public PracticeBackdropReportView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            datum = (Report) bundle.getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_practice_backdrop_report_view, container, false);

        if (getContext() != null) {
            SeekBar seekBar = root.findViewById(R.id.seekBar);
            seekBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.darkButtonText), PorterDuff.Mode.MULTIPLY);
        }

        TextView name = root.findViewById(R.id.practice_backdrop_recording_name);
        if (name != null) {
            name.setText(datum.name);
        }
        if (datum.type == PresentationType.GROUP) {
            TextView type = root.findViewById(R.id.practice_backdrop_recording_type);
            if (type != null) {
                type.setText(datum.group_type.toString());
            }
        }

        TextView duration = root.findViewById(R.id.report_backdrop_presentation_individual_duration);
        duration.setText(Presentation.toStringTime(datum.total_actual));
        TextView estTime = root.findViewById(R.id.estimateTime);
        TextView actTime = root.findViewById(R.id.actualTime);
        estTime.setText(Presentation.toStringTime(datum.total_estimate));
        actTime.setText(Presentation.toStringTime(datum.total_actual));
        LinearLayout est = root.findViewById(R.id.estimateChart);
        LinearLayout act = root.findViewById(R.id.actualChart);

        ArrayList<NamedSegments> estimates = datum.estimates;
        ArrayList<NamedSegments> actuals = datum.actuals;

        double avse = (datum.total_actual*1.0) / datum.total_estimate;
        double evsa = (datum.total_estimate*1.0) / datum.total_actual;
        double est_len;
        double act_len;
        if (avse < 1.0) {
            est_len = est.getLayoutParams().width;
            act_len = est_len * avse;
        } else {
            act_len = act.getLayoutParams().width;
            est_len = act_len * evsa;
        }
        Utilities util = new Utilities(getContext());
        est_len = util.convertPX((int)est_len);
        act_len = util.convertPX((int)act_len);

        util.setChartDynamicLength(est, estimates, (int)est_len);
        util.setChartDynamicLength(act, actuals, (int)act_len);


        return root;
    }


    @Override
    public void onClick(View v) {
        // TODO
    }
}
