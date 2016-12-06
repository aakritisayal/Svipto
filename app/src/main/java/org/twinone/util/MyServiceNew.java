package org.twinone.util;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MyServiceNew extends Service{
    String deviceId, latitude, longitude, message, response, straddress;

    NotificationManager mManager;
    boolean connected = false;

    GPSTracker gps;
    SharedPreferences preferences;
    boolean bn = false;
    Editor editor;
    boolean min = false;
    List<Address> addresses = null;
    SharedPreferences sp;
    Editor edt;
    GoogleApiClient mGoogleApiClient;


    /*
     * private static final ScheduledExecutorService worker =
     * Executors.newSingleThreadScheduledExecutor();
     */
    public MyServiceNew() {

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
                Local.save(MyServiceNew.this,"clear","");
            }
        }.start();

      /*  CountDownTimer t = new CountDownTimer(Long.MAX_VALUE, 40000) {

            // This is called every interval. (Every 10 seconds in this example)
            public void onTick(long millisUntilFinished) {

                double seconds = millisUntilFinished / 1000.0;

                if(seconds  >9)
                {
                    if(Local.fetch(MyServiceNew.this,"clear").contains(""))
                    {
                        Local.save(MyServiceNew.this,"clear","true");
                    }else
                    {
                        Local.save(MyServiceNew.this,"clear","false");
                    }
                }





            }

            public void onFinish() {

            }
        }.start();*/

        return START_STICKY;
    }

    public class GetTemperature extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            //String name ="Sector 40 Market Rd, Sector 40B, Chandigarh, 160036";
            String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + straddress + "%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            url = url.replaceAll(" ", "%20");

            DefaultHttpClient dhc = new DefaultHttpClient();

            ResponseHandler<String> res = new BasicResponseHandler();

            HttpGet get = new HttpGet(url);

            try {

                response = dhc.execute(get, res);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {

                try {
                    JSONObject obj = new JSONObject(s);

                    JSONObject query = obj.getJSONObject("query");

                    JSONObject results = query.getJSONObject("results");

                    JSONObject channel = results.getJSONObject("channel");

                    JSONObject location = channel.getJSONObject("location");

                    String city = location.getString("city");
                    String country = location.getString("country");

                    JSONObject item = channel.getJSONObject("item");

                    JSONArray forecast = item.getJSONArray("forecast");

                    for (int i = 0; i < forecast.length(); i++) {

                        JSONObject objectToday = forecast.getJSONObject(0);
                        String hightoday = objectToday.getString("high");
                        String texttoday = objectToday.getString("text");


                        hightoday = convertToCelc(hightoday);
                        editor.putString("text", texttoday);
                        editor.putString("high", hightoday);
                        editor.putString("city", city);
                        edt.commit();

                        JSONObject object = forecast.getJSONObject(i);
                        String day = object.getString("day");
                        String high = object.getString("high");

                        high = convertToCelc(high);

                        String low = object.getString("low");
                        String text = object.getString("text");


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                editor.putString("result", s);
                editor.commit();

                edt.putString("address", s);
                edt.commit();
            } else {
                Log.e("Something went wrong", "eeeeeee");
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public String convertToCelc(String temp) {
        int temperatue = Integer.valueOf(temp);
        temperatue = (temperatue - 32) * 5 / 9;

        String getTemp = String.valueOf(temperatue) + " \u2103";


        return getTemp;
    }


}
