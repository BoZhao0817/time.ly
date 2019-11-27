package com.example.timely;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dataStructures.FakeDatabase;
import dataStructures.User;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GroupSearchUserActivity extends AppCompatActivity {
    private User selectedUser;
    private RecyclerView recyclerView;
    private Disposable onSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search_users);
        createActionBar();

        recyclerView = findViewById(R.id.search_users_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        GroupSearchRecyclerAdapter adapter = new GroupSearchRecyclerAdapter(FakeDatabase.getInstance().users);
        recyclerView.setAdapter(adapter);
        this.onSelect = adapter.onSelect().subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                selectedUser = user;
                ((TextView)findViewById(R.id.search_selected_user)).setText(selectedUser.name);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return onQueryTextChange(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                GroupSearchRecyclerAdapter adapter = new GroupSearchRecyclerAdapter(FakeDatabase.getInstance().findUser(newText));
                recyclerView.swapAdapter(adapter, false);
                onSelect.dispose();
                onSelect = adapter.onSelect().subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        selectedUser = user;
                        ((TextView)findViewById(R.id.search_selected_user)).setText(selectedUser.name);
                    }
                });
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", selectedUser);
                bundle.putSerializable("actionType", FeedbackType.CANCEL);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
            case R.id.search_bar_confirm: {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", selectedUser);
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

    private void createActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable cancel = ContextCompat.getDrawable(this, R.drawable.icon_close);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.appBar)));
            actionBar.setTitle("Search Users");
            actionBar.setElevation(0);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(cancel);
            actionBar.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onSelect.dispose();
    }
}
