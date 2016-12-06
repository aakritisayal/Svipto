package org.twinone.util;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

/**
 * Created by Android on 5/20/2016.
 */
public class MyServiceBattery extends Service {
    String deviceId, latitude, longitude, message, response, straddress;

    NotificationManager mManager;
    boolean connected = false;

    GPSTracker gps;
    SharedPreferences preferences;
    boolean bn = false;
    SharedPreferences.Editor editor;
    boolean min = false;
    List<Address> addresses = null;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    GoogleApiClient mGoogleApiClient;


    /*
     * private static final ScheduledExecutorService worker =
     * Executors.newSingleThreadScheduledExecutor();
     */
    public MyServiceBattery() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        /* throw new UnsupportedOperationException("Not yet implemented"); */
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        gps = new GPSTracker(this);

        preferences = getSharedPreferences("list_index", Context.MODE_PRIVATE);
        editor = preferences.edit();

        sp = getSharedPreferences("Shared_value", MODE_MULTI_PROCESS);
        edt = sp.edit();


        deviceId = android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generat

        Random r = new Random();
        int i1 = r.nextInt(900000 - 600000) + 600000;

        new CountDownTimer(i1, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Local.save(MyServiceBattery.this,"clear_battery","");
            }
        }.start();


        return START_STICKY;
    }



}
