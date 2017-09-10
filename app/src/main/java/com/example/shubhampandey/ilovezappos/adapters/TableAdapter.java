package com.example.shubhampandey.ilovezappos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shubhampandey.ilovezappos.R;

import java.util.List;

/**
 * Created by SHUBHAM PANDEY on 9/10/2017.
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private List<String> values;

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bidAsk;
        private TextView amount;
        private TextView value;

        private ViewHolder(View v) {
            super(v);
            bidAsk = (TextView) v.findViewById(R.id.bidask);
            amount = (TextView) v.findViewById(R.id.amount);
            value = (TextView) v.findViewById(R.id.value);
        }
    }

    public TableAdapter(List<String> myDataSet) {
        values = myDataSet;
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
        final String name = values.get(position);
        holder.bidAsk.setText(name);
        holder.amount.setText("amount: " + name);
        holder.value.setText("value: " + name);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}