package com.example.timely;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

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

        this.activePresentation = FakeDatabase.getInstance().presentations.get(0);
//        activePresentation = (Presentation) getIntent().getSerializableExtra("data");

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
        getMenuInflater().inflate(R.menu.practice_app_bar, menu);
        return true;
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.icon_light_arrow_back);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.frontLayer)));
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
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        if (this.activePresentation != null) { return; }
        if (findViewById(R.id.practice_backdrop_menu_wrapper) != null) {
            FragmentManager manager = getSupportFragmentManager();
            Bundle inputData = new Bundle();
            inputData.putSerializable("data", this.activePresentation);
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
        if (this.activePresentation != null) { return; }

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
                showBottomSheet();
                break;
            }
            case COUNTDOWN: {
                inputData.putSerializable("data", this.activePresentation);
                if (this.activePresentation.type == PresentationType.GROUP) {
                    PracticeBackdropGroupView v = new PracticeBackdropGroupView();
                    v.setArguments(inputData);
                    transaction.replace(R.id.practice_backdrop_menu_wrapper, v);
                } else {
                    PracticeBackdropIndividualView v = new PracticeBackdropIndividualView();
                    v.setArguments(inputData);
                    transaction.replace(R.id.practice_backdrop_menu_wrapper, v);
                }
                break;
            }
        }
        transaction.commit();
    }

    public void showBottomSheet() {
        bottomSheet.setHideable(false);
        bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void closeBottomSheet() {
        bottomSheet.setHideable(true);
        bottomSheet.setState(BottomSheetBehavior.STATE_HIDDEN);
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
    }
}
