package com.example.ilovezappos.Model;

public class Transaction
{
    public String date;
    public String tid;
    public String price;
    public String type;
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

    public String getTid() {
        return tid;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }
}