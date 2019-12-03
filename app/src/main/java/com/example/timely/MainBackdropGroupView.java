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
import android.widget.Toast;

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

        final TextView portionDuration = root.findViewById(R.id.main_backdrop_presentation_portion_duration);
        portionDuration.setText(datum.getPortionDurationString(FakeDatabase.getInstance().currentUser.id));
        portionDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datum.isOwner()) {
                    Toast.makeText(getActivity(), "Portion can be edited from Sharing menu", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Contact group organizer to change portions", Toast.LENGTH_LONG).show();
                }
            }
        });

        final EditText groupDuration = root.findViewById(R.id.main_backdrop_presentation_group_duration);
        final String durationString = datum.getDurationString();
        groupDuration.setText(durationString);
        if (datum.isOwner()) {
            final MainActivity mainActivity = (MainActivity) getActivity();

            groupDuration.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    String d = s.toString();
                    TextView warning = mainActivity.warning;
                    if (datum.toDuration(d)) {
                        groupDuration.clearFocus();
                        warning.setVisibility(View.GONE);
                        portionDuration.setText(datum.getPortionDurationString(FakeDatabase.getInstance().currentUser.id));
                        mainActivity.recyclerAdapter.notifyDataSetChanged();
                    } else {
                        if (warning.getVisibility() == View.GONE) {
                            warning.setText("Change not applied. Please input number in format mm:ss");
                            warning.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            groupDuration.setKeyListener(null);
            groupDuration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Contact group organizer to change total duration", Toast.LENGTH_LONG).show();
                }
            });
        }

        Button delete = root.findViewById(R.id.main_delete_presentation);
        if (delete != null) {
            delete.setOnClickListener(this);
        }

        GridLayout g1 = root.findViewById(R.id.main_group_start_button);
        GridLayout g2 = root.findViewById(R.id.main_group_practice_button);
        GridLayout g3 = root.findViewById(R.id.main_group_settings_button);
        GridLayout g4 = root.findViewById(R.id.main_group_share_button);
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
            case R.id.main_group_start_button: {
                Intent intent = new Intent(mainActivity, CountdownActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }
            case R.id.main_group_practice_button: {
                Intent intent = new Intent(mainActivity, PracticeActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }
            case R.id.main_group_settings_button: {
                Intent intent = new Intent(mainActivity, ConfigurationActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }

            case R.id.main_group_share_button: {
                Intent intent = new Intent(mainActivity, GroupActivity.class);
                intent.putExtra("presentationID", mainActivity.activePresentation.id);
                startActivity(intent);
                break;
            }
        }
    }
}
