package com.example.shubhampandey.ilovezappos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubhampandey.ilovezappos.R;
import com.example.shubhampandey.ilovezappos.utils.Transaction;

import java.util.List;

/**
 * Created by SHUBHAM PANDEY on 9/10/2017.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private List<Transaction> transactions;

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bidAskView;
        private TextView amountView;
        private TextView valueView;

        private ViewHolder(View v) {
            super(v);
            bidAskView = (TextView) v.findViewById(R.id.bidask);
            amountView = (TextView) v.findViewById(R.id.amount);
            valueView = (TextView) v.findViewById(R.id.value);
        }
    }

    public TableAdapter(List<Transaction> transactionList) {
        transactions = transactionList;
    }

    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Transaction transaction = transactions.get(position);
        String bidOrAsk = "";
        String amount = "";
        String value = "";
        if (transaction.isBid()) {
            bidOrAsk = "" + transaction.getBid();
            amount = "" + transaction.getAmount();
            value = "" + transaction.getValue();
        } else {
            bidOrAsk = "" + transaction.getAsk();
            amount = "" + transaction.getAmount();
            value = "" + transaction.getValue();
        }
        holder.bidAskView.setText(bidOrAsk);
        holder.amountView.setText(amount);
        holder.valueView.setText(value);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

}