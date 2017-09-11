package com.example.shubhampandey.ilovezappos.utils;

/**
 * Created by SHUBHAM PANDEY on 9/10/2017.
 */

public class Transaction {
    private double bid;
    private double ask;
    private double amount;
    private boolean isBid;

    public boolean isBid() {
        return isBid;
    }

    public Transaction(boolean isBidTrue) {
        isBid = isBidTrue;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getValue() {
        double askingValue = ask * amount;
        double biddingValue = bid * amount;
        return isBid ? biddingValue : askingValue;
    }

}
