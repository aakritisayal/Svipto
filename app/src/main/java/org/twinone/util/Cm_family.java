package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/29/2016.
 */
public class Cm_family extends Activity {
    RelativeLayout back;
    private static final String DFP_AD_UNIT_ID = "ca-app-pub-9480918527727973/8136753243";
    private static final String ADMOB_APP_ID ="ca-app-pub-9480918527727973~5322887640";


    LinearLayout linearCmFamily;
    Button download, download2, download3,download4,download5;
  //  String device_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cm_family);

   //    device_id = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        MobileAds.initialize(this, ADMOB_APP_ID);


        back = (RelativeLayout) findViewById(R.id.back);
        linearCmFamily = (LinearLayout) findViewById(R.id.linearCmFamily);

        download = (Button) findViewById(R.id.download);
        download2 = (Button) findViewById(R.id.download2);
        download3 = (Button) findViewById(R.id.download3);
        download4 = (Button) findViewById(R.id.download4);
        download5 = (Button) findViewById(R.id.download5);

        NativeExpressAdView frameLayout = (NativeExpressAdView) findViewById(R.id.adView);
        NativeExpressAdView frameLayout1 = (NativeExpressAdView) findViewById(R.id.adView1);

        frameLayout.loadAd(new AdRequest.Builder().build());
        frameLayout1.loadAd(new AdRequest.Builder().build());


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goPlaystore();
            }
        });
        download2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goPlaystore();
            }
        });
        download3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goPlaystore();
            }
        });

        download4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goPlaystore();
            }
        });
        download5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goPlaystore();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void goPlaystore() {
        String appPackageName = "com.thirdrock.fivemiles";

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }




//    public void openNative(final FrameLayout frameLayout){
//        AdLoader.Builder builder = new AdLoader.Builder(this, DFP_AD_UNIT_ID);
//
////        AdRequest builder = new AdRequest.Builder()
////                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
////                        // Check the LogCat to get your test device ID
////                .addTestDevice(device_id)
////                .build();
//
//            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
//                @Override
//                public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
//
//                    NativeAppInstallAdView adView = (NativeAppInstallAdView) getLayoutInflater()
//                            .inflate(R.layout.ad_app_install, null);
//                    populateAppInstallAdView(ad, adView);
//                    frameLayout.removeAllViews();
//                    frameLayout.addView(adView);
//                }
//            });
//        AdLoader adLoader = builder.withAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(Cm_family.this, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
//            }
//        }).build();
//
//        adLoader.loadAd(new PublisherAdRequest.Builder().build());
//    }



//    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
//                                          NativeAppInstallAdView adView) {
//        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
//        adView.setImageView(adView.findViewById(R.id.appinstall_image));
//        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
//        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
//        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
//        adView.setStoreView(adView.findViewById(R.id.appinstall_store));
//
//        // Some assets are guaranteed to be in every NativeAppInstallAd.
//        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
//        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
//        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
//        ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
//                .getDrawable());
//
//        List<NativeAd.Image> images = nativeAppInstallAd.getImages();
//
//        if (images.size() > 0) {
//            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
//        }
//
//        // Some aren't guaranteed, however, and should be checked.
//        if (nativeAppInstallAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
//        }
//
//        if (nativeAppInstallAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
//        }
//
//        if (nativeAppInstallAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//
//        // Assign native ad object to the native view.
//        adView.setNativeAd(nativeAppInstallAd);
//    }
}


