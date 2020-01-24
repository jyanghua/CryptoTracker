package com.example.ilovezappos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ilovezappos.Model.Transaction;
import com.example.ilovezappos.Retrofit.IBitStampAPI;
import com.example.ilovezappos.Retrofit.RetrofitClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LineChart priceChart;

    IBitStampAPI bitStampAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Navigation Bar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Initialize API
        Retrofit retrofit = RetrofitClient.getInstance();
        bitStampAPI = retrofit.create(IBitStampAPI.class);

        // Check internet connection
        if (isNetworkConnected()){
            fetchData();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG);
            toast.show();
        }

        bitStampAPI.getTransaction("btcusd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Transaction>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Transaction> transactions) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        // Line Chart with MPAndroidChart
        priceChart = findViewById(R.id.lineChart);

//        priceChart.setOnChartGestureListener(MainActivity.this);
//        priceChart.setOnChartValueSelectedListener(MainActivity.this);
        priceChart.setDragEnabled(true);
        priceChart.setScaleEnabled(false);
        priceChart.setTouchEnabled(true);
        priceChart.getXAxis().setDrawGridLines(false);


        ArrayList<Entry> yValues = new ArrayList<>();

        // Insert the values here

        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        priceChart.setData(data);
    }

    private void fetchData() {
        compositeDisposable.add(bitStampAPI.getTransaction("btcusd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions -> displayData(transactions)));
    }

    private void displayData(List<Transaction> transactions) {

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_order_book:
                        selectedFragment = new OrderBookFragment();
                        break;
                    case R.id.nav_alerts:
                        selectedFragment = new AlertsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
