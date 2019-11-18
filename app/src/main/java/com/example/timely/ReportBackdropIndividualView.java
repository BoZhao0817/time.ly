package com.example.timely;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Arrays;

import dataStructures.Presentation;
import dataStructures.Report;
import dataStructures.Section;
import dataStructures.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportBackdropIndividualView extends Fragment implements View.OnClickListener {

    private Report datum;
    private View root;
    public ReportBackdropIndividualView() {
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
        root = inflater.inflate(R.layout.fragment_report_backdrop_individual_view, container, false);
        TextView duration = root.findViewById(R.id.report_backdrop_presentation_individual_duration);
        duration.setText(Presentation.toStringTime(datum.total_actual));
        TextView estTime = root.findViewById(R.id.estimateTime);
        TextView actTime = root.findViewById(R.id.actualTime);
        estTime.setText(Presentation.toStringTime(datum.total_estimate));
        actTime.setText(Presentation.toStringTime(datum.total_actual));
        LinearLayout est = root.findViewById(R.id.estimateChart);
        LinearLayout act = root.findViewById(R.id.actualChart);
        ArrayList<Integer> estimates = datum.estimates;
        ArrayList<Integer> actuals = datum.actuals;
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

        util.setChart(est, estimates, (int)est_len);
        util.setChart(act, actuals, (int)act_len);


        return root;
    }


    @Override
    public void onClick(View v) {
        MainActivity mainActivity = (MainActivity)getActivity();
        if (mainActivity == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.main_backdrop_delete_button: {
                break;
            }
            case R.id.main_individual_start_button: {
                Intent intent = new Intent(mainActivity, CountdownActivity.class);
                intent.putExtra("data", mainActivity.activePresentation);
                startActivity(intent);
                break;
            }
            case R.id.main_individual_practice_button: {
                Intent intent = new Intent(mainActivity, PracticeActivity.class);
                intent.putExtra("data", mainActivity.activePresentation);
                startActivity(intent);
                break;
            }
            case R.id.main_individual_settings_button: {
                Intent intent = new Intent(mainActivity, ConfigurationActivity.class);
                intent.putExtra("data", mainActivity.activePresentation);
                startActivity(intent);
                break;
            }
            case R.id.main_individual_share_button: {
                Intent intent = new Intent(mainActivity, GroupActivity.class);
                intent.putExtra("data", mainActivity.activePresentation);
                startActivity(intent);
                break;
            }
        }
    }
}
