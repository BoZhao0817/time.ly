package com.example.timely;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewStub;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {
    private Presentation[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = initializeData();
        createActionBar();
        createBottomSheet();
        createRecyclerView();
        createBackDropMenu();
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
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private Presentation[] initializeData() {
        Presentation[] data = new Presentation[1];
        data[0] = new Presentation();
        data[0].name = "For CS";
        data[0].duration = 180;
        data[0].type = PresentationType.INDIVIDUAL;
        return data;
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    private void createBackDropMenu() {
        if (data == null || data.length == 0 || data[0].type == PresentationType.INDIVIDUAL) {
            ViewStub stub = findViewById(R.id.main_backdrop_menu);
            stub.setLayoutResource(R.layout.fragment_main_backdrop_solo_view);
            stub.inflate();
        } else if (data[0].type == PresentationType.GROUP) {
            ViewStub stub = findViewById(R.id.main_backdrop_menu);
            stub.setLayoutResource(R.layout.fragment_main_backdrop_group_view);
            stub.inflate();
        }
    }
}
