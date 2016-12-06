package org.twinone.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.*;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.twinone.SplashActivity;
import org.twinone.activity.CleanerActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/19/2016.
 */
public class Main_navigation extends FragmentActivity {

LinearLayout  me,tools, home;


    InterstitialAd interstitial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_navigation);


        String device_id = android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        home = (LinearLayout) findViewById(R.id.home_main);
        tools = (LinearLayout) findViewById(R.id.tools);
        me = (LinearLayout) findViewById(R.id.me);

        interstitial = new InterstitialAd(Main_navigation.this);
        interstitial.setAdUnitId("ca-app-pub-9480918527727973/6799620841");

        AdView adView = (AdView) this.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()

                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(device_id)
                .build();

        adView.loadAd(adRequest);

        interstitial.loadAd(adRequest);


        HomeActivity fragmentA = new HomeActivity();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragments, fragmentA);
        transaction.commit();

        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                displayInterstitial();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                HomeActivity homeActivity = new HomeActivity();
                transaction.replace(R.id.fragments, homeActivity);
                transaction.commit();
            }
        });

        tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Tools homeActivity = new Tools();
                transaction.replace(R.id.fragments, homeActivity);
                transaction.commit();
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Me_activity homeActivity = new Me_activity();
                transaction.replace(R.id.fragments, homeActivity);
                transaction.commit();
            }
        });

    }



//    public void selectFrag(View view) {
//
//        Fragment fr;
//
//        if(view == findViewById(R.id.home_main)) {
//
//            fr = new HomeActivity();
//
//
//        }
//
//      else  if(view == findViewById(R.id.me)) {
//
//            fr = new Me_activity();
//
//
//
//        }else {
//
//            fr = new Tools();
//
//        }
//
//        android.support.v4.app.FragmentManager fm = Main_navigation.this.getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction fragtr = fm.beginTransaction();
//
//        fragtr.replace(R.id.fragments, fr);
//        fragtr.commit();
//
//
//
//
//    }



public void displayInterstitial() {
    // If Ads are loaded, show Interstitial else show nothing.
    if (interstitial.isLoaded()) {
        interstitial.show();
    }
}


}



