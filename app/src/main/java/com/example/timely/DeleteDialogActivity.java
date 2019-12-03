package com.example.timely;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class DeleteDialogActivity extends Dialog implements View.OnClickListener {
    private Activity activity;
    private final PublishSubject<Boolean> onUndo = PublishSubject.create();
    private final PublishSubject<Boolean> onDismiss = PublishSubject.create();

    private String title;
    private String content;

    public DeleteDialogActivity(Activity a, String title, String content) {
        super(a);
        this.activity = a;
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_dialog);

        Button undo = findViewById(R.id.delete_dialog_undo);
        Button dismiss = findViewById(R.id.delete_dialog_dismiss);
        undo.setOnClickListener(this);
        dismiss.setOnClickListener(this);

        TextView titleView = findViewById(R.id.delete_dialog_title);
        titleView.setText(title);
        TextView contentView = findViewById(R.id.delete_dialog_content);
        contentView.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_dialog_undo:
                onUndo.onNext(true);
                onUndo.onComplete();
                break;
            case R.id.delete_dialog_dismiss:
                onDismiss.onNext(true);
                onDismiss.onComplete();
                break;
        }
        dismiss();
    }

    public Single<Boolean> onUndoClicked() {
        return Single.fromObservable(onUndo.hide());
    }

    public Single<Boolean> onDismissClicked() {
        return Single.fromObservable(onDismiss.hide());
    }
}
