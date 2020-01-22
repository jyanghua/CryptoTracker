package com.example.ilovezappos.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction
{
    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("tid")
    @Expose
    public String tid;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("amount")
    @Expose
    public String amount;

    public Transaction() { }

    public Transaction(String date, String tid, String price, String type, String amount) {
        this.date = date;
        this.tid = tid;
        this.price = price;
        this.type = type;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}