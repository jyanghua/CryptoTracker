package com.example.ilovezappos.Retrofit;


import com.example.ilovezappos.Model.OrderBook;
import com.example.ilovezappos.Model.TickerHour;
import com.example.ilovezappos.Model.Transaction;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IBitStampAPI {
    @GET("transactions/{pair}")
    Observable<List<Transaction>> getTransaction(
            @Path("pair") String currencyPair,
            @Query("time") String time
    );

    @GET("order_book/{pair}")
    Observable<OrderBook> getOrderBook(
            @Path("pair") String currencyPair
    );

    @GET("ticker_hour/{pair}")
    Observable<List<TickerHour>> getTickerHour(
            @Path("pair") String currencyPair
    );
}
