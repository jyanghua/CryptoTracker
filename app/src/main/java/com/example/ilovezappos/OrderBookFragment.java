package com.example.ilovezappos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ilovezappos.Adapter.OrderBookAdapter;
import com.example.ilovezappos.Model.OrderBook;
import com.example.ilovezappos.Retrofit.IBitStampAPI;
import com.example.ilovezappos.Retrofit.RetrofitClient;


import javax.sql.DataSource;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class OrderBookFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private IBitStampAPI bitStampAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerBids;
    private RecyclerView recyclerAsks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_book, container, false);

        // Initialize API
        Retrofit retrofit = RetrofitClient.getInstance();
        bitStampAPI = retrofit.create(IBitStampAPI.class);

        // Views
        recyclerBids = view.findViewById(R.id.recycler_bids);
        recyclerBids.setHasFixedSize(true);
        recyclerAsks = view.findViewById(R.id.recycler_asks);
        recyclerAsks.setHasFixedSize(true);

        fetchData();

        return view;
    }

    private void fetchData() {

        compositeDisposable.add(bitStampAPI.getOrderBook("btcusd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderBooks -> displayData(orderBooks)));
    }

    private void displayData(OrderBook orderBooks) {

        // Limiting the results to the first 50 elements
        OrderBookAdapter bidsAdapter = new OrderBookAdapter(this.getContext(), orderBooks.getBids().subList(0, 50), "bids");
        OrderBookAdapter asksAdapter = new OrderBookAdapter(this.getContext(), orderBooks.getAsks().subList(0, 50), "asks");

        recyclerBids.setAdapter(bidsAdapter);
        recyclerAsks.setAdapter(asksAdapter);

        recyclerBids.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerAsks.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
}
