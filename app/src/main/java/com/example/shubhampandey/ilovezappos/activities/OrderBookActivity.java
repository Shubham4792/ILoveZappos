package com.example.shubhampandey.ilovezappos.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.shubhampandey.ilovezappos.R;
import com.example.shubhampandey.ilovezappos.adapters.TableAdapter;
import com.example.shubhampandey.ilovezappos.utils.CommonUtils;
import com.example.shubhampandey.ilovezappos.utils.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.shubhampandey.ilovezappos.utils.CommonUtils.isNetworkAvailable;


public class OrderBookActivity extends BaseActivity {
    private RecyclerView bidView;
    private RecyclerView askView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        bidView = (RecyclerView) findViewById(R.id.bid_table_view);
        askView = (RecyclerView) findViewById(R.id.ask_table_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initListeners();
    }

    private void initListeners() {
        orderBookPopulate();
    }

    private void orderBookPopulate() {
        if (isNetworkAvailable(getApplicationContext())) {
            new OrderBookPopulateTask().execute();
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeRecyclerView(RecyclerView recyclerView, List<Transaction> transactions) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new TableAdapter(transactions);
        recyclerView.setAdapter(mAdapter);
    }

    private List<Transaction> getOrderBookFeed() {
        List<Transaction> transactions = new ArrayList<>();
        JSONObject jsonObject = CommonUtils.getJSON(getString(R.string.order_book_url));
        if (jsonObject != null) {
            try {
                JSONArray bids = jsonObject.getJSONArray("bids");
                JSONArray asks = jsonObject.getJSONArray("asks");
                for (int i = 0; i < 10; i++) {
                    JSONArray bidAmtPair = (JSONArray) bids.get(i);
                    JSONArray askAmtPair = (JSONArray) asks.get(i);
                    Transaction bidTransaction = new Transaction(true);
                    bidTransaction.setBid(Double.parseDouble(bidAmtPair.getString(0)));
                    bidTransaction.setAmount(Double.parseDouble(bidAmtPair.getString(1)));
                    Transaction askTransaction = new Transaction(false);
                    askTransaction.setAsk(Double.parseDouble(askAmtPair.getString(0)));
                    askTransaction.setAmount(Double.parseDouble(askAmtPair.getString(1)));
                    transactions.add(bidTransaction);
                    transactions.add(askTransaction);
                }
                return transactions;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected int getContentPageLayoutId() {
        return R.layout.activity_order_book;
    }

    class OrderBookPopulateTask extends AsyncTask<String, Void, List<Transaction>> {
        @Override
        protected List<Transaction> doInBackground(String... strings) {
            return getOrderBookFeed();
        }

        @Override
        protected void onPostExecute(List<Transaction> transactions) {
            if (transactions != null && !transactions.isEmpty()) {
                List<Transaction> bids = new ArrayList<>();
                List<Transaction> asks = new ArrayList<>();
                for (Transaction transaction : transactions) {
                    if (transaction.isBid()) {
                        bids.add(transaction);
                    } else {
                        asks.add(transaction);
                    }
                }
                initializeRecyclerView(bidView, bids);
                initializeRecyclerView(askView, asks);
            }
        }
    }
}
