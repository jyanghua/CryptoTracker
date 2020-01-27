package com.example.ilovezappos;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.ilovezappos.Model.TickerHour;
import com.example.ilovezappos.Retrofit.IBitStampAPI;
import com.example.ilovezappos.Retrofit.RetrofitClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PriceAlertJobService extends JobService {
    private static final String TAG = "PriceAlertJobService";

    private boolean jobCancelled = false;

    /**
     * Function that starts the Job Service
     * @param params the parameters that help configuring and identifying the Job
     * @return true if the job started successfully
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Alert job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(JobParameters params) {

        // Configure the channel by setting the importance to high. 001 is the placeholder ID for the job
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("001", "Alerts Channel", importance);
        channel.setDescription("Bitcoin Price Alert");

        // Register the channel with the notifications manager
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);

        // Initialize API
        Retrofit retrofit = RetrofitClient.getInstance();
        IBitStampAPI bitStampAPI = retrofit.create(IBitStampAPI.class);

        // Using RxJava and Retrofit
        bitStampAPI.getTickerHour("btcusd")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<TickerHour>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(TickerHour tickerHour) {
                double lastPrice = Double.parseDouble(tickerHour.getLast());
                double alertPrice = Double.parseDouble(getApplicationContext().getSharedPreferences(
                        AlertsFragment.SHARED_PREF, MODE_PRIVATE).getString(AlertsFragment.PRICE, ""));

                System.out.println("Last Price: "+ lastPrice);
                System.out.println("Alert Price: "+ alertPrice);

                if (lastPrice < alertPrice) {
                    // Fire a notification to the user
                    createNotification(001, R.drawable.ic_notifications_none_black_24dp,"Zappolert!",
                            String.format("The current BTC/USD price is now below %.2f", alertPrice), "001");

                    Log.d(TAG, "Job has finished");
                    jobFinished(params, false);

                } /*else { // Could be used in case the Job Scheduler doesn't work
                    try {
                        Thread.sleep(60 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
            }
        });
    }

    /**
     * Function that creates a simple notification that will send the notice that the price of
     * BTC/USD is now lower than the value set by the user.
     * @param nId Id number of the notification
     * @param iconRes Icon used for the notification
     * @param title Title of the notification
     * @param body Body and text description of the notification
     * @param channelId The channel ID that was created to send the notifications
     */
    private void createNotification(int nId, int iconRes, String title, String body, String channelId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this, channelId).setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body);

        // Fires the notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(nId, mBuilder.build());
    }

    /**
     * Function that will stop the current Job Service.
     * @param params the parameters that help configuring and identifying the Job
     * @return true if the job service has been successfully stopped
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job was cancelled");
        jobCancelled = true;
        return true;
    }
}
