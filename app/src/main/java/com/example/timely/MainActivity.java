package com.example.timely;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createActionBar();
        createBottomSheet();
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
//        final NestedScrollView nestedScrollView = findViewById(R.id.main_nested_view);
//        final String key = "closure";
//        final Map<String, Boolean> isNestedScrolling = new HashMap<>();
//        isNestedScrolling.put(key, false);
//        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                isNestedScrolling.put(key, true);
//            }
//        });
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (isNestedScrolling.containsKey(key)) {
//                    Boolean val = isNestedScrolling.get(key);
//                    if (newState == BottomSheetBehavior.STATE_DRAGGING && val != null && val) {
//                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    }
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
//        });
    }
}
