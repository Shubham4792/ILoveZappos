package com.example.shubhampandey.ilovezappos.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shubhampandey.ilovezappos.R;
import com.example.shubhampandey.ilovezappos.adapters.TableAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderBookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView bidView = (RecyclerView) findViewById(R.id.bid_table_view);
        RecyclerView askView = (RecyclerView) findViewById(R.id.ask_table_view);
        initializeRecyclerView(bidView);
        initializeRecyclerView(askView);
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            input.add("Test" + i);
        }
        RecyclerView.Adapter mAdapter = new TableAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    protected int getContentPageLayoutId() {
        return R.layout.activity_order_book;
    }
}
