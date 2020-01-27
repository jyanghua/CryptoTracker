package com.example.ilovezappos.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit rInstance;

    /**
     * Default instance Retrofit object getter set to the Bitstamp API v2
     * @return Instance of the Retrofit object
     */
    public static Retrofit getInstance() {
        if(rInstance == null)
            rInstance = new Retrofit.Builder()
                    .baseUrl("https://www.bitstamp.net/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return rInstance;
    }

    private RetrofitClient() {
    }
}
