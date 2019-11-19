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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainRecyclerAdapter recyclerAdapter;
    public ReportListView rpl;
    private Disposable listItemClicked;
    Presentation activePresentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activePresentation = FakeDatabase.getInstance().presentations.get(0);

        createActionBar();
        createBackDrop();
        createBottomSheet();
        createRecyclerView();

        // all layout elements are populated
        listItemClicked =  recyclerAdapter.onClick().subscribe(new Consumer<Presentation>() {
            @Override
            public void accept(Presentation presentation) throws Exception {
                updateBackdrop(presentation);
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
            TextView name = findViewById(R.id.main_backdrop_presentation_name);
            name.setText(this.activePresentation.name);
            // type
            TextView type = findViewById(R.id.main_backdrop_presentation_type);
            type.setText(this.activePresentation.type.toString());
            // menu
            Bundle inputData = new Bundle();
            inputData.putSerializable("data", this.activePresentation);
            if (this.activePresentation.type == PresentationType.INDIVIDUAL) {
                MainBackdropIndividualView individualView = new MainBackdropIndividualView();
                individualView.setArguments(inputData);
                manager.beginTransaction().add(R.id.main_backdrop_menu_wrapper, individualView).commit();
            } else {
                MainBackdropGroupView groupView = new MainBackdropGroupView();
                groupView.setArguments(inputData);
                manager.beginTransaction().add(R.id.main_backdrop_menu_wrapper, groupView).commit();
            }
        }
    }

    private void updateBackdrop(Presentation datum) {
        if (this.activePresentation != null && datum.id.equals(this.activePresentation.id)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle inputData = new Bundle();
        inputData.putSerializable("data", datum);
        this.activePresentation = datum;

        // title
        TextView name = findViewById(R.id.main_backdrop_presentation_name);
        name.setText(this.activePresentation.name);
        // type
        TextView type = findViewById(R.id.main_backdrop_presentation_type);
        type.setText(this.activePresentation.type.toString());
        // menu
        if (datum.type == PresentationType.INDIVIDUAL) {
            MainBackdropIndividualView individualView = new MainBackdropIndividualView();
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
        Log.d("WHATTHEHELL", Integer.toString(v.getId()));
        switch (v.getId()) {
            case R.id.main_add_presentation: {
                addData();
                break;
            }
        }
    }

    public void deleteData(Presentation toDelete) {
        this.recyclerAdapter.deleteData(toDelete);
    }

    public void addData() {
        Presentation datum = Presentation.newInstance();
        this.activePresentation = datum;
        this.recyclerAdapter.addData(datum);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.listItemClicked.dispose();
    }
}
