package com.example.timely;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private BottomSheetBehavior bottomSheet;
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
                minimizeBottomSheet();
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
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.appBar)));
            actionBar.setElevation(0);
            actionBar.show();
        }
    }

    private void createBottomSheet() {
        LinearLayout linearLayout = findViewById(R.id.main_bottom_sheet);
        final BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(linearLayout);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        this.bottomSheet = sheetBehavior;

        Button addNewPresentation = findViewById(R.id.main_add_presentation);
        if (addNewPresentation != null) {
            addNewPresentation.setOnClickListener(this);
        }
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MainRecyclerAdapter adapter = new MainRecyclerAdapter();
        recyclerView.setAdapter(adapter);
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                v.onTouchEvent(event);
//                return true;
//            }
//        });
        this.recyclerAdapter = adapter;

        ItemTouchHelper.Callback callback = new DragHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
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
        switch (v.getId()) {
            case R.id.main_add_presentation: {
                addData();
                minimizeBottomSheet();
                break;
            }
            case R.id.main_delete_presentation: {
                deleteData(this.activePresentation);
                break;
            }
        }
    }

    public void expandBottomSheet() {
        bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void minimizeBottomSheet() {
        bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void deleteData(Presentation toDelete) {
        this.recyclerAdapter.deleteData(toDelete);
        this.expandBottomSheet();
        if (FakeDatabase.getInstance().presentations.size() > 0) {
            this.updateBackdrop(FakeDatabase.getInstance().presentations.get(FakeDatabase.getInstance().presentations.size() - 1));
        } else {
            addData();
        }
    }

    public void addData() {
        Presentation datum = Presentation.newInstance();
        this.recyclerAdapter.addData(datum);
        this.updateBackdrop(datum);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.listItemClicked.dispose();
    }
}
