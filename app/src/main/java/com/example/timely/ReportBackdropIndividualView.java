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
        /*
        HorizontalBarChart chart = root.findViewById(R.id.estimate_chart);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        ArrayList<BarEntry> entries = new ArrayList<>();
        BarEntry estimates = new BarEntry(0f, new float[] {10, 20, 30});
        entries.add(estimates);
        BarDataSet set = new BarDataSet(entries, "Dataset 1");
        set.setDrawIcons(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(0);
        data.setBarWidth(9f);
        chart.setData(data);
        */
        LinearLayout ll = root.findViewById(R.id.estimateChart);
        ArrayList<Integer> estimates = datum.estimates;
        setChart(ll, estimates);
        return root;
    }

    private void setChart(LinearLayout ll, ArrayList<Integer> values) {
        int length = convertPX(ll.getLayoutParams().width);
        String[] colors = {"#FC6451", "#1CBD7D", "#FDD242", "#C056FF"};
        int total = 0;
        for (int v : values) {
            total += v;
        }
        for (int i = 0; i < values.size(); i++) {
            int dp = (int)Math.floor(((values.get(i)*1.0) / total) * length);
            View v = new View(getContext());
            v.setLayoutParams(new LinearLayout.LayoutParams(convertDP(dp), convertDP(20)));
            v.setBackgroundColor(Color.parseColor(colors[i % colors.length]));
            ll.addView(v);
        }
    }

    private int convertDP(int dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5f);
    }

    private int convertPX(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)Math.ceil(px / scale);
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
