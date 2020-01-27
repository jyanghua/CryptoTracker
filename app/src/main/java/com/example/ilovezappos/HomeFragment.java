package com.example.ilovezappos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ilovezappos.Model.Transaction;
import com.example.ilovezappos.Retrofit.IBitStampAPI;
import com.example.ilovezappos.Retrofit.RetrofitClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private IBitStampAPI bitStampAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LineChart priceChart;
    private LineDataSet btcusdDataSet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize API
        Retrofit retrofit = RetrofitClient.getInstance();
        bitStampAPI = retrofit.create(IBitStampAPI.class);

        fetchData();

        Switch switchGrid = view.findViewById(R.id.switch_grid);
        switchGrid.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The toggle is enabled
                priceChart.getXAxis().setDrawGridLines(true);
                priceChart.getAxisLeft().setDrawGridLines(true);
                priceChart.getAxisRight().setDrawGridLines(true);

                // Refresh the chart
                priceChart.invalidate();
            } else {
                // The toggle is disabled
                priceChart.getXAxis().setDrawGridLines(false);
                priceChart.getAxisLeft().setDrawGridLines(false);
                priceChart.getAxisRight().setDrawGridLines(false);

                // Refresh the chart
                priceChart.invalidate();
            }
        });

        Switch switchPrices = view.findViewById(R.id.switch_prices);
        switchPrices.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The toggle is enabled
                btcusdDataSet.setDrawValues(true);

                // Refresh the chart
                priceChart.invalidate();
            } else {
                // The toggle is disabled
                btcusdDataSet.setDrawValues(false);

                // Refresh the chart
                priceChart.invalidate();
            }
        });

        return view;
    }

    /**
     * Function that gets the transaction data from BitStamp's API through a GET Request.
     * Retrieves a list containing all the transactions done in the past 24h
     * related to the pair BTC/USD.
     * Implemented using RxJava and Retrofit.
     */
    private void fetchData() {
        compositeDisposable.add(bitStampAPI.getTransaction("btcusd", "day")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions -> displayData(transactions)));
    }

    /**
     * Function that displays the price data of BTC/USD on the Line Chart.
     * Price calculation is simplified, and to avoid crowding the chart
     * with a lot of data points, the time spacing used between every price point
     * is approximately every 16 minutes yielding an approximate of 80-90 data points
     * for visualization.
     * Implemented using MPAndroidCharts.
     * @param transactions the list of transactions from the past 24 hours
     */
    private void displayData(List<Transaction> transactions) {

        // Line Chart with MPAndroidChart, styling and other misc. configuration
        priceChart = getView().findViewById(R.id.line_chart);
        priceChart.setDragEnabled(true);
        priceChart.setScaleYEnabled(false);
        priceChart.setScaleXEnabled(true);
        priceChart.setTouchEnabled(true);
        priceChart.setHighlightPerDragEnabled(true);
        priceChart.setHighlightPerTapEnabled(true);
        priceChart.getDescription().setEnabled(false);
        priceChart.getAxisLeft().setEnabled(false);
        priceChart.getXAxis().setDrawGridLines(false);
        priceChart.getAxisRight().setDrawAxisLine(false);
        priceChart.getAxisLeft().setDrawGridLines(false);
        priceChart.getAxisRight().setDrawGridLines(false);
        priceChart.getXAxis().setPosition(BOTTOM);

        // Custom date and time format for the MPAndroidChart X Axis
        // In this case it is only relevant to show the time since it is the last 24 hours.
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Date date = new Date((long) value);
                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                return fmt.format(date);
            }
        };

        priceChart.getXAxis().setValueFormatter(formatter);

        // List of entries that will populate the line chart
        ArrayList<Entry> yValues = new ArrayList<>();

        // Counter to separate the time span between points
        long timeCounter = Long.parseLong(transactions.get(0).getDate()) + 1;

        for(Transaction t: transactions){
            // Using the BTC/USD selling prices
            if (t.getType().equals("1") && timeCounter > Long.parseLong(t.getDate())){
                yValues.add(new Entry(Float.parseFloat(t.getDate()) * 1000L, Float.parseFloat(t.getPrice())));

                // Rough separator of data in seconds to avoid overloading the chart with data.
                timeCounter -= 1000;
            }

        }

        // Sorts the list of entries to avoid an error with LineDataSet
        Collections.sort(yValues, new EntryXComparator());

        // Creation of the data set to populate the chart
        btcusdDataSet = new LineDataSet(yValues, "BTC/USD");

        // Smooth curves of the line graph
        btcusdDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        btcusdDataSet.setCubicIntensity(0.1f);

        // More styling settings for the data set
        btcusdDataSet.setLineWidth(1f);
        btcusdDataSet.setColor(Color.rgb(1,118,189));
        btcusdDataSet.setValueTextSize(9);
        btcusdDataSet.setHighLightColor(Color.rgb(250, 151, 43));
        btcusdDataSet.setHighlightLineWidth(1);

        // Area below the line
        btcusdDataSet.setDrawFilled(true);
        btcusdDataSet.setFillColor(Color. rgb(1,118,189));
        btcusdDataSet.setFillAlpha(80);

        // Remove the circles of each data point
        btcusdDataSet.setDrawCircles(false);

        btcusdDataSet.setDrawValues(false);

        // Creating the line chart
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(btcusdDataSet);
        LineData data = new LineData(dataSets);
        priceChart.setData(data);

        // Refreshes the chart
        priceChart.invalidate();
    }


    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
