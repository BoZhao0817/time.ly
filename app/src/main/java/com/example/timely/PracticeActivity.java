package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import dataStructures.Presentation;
import dataStructures.PresentationType;
import dataStructures.Report;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

enum PracticeBackdropType {
    COUNTDOWN, REPORT
}

public class PracticeActivity extends AppCompatActivity implements View.OnClickListener {

    private PracticeRecyclerAdapter recyclerAdapter;
    private BottomSheetBehavior bottomSheet;
    private Disposable listItemClicked;

    private Report activeReport;
    private Presentation activePresentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Intent i = getIntent();
        this.activePresentation = (Presentation) i.getSerializableExtra("data");
//        activePresentation = (Presentation) getIntent().getSerializableExtra("data");
        activeReport = new Report(this.activePresentation, "new recording");
        this.activePresentation.reports.add(activeReport);

        createActionBar();
        createBackDrop();
        createBottomSheet();
        createRecyclerView();

        // all layout elements are populated
        listItemClicked =  recyclerAdapter.onClick().subscribe(new Consumer<Report>() {
            @Override
            public void accept(Report report) throws Exception {
                updateBackdrop(PracticeBackdropType.REPORT, report);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.simple_app_bar, menu);
        return true;
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.icon_light_arrow_back);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.appBar)));
            actionBar.setTitle("Practice");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backArrow);
            actionBar.show();
        }
    }

    private void createBottomSheet() {
        LinearLayout linearLayout = findViewById(R.id.practice_bottom_sheet);
        final BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(linearLayout);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        this.bottomSheet = sheetBehavior;
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.practice_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PracticeRecyclerAdapter adapter = new PracticeRecyclerAdapter(this.activePresentation);
        recyclerView.setAdapter(adapter);
        this.recyclerAdapter = adapter;
    }

    private void createBackDrop() {
        if (this.activePresentation == null) { return; }
        if (findViewById(R.id.practice_backdrop_menu_wrapper) != null) {
            FragmentManager manager = getSupportFragmentManager();
            Bundle inputData = new Bundle();
            inputData.putSerializable("data", this.activePresentation);
            if (this.activePresentation.type == PresentationType.GROUP) {
                PracticeBackdropGroupView v = new PracticeBackdropGroupView();
                v.setArguments(inputData);
                manager.beginTransaction().add(R.id.practice_backdrop_menu_wrapper, v).commit();
            } else {
                PracticeBackdropIndividualView v = new PracticeBackdropIndividualView();
                v.setArguments(inputData);
                manager.beginTransaction().add(R.id.practice_backdrop_menu_wrapper, v).commit();
            }
        }
    }

    public void updateBackdrop(PracticeBackdropType requestType, Report nextReport) {
        if (this.activePresentation == null) { return; }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle inputData = new Bundle();

        switch (requestType) {
            case REPORT: {
                if (nextReport == null) { return; }
                inputData.putSerializable("data", nextReport);
                this.activeReport = nextReport;
                PracticeBackdropReportView v = new PracticeBackdropReportView();
                v.setArguments(inputData);
                transaction.replace(R.id.practice_backdrop_menu_wrapper, v);
                transaction.commit();
                showBottomSheet();
                break;
            }
            case COUNTDOWN: {
                inputData.putSerializable("data", this.activePresentation);
                if (this.activePresentation.type == PresentationType.GROUP) {
                    PracticeBackdropGroupView v = new PracticeBackdropGroupView();
                    v.setArguments(inputData);
                    transaction.replace(R.id.practice_backdrop_menu_wrapper, v);
                    transaction.commit();
                } else {
                    PracticeBackdropIndividualView v = new PracticeBackdropIndividualView();
                    v.setArguments(inputData);
                    transaction.replace(R.id.practice_backdrop_menu_wrapper, v);
                    transaction.commit();
                }
                break;
            }
        }
    }

    public void showBottomSheet() {
        bottomSheet.setHideable(false);
        bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
        CoordinatorLayout basis = findViewById(R.id.practice_basis);
        if (basis != null) {
            basis.setBackgroundColor(ContextCompat.getColor(this, R.color.appBar));
        }
    }

    public void closeBottomSheet() {
        bottomSheet.setHideable(true);
        bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        CoordinatorLayout basis = findViewById(R.id.practice_basis);
        if (basis != null) {
            basis.setBackgroundColor(ContextCompat.getColor(this, R.color.backDrop));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.practice_add_recording: {
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
        if (this.activeReport.actuals.size() == 0) {
            this.activePresentation.reports.remove(this.activeReport);
        }
    }
}
