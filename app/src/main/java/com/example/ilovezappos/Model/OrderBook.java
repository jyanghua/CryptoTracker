package com.example.ilovezappos.Model;

import java.util.List;

public class OrderBook {

    public String timestamp;
    public List<List<String>> bids;
    public List<List<String>> asks;

    public OrderBook() { }

    public OrderBook(String timestamp, List<List<String>> bids, List<List<String>> asks) {
        this.timestamp = timestamp;
        this.bids = bids;
        this.asks = asks;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<List<String>> getBids() {
        return bids;
    }

    public List<List<String>> getAsks() {
        return asks;
    }
}