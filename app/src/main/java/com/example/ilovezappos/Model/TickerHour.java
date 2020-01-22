package com.example.ilovezappos.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TickerHour {

    @SerializedName("date")
    @Expose
    public String high;

    @SerializedName("date")
    @Expose
    public String last;

    @SerializedName("date")
    @Expose
    public String timestamp;

    @SerializedName("date")
    @Expose
    public String bid;

    @SerializedName("date")
    @Expose
    public String vwap;

    @SerializedName("date")
    @Expose
    public String volume;

    @SerializedName("date")
    @Expose
    public String low;

    @SerializedName("date")
    @Expose
    public String ask;

    @SerializedName("date")
    @Expose
    public String open;

    public TickerHour() { }

    public TickerHour(String high, String last, String timestamp, String bid, String vwap, String volume, String low, String ask, String open) {
        this.high = high;
        this.last = last;
        this.timestamp = timestamp;
        this.bid = bid;
        this.vwap = vwap;
        this.volume = volume;
        this.low = low;
        this.ask = ask;
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getVwap() {
        return vwap;
    }

    public void setVwap(String vwap) {
        this.vwap = vwap;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }
}
