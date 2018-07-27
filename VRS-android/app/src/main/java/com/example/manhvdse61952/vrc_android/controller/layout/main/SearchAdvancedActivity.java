package com.example.manhvdse61952.vrc_android.controller.layout.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;

public class SearchAdvancedActivity extends AppCompatActivity {

    RecyclerView rv_search_advanced;
    SearchAdvancedAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_advance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();

        initLayout();


    }
    private void declareID(){
        rv_search_advanced = (RecyclerView)findViewById(R.id.rv_search_advanced);
    }

    private void initLayout(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_search_advanced.setLayoutManager(mLayoutManager);
        adapter = new SearchAdvancedAdapter(ImmutableValue.listSearchAdvanced, SearchAdvancedActivity.this);
        rv_search_advanced.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        SearchAdvancedActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
