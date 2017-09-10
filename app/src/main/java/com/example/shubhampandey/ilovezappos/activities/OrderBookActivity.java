package com.example.shubhampandey.ilovezappos.activities;

import android.os.Bundle;

import com.example.shubhampandey.ilovezappos.R;

public class OrderBookActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentPageLayoutId() {
        return R.layout.activity_order_book;
    }
}
