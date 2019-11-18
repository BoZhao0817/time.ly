package com.example.timely;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import dataStructures.FakeDatabase;
import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Report;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private MainRecyclerAdapter recyclerAdapter;

    private Disposable listItemClicked;
    Report activeReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        this.activeReport = FakeDatabase.getInstance().testReport;

        createActionBar();
        createBackDrop();
        createBottomSheet();
        createRecyclerView();

        // all layout elements are populated
        listItemClicked =  recyclerAdapter.onClick().subscribe(new Consumer<Presentation>() {
            @Override
            public void accept(Presentation presentation) throws Exception {
                updateBackdrop(activeReport);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_app_bar, menu);
        return true;
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.appBar)));
            actionBar.setElevation(0);
            actionBar.show();
        }
    }

    private void createBottomSheet() {
        LinearLayout linearLayout = findViewById(R.id.main_bottom_sheet);
        final BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(linearLayout);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MainRecyclerAdapter adapter = new MainRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        this.recyclerAdapter = adapter;
    }

    private void createBackDrop() {

        if (findViewById(R.id.main_backdrop_menu_wrapper) != null) {
            FragmentManager manager = getSupportFragmentManager();
            // title
            //TextView name = findViewById(R.id.main_backdrop_presentation_name);
            //name.setText(this.activePresentation.name);
            // type
            //TextView type = findViewById(R.id.main_backdrop_presentation_type);
            //type.setText(this.activePresentation.type.toString());
            // menu
            Bundle inputData = new Bundle();
            inputData.putSerializable("data", this.activeReport);
            if (this.activeReport.type == PresentationType.INDIVIDUAL) {
                ReportBackdropIndividualView individualView = new ReportBackdropIndividualView();
                individualView.setArguments(inputData);
                manager.beginTransaction().add(R.id.main_backdrop_menu_wrapper, individualView).commit();
            } else {
                MainBackdropGroupView groupView = new MainBackdropGroupView();
                groupView.setArguments(inputData);
                manager.beginTransaction().add(R.id.main_backdrop_menu_wrapper, groupView).commit();
            }
        }
    }

    private void updateBackdrop(Report datum) {
        if (this.activeReport != null && datum.id.equals(this.activeReport.id)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle inputData = new Bundle();
        inputData.putSerializable("data", datum);
        this.activeReport = datum;

        // title
        //TextView name = findViewById(R.id.report);
        //name.setText(this.activeReport.name);
        // type
        //TextView type = findViewById(R.id.main_backdrop_presentation_type);
        //type.setText(this.activePresentation.type.toString());
        // menu
        if (datum.type == PresentationType.INDIVIDUAL) {
            ReportBackdropIndividualView individualView = new ReportBackdropIndividualView();
            individualView.setArguments(inputData);
            transaction.replace(R.id.main_backdrop_menu_wrapper, individualView);
        } else {
            MainBackdropGroupView groupView = new MainBackdropGroupView();
            groupView.setArguments(inputData);
            transaction.replace(R.id.main_backdrop_menu_wrapper, groupView);
        }

        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_add_presentation: {
                break;
            }
        }
    }

    public void deleteData(Presentation toDelete) {
        this.recyclerAdapter.deleteData(toDelete);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.listItemClicked.dispose();
    }
}
