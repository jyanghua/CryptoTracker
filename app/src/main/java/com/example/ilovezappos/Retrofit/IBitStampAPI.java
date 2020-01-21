package com.example.ilovezappos.Retrofit;

import android.database.Observable;

import com.example.ilovezappos.Model.OrderBook;
import com.example.ilovezappos.Model.Transaction;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IBitStampAPI {
    @GET("transactions/{pair}")
    Observable<List<Transaction>> getTransaction(@Path("pair") String currencyPair);

    @GET("order_book/{pair}")
    Observable<List<OrderBook>> getOrderBook(@Path("pair") String currencyPair);
}
