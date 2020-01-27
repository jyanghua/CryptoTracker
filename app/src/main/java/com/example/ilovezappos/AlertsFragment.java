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

        textPriceAlert = view.findViewById(R.id.textPriceAlert);
        editPriceAlert = view.findViewById(R.id.editPriceAlert);
        setButton = view.findViewById(R.id.setButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        setButton.setOnClickListener(v -> {
            textPriceAlert.setText(editPriceAlert.getText().toString());
            saveData();
            scheduleJob(v);
        });

        cancelButton.setOnClickListener(v -> {
            textPriceAlert.setText("");
            deleteData();
            cancelJob(v);
        });

        loadData();
        updateFragment();
        return view;
    }

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

    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) v.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(001);
        Log.d(TAG, "Job Cancelled");
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PRICE, textPriceAlert.getText().toString());
        editPriceAlert.setText("");
        editor.apply();

        Toast.makeText(this.getContext(), "Alert is set", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        price = sharedPreferences.getString(PRICE, "");
    }

    public void deleteData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PRICE);
        editor.apply();

        Toast.makeText(this.getContext(), "Alert is canceled", Toast.LENGTH_SHORT).show();
    }

    public void updateFragment() {
        textPriceAlert.setText(price);
    }
}
