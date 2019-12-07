package com.example.timely;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class HelperDialogActivity extends Dialog implements View.OnClickListener {
    private String title;
    private String content;

    public HelperDialogActivity(Activity a, String title, String content) {
        super(a);
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_helper_dialog);

        Button dismiss = findViewById(R.id.helper_dialog_dismiss);
        dismiss.setOnClickListener(this);

        TextView titleView = findViewById(R.id.helper_dialog_title);
        titleView.setText(title);
        TextView contentView = findViewById(R.id.helper_dialog_content);
        contentView.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.helper_dialog_dismiss:
                break;
        }
        dismiss();
    }
}