package com.example.timely;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.UUID;

import dataStructures.FakeDatabase;
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
            datum = FakeDatabase.getInstance().findPresentation((UUID) bundle.getSerializable("id"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_main_backdrop_individual_view, container, false);
        final EditText duration = root.findViewById(R.id.main_backdrop_presentation_individual_duration);
        final String durationString = datum.getDurationString();
        final MainActivity mainActivity = (MainActivity) getActivity();

        duration.setText(durationString);
        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String d = s.toString();
                TextView warning = mainActivity.warning;
                if (datum.toDuration(d)) {
                    duration.clearFocus();
                    warning.setVisibility(View.GONE);
                    mainActivity.recyclerAdapter.notifyDataSetChanged();
                } else {
                    if (warning.getVisibility() == View.GONE) {
                        warning.setText("Change not applied. Please input number in format mm:ss");
                        warning.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        getActivity().recreate();
//    }

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
