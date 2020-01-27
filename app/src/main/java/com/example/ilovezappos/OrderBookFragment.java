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

        // Initializes Retrofit API and client
        Retrofit retrofit = RetrofitClient.getInstance();
        bitStampAPI = retrofit.create(IBitStampAPI.class);

        // Recycler views that contain each of the tables for Bids and Asks
        recyclerBids = view.findViewById(R.id.recycler_bids);
        recyclerBids.setHasFixedSize(true);
        recyclerAsks = view.findViewById(R.id.recycler_asks);
        recyclerAsks.setHasFixedSize(true);

        fetchData();
        return view;
    }

    /**
     * Function that gets the order book data from BitStamp's API through a GET Request.
     * Retrieves an object of type OrderBook containing all the Bids and Asks related to the
     * pair BTC/USD.
     * Implemented using RxJava and Retrofit.
     */
    private void fetchData() {

        compositeDisposable.add(bitStampAPI.getOrderBook("btcusd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orderBooks -> displayData(orderBooks)));
    }

    /**
     * Function that sets the top 50 Bids and Asks for the pair BTC/USD from
     * BitStamp in two different recycler views.
     * @param orderBooks order book object that contains the Bids and Asks for BTC/USD pair
     */
    private void displayData(OrderBook orderBooks) {

        // Limiting the results to the first 50 elements
        OrderBookAdapter bidsAdapter = new OrderBookAdapter(this.getContext(), orderBooks.getBids().subList(0, 50), "bids");
        OrderBookAdapter asksAdapter = new OrderBookAdapter(this.getContext(), orderBooks.getAsks().subList(0, 50), "asks");

        // Setting the adapter
        recyclerBids.setAdapter(bidsAdapter);
        recyclerAsks.setAdapter(asksAdapter);

        // Setting the Layout Manager to display the two "tables"
        recyclerBids.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerAsks.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }
}
