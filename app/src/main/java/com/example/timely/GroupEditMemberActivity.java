package com.example.timely;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import dataStructures.FakeDatabase;
import dataStructures.GroupMember;
import dataStructures.User;
import dataStructures.VibrationPattern;

public class GroupEditMemberActivity extends AppCompatActivity implements View.OnClickListener{
    EditText minutes;
    EditText seconds;
    EditText name;

    GroupMember currentMember;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit_member);
        createActionBar();

        Bundle bundle = getIntent().getExtras();
        currentMember = (GroupMember) bundle.getSerializable("data");

        ((TextView)findViewById(R.id.presentation_name)).setText(bundle.getString("presentationName"));
        ((TextView)findViewById(R.id.total_time)).setText(bundle.getString("presentationDuration"));

        name = findViewById(R.id.group_edit_member_editName);
        minutes = findViewById(R.id.group_edit_member_minutes);
        seconds = findViewById(R.id.group_edit_member_seconds);

        if (currentMember != null) {
            name.setText(currentMember.memberName);
            int duration = (int) currentMember.duration;
            Integer minute = duration / 60;
            Integer second = duration % 60;
            seconds.setText(second.toString());
            minutes.setText(minute.toString());
        }

        Button delete = findViewById(R.id.group_edit_member_delete);
        Button copy = findViewById(R.id.group_edit_member_copy);
        Button search = findViewById(R.id.group_edit_member_search);

        delete.setOnClickListener(this);
        copy.setOnClickListener(this);
        search.setOnClickListener(this);

        final LinearLayout sharing = findViewById(R.id.group_edit_member_share);
        SwitchCompat switchCompat = findViewById(R.id.group_edit_member_toggle);
        if (currentMember.ownerID.equals(FakeDatabase.getInstance().currentUser.id)) {
            switchCompat.setChecked(true);
            sharing.setVisibility(View.GONE);
        } else {
            switchCompat.setChecked(false);
            sharing.setVisibility(View.VISIBLE);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharing.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable cancel = ContextCompat.getDrawable(this, R.drawable.icon_close);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.appBar)));
            actionBar.setTitle("Edit Section");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(cancel);
            actionBar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_bar, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.group_edit_member_delete: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", currentMember);
                bundle.putSerializable("actionType", FeedbackType.SAVE);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            }
            case R.id.group_edit_member_copy: {
                String txt = "Link Copied to Clipboard.";
                ((TextView)findViewById(R.id.group_edit_member_shareIndicator)).setText(txt);
                Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.group_edit_member_search: {
                Intent search = new Intent(this, GroupSearchUserActivity.class);
                startActivityForResult(search, 1);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", currentMember);
                bundle.putSerializable("actionType", FeedbackType.CANCEL);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            case R.id.save_button: {
                int d = Integer.parseInt(minutes.getText().toString())*60 + Integer.parseInt(seconds.getText().toString());
                String n = name.getText().toString();
                currentMember.duration = (float) d;
                currentMember.memberName = n;

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", currentMember);
                bundle.putSerializable("actionType", FeedbackType.SAVE);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                User passedUser = (User)(bundle.get("data"));
                ArrayList<VibrationPattern> allPatterns = FakeDatabase.getInstance().vibrationPatterns;
                switch ((FeedbackType)(bundle.get("actionType"))) {
                    case CANCEL: {
                        break;
                    }
                    case SAVE: {
                        this.currentMember.ownerID = passedUser.id;
                        ((TextView)findViewById(R.id.group_edit_member_shareIndicator)).setText(
                                "User with name " + passedUser.name + " has been selected"
                        );
                    }
                }
            }
        }
    }
}
