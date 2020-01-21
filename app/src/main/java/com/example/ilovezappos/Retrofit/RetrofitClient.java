package com.example.ilovezappos.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit ourInstance;

    public static Retrofit getInstance() {
        if(ourInstance == null)
            ourInstance = new Retrofit.Builder()
                    .baseUrl("https://www.bitstamp.net/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return ourInstance;
    }

    private RetrofitClient() {
    }
}
