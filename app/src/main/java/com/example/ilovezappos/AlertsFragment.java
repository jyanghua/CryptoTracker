package com.example.ilovezappos;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AlertsFragment extends Fragment {

    private static final String TAG = "AlertsFragment";

    private TextView textPriceAlert;
    private EditText editPriceAlert;
    private Button setButton;
    private Button cancelButton;

    public static final String SHARED_PREF = "sharedPref";
    public static final String PRICE = "price";

    private String price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);

        // Initialization of variables
        textPriceAlert = view.findViewById(R.id.textPriceAlert);
        editPriceAlert = view.findViewById(R.id.editPriceAlert);
        setButton = view.findViewById(R.id.setButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        // Listener for the button of setting up an alert.
        setButton.setOnClickListener(v -> {
            textPriceAlert.setText(editPriceAlert.getText().toString());
            saveData();
            scheduleJob(v);
        });

        // Listener for the button of canceling an alert.
        cancelButton.setOnClickListener(v -> {
            textPriceAlert.setText("");
            deleteData();
            cancelJob(v);
        });

        // Loads the data that is stored in the shared preferences
        loadData();
        updateFragment();
        return view;
    }

    /**
     * Function that uses the Android Job Scheduler to set a price alert service and
     * check the price of BTC/USD and fires an alert if it is below the threshold.
     * The periodic time for the scheduler is set at 1 hour, where it will check if the
     * condition has been met through Bitstamp's API.
     * @param v the view of the current Fragment
     */
    public void scheduleJob(View v){
        ComponentName componenName = new ComponentName(v.getContext(), PriceAlertJobService.class);
        JobInfo info = new JobInfo.Builder(001, componenName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(60 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) v.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "Job Scheduled");
        } else {
            Log.d(TAG, "Job Failed");
        }
    }

    /**
     * Function that cancels the Job that has been scheduled and also deletes the data related
     * was created to store the price alert. It will reset the alert completely.
     * @param v the view of the current Fragment
     */
    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) v.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(001);
        Log.d(TAG, "Job Cancelled");
    }

    /**
     * Function that saves the price input by the user to the shared preferences of the Android
     * phone.
     */
    public void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRICE, textPriceAlert.getText().toString());
        editPriceAlert.setText("");
        editor.apply();

        Toast.makeText(this.getContext(), "Alert is set", Toast.LENGTH_SHORT).show();
    }

    /**
     * Function that loads the price alert data from the shared preferences of the Android phone.
     */
    public void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        price = sharedPreferences.getString(PRICE, "");
    }

    /**
     * Function that deletes the price alert data in the shared preferences of the Android phone.
     */
    public void deleteData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PRICE);
        editor.apply();

        Toast.makeText(this.getContext(), "Alert is canceled", Toast.LENGTH_SHORT).show();
    }

    /**
     * Function that updates the fragment by setting the text containing the price of the
     * alert set by the user.
     */
    public void updateFragment() {
        textPriceAlert.setText(price);
    }
}
