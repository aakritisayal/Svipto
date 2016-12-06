package org.twinone.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import org.json.JSONObject;

import java.text.DecimalFormat;

import wewe.app.moboost.R;

public class DashClockWeatherExtension extends DashClockExtension {
    private static final Uri URI_BASE = Uri.parse("content://wewe.app.moboost.authority");
    private static final String UPDATE_URI_PATH_SEGMENT = "dashclock/update";

    @Override
    protected void onInitialize(boolean isReconnect) {
        super.onInitialize(isReconnect);

        // Watch for weather updates
        removeAllWatchContentUris();
        addWatchContentUris(new String[]{getUpdateUri().toString()});
    }

    @Override
    protected void onUpdateData(int reason) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String result = sp.getString("lastToday", "{}");
        try {
            JSONObject reader = new JSONObject(result);

            float temperature = Float.parseFloat(reader.optJSONObject("main").getString("temp").toString());
            if (sp.getString("unit", "C").equals("C")) {
                temperature = temperature - 273.15f;
            }

            if (sp.getString("unit", "C").equals("F")) {
                temperature = (((9 * (temperature - 273.15f)) / 5) + 32);
            }

            double wind = Double.parseDouble(reader.optJSONObject("wind").getString("speed").toString());
            if (sp.getString("speedUnit", "m/s").equals("kph")) {
                wind = wind * 3.59999999712;
            }

            if (sp.getString("speedUnit", "m/s").equals("mph")) {
                wind = wind * 2.23693629205;
            }

            double pressure = Double.parseDouble(reader.optJSONObject("main").getString("pressure").toString());
            if (sp.getString("pressureUnit", "hPa").equals("kPa")) {
                pressure = pressure / 10;
            }
            if (sp.getString("pressureUnit", "hPa").equals("mm Hg")) {
                pressure = pressure * 0.750061561303;
            }

            WeatherReport.initMappings();
            publishUpdate(new ExtensionData()
                    .visible(true)
                    .icon(R.drawable.ic_cloud_white_18dp)
                    .status(getString(R.string.dash_clock_status, new DecimalFormat("#.#").format(temperature), localize(sp, "unit", "C")))
                    .expandedTitle(getString(R.string.dash_clock_expanded_title, new DecimalFormat("#.#").format(temperature), localize(sp, "unit", "C"), reader.optJSONArray("weather").getJSONObject(0).getString("description")))
                    .expandedBody(getString(R.string.dash_clock_expanded_body, reader.getString("name"), reader.optJSONObject("sys").getString("country"),
                            new DecimalFormat("#.0").format(wind), localize(sp, "speedUnit", "m/s"),
                            new DecimalFormat("#.0").format(pressure), localize(sp, "pressureUnit", "hPa"),
                            reader.optJSONObject("main").getString("humidity")))
                    .clickIntent(new Intent(this, WeatherReport.class)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String localize(SharedPreferences sp, String preferenceKey, String defaultValueKey) {
        return WeatherReport.localize(sp, this, preferenceKey, defaultValueKey);
    }

    public static void updateDashClock(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.notifyChange(getUpdateUri(), null);
    }

    private static Uri getUpdateUri() {
        return Uri.withAppendedPath(URI_BASE, UPDATE_URI_PATH_SEGMENT);
    }

}
