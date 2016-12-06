package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import wewe.app.moboost.R;

/**
 * Created by Android on 4/21/2016.
 */
public class Cleared extends Activity {
    Button done;
    String no_apps,cpu_temp,junk_cleared,phone_boost;
    TextView txtOptimized;
    public static  int addSum=0;
    String adds;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleared);

        Intent it = getIntent();

        String adds = it.getStringExtra("add");

        String device_id = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        if(adds!=null){
            mInterstitialAd = new InterstitialAd(this);

            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            // Check the LogCat to get your test device ID
                    .addTestDevice(device_id)
                    .build();

            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }

                @Override
                public void onAdClosed() {
                   // Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLeftApplication() {
                 //   Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
                   // Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
                }
            });
        }




        txtOptimized=(TextView) findViewById(R.id.txtOptimized);



        no_apps =it.getStringExtra("no_apps");

        cpu_temp =it.getStringExtra("cpu_temp");

        junk_cleared =it.getStringExtra("junk_cleared");

        phone_boost=it.getStringExtra("phone_boost");

        if(phone_boost!=null){
            txtOptimized.setText("Boosted");
        }


        if(junk_cleared!=null){

            txtOptimized.setText("Junk Cleared");

        }

        if(no_apps!=null){

            txtOptimized.setText("No spps, Congrats");
            
        }

        if(cpu_temp!=null){
            txtOptimized.setText("CPU temperature is OK");
        }


        done =(Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}
