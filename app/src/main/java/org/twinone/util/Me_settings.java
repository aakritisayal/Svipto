package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/29/2016.
 */
public class Me_settings extends Activity {
    LinearLayout linearLogout;
    RelativeLayout back;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    LinearLayout about;
    String token, stremail;
    LinearLayout likeUs,linearPrivacy,linearfeedback,id_linear_faq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_settings);

        sp = getSharedPreferences("Shared_value", MODE_MULTI_PROCESS);
        edt = sp.edit();

        token = sp.getString("token", "");
        stremail = sp.getString("stremail", "");

        linearLogout = (LinearLayout) findViewById(R.id.linearLogout);
        about = (LinearLayout) findViewById(R.id.about);
        likeUs =(LinearLayout) findViewById(R.id.likeUs);
        linearPrivacy =(LinearLayout) findViewById(R.id.linearPrivacy);
        linearfeedback =(LinearLayout) findViewById(R.id.linearfeedback);
        id_linear_faq =(LinearLayout) findViewById(R.id.id_linear_faq);

        id_linear_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Me_settings.this,Faq.class);
                startActivity(it);
            }
        });

        linearfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setType("plain/text");
                sendIntent.setData(Uri.parse("support@moboost.com"));
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
              //  sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"support@moboost.com"});
             //   sendIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
              //  sendIntent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my demo app :-)");
                startActivity(sendIntent);



            }
        });


        likeUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myUrl = "https://www.facebook.com/moboostapp/";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(myUrl)) ;
                startActivity(i);
            }
        });

        linearPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Me_settings.this,PrivatePolicyMobost.class);
                startActivity(it);
            }
        });


        if (!token.equals("")) {

            linearLogout.setVisibility(View.VISIBLE);

        } else {
            linearLogout.setVisibility(View.GONE);
        }




        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Me_settings.this, AboutUs.class);
                startActivity(it);

            }
        });

        linearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent it = new Intent(Me_settings.this, Me_settings.class);
                edt.clear();
                edt.apply();
                edt.commit();
                it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(it, 0);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent it = new Intent(Me_settings.this, Me_activity.class);
//                startActivity(it);
                finish();
            }
        });

    }
}
