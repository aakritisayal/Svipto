package org.twinone.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import wewe.app.moboost.R;

/**
 * Created by Android on 7/5/2016.
 */
public class AddLocation extends Activity implements AdapterView.OnItemClickListener {
    //http://maps.googleapis.com/maps/api/geocode/json?address=activecraft%20mohali&sensor=true
    private static final String LOG_TAG = "Add location";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/geocode";
    private static final String TYPE_AUTOCOMPLETE = "/json?";
    static String lat;
    Button btn_login;
    static String lng;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    RelativeLayout relativeLayout5;
    AutoCompleteTextView autoCompView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlocation);

        btn_login = (Button) findViewById(R.id.btn_login);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);

        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        sp = getSharedPreferences("shared_getplaces", MODE_MULTI_PROCESS);
        edt = sp.edit();

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setText("");
        autoCompView.setSelectAllOnFocus(true);
        int id = R.drawable.ic_launcher;


        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.custom_textview));
        autoCompView.setOnItemClickListener(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = autoCompView.getText().toString();

                edt.remove("result");
                edt.putString("place", str);
                edt.commit();

                Intent it = new Intent(AddLocation.this, WeatherReport.class);
                startActivity(it);
                finish();

            }
        });
    }


//    public void onBackPressed() {
//        Intent it = new Intent(LocationFind.this,DemoMAP.class);
//        startActivity(it);
//        finish();
//    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //       Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

        edt.remove("result");
        edt.remove("straddress");
        edt.putString("place", str);
        edt.commit();

        Intent it = new Intent(AddLocation.this, WeatherReport.class);
        startActivity(it);
        finish();

    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;
        ArrayList<String> str = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE);
            //	sb.append("?key=" + API_KEY);

            sb.append("address=" + URLEncoder.encode(input, "utf8"));
            sb.append("&sensor=true");


            URL url = new URL(sb.toString());

            Log.e("urlllllllllll", "" + url);
            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            resultList = new ArrayList<String>(predsJsonArray.length());
            str = new ArrayList<String>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("address_components"));

                resultList.add(predsJsonArray.getJSONObject(i).getString("formatted_address"));
                JSONObject location = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");

                lat = location.optString("lat");
                lng = location.optString("lng");

            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public android.widget.Filter getFilter() {
            android.widget.Filter filter = new android.widget.Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;

                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }


}
