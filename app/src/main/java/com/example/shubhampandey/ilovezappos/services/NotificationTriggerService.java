package com.example.shubhampandey.ilovezappos.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.example.shubhampandey.ilovezappos.R;
import com.example.shubhampandey.ilovezappos.activities.GraphActivity;
import com.example.shubhampandey.ilovezappos.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SHUBHAM PANDEY on 9/9/2017.
 */

public class NotificationTriggerService extends Service {
    SharedPreferences preferences;
    double storedPreference;

    public NotificationTriggerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String storedString = preferences.getString(getString(R.string.stored_price), "");
        if (CommonUtils.isNetworkAvailable(getApplicationContext()) && !TextUtils.isEmpty(storedString)) {
            storedPreference = Double.parseDouble(storedString);
            new LastBTCPriceTask().execute();
        }
        return START_STICKY;
    }


    class LastBTCPriceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return getLastPrice();
        }

        @Override
        protected void onPostExecute(String pushNotif) {
            if (pushNotif != null && !TextUtils.isEmpty(pushNotif)) {
                double value = Double.parseDouble(pushNotif);
                pushNotification(value);
            }
        }
    }

    private void pushNotification(double value) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_msg_prefix) + value)
                        .setAutoCancel(true);

        Intent notificationIntent = new Intent(this, GraphActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
        stopSelf();
    }

    private String getLastPrice() {
        double fetchedPrice;
        JSONObject object = CommonUtils.getJSON(getString(R.string.ticker_api_url));
        if (object != null) {
            try {
                fetchedPrice = object.getDouble(getString(R.string.last));
                if (fetchedPrice > 0 && fetchedPrice < storedPreference) {
                    return "" + fetchedPrice;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
