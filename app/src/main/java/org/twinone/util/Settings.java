package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/7/2016.
 */
public class Settings extends Activity {
    String Settings;
    LinearLayout images, linear_video, linearAudio, listDocuments, mobileStorage;
    RelativeLayout relativeLayout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Intent it = getIntent();

        String videos = it.getStringExtra("videos");
        String photos = it.getStringExtra("photos");

        if (videos != null) {

            Log.e("videos", "videois.................");
            Intent itv = new Intent(getApplicationContext(), My_photos.class);
            itv.putExtra("videos", "videos");
            startActivity(itv);
        } else if (photos != null) {

            Log.e("photos", "photos.................");
            Intent itv = new Intent(getApplicationContext(), My_photos.class);
            startActivity(itv);
        }

        images = (LinearLayout) findViewById(R.id.images);
        linear_video = (LinearLayout) findViewById(R.id.linear_video);
        linearAudio = (LinearLayout) findViewById(R.id.linearAudio);
        listDocuments = (LinearLayout) findViewById(R.id.listDocuments);
        mobileStorage = (LinearLayout) findViewById(R.id.mobileStorage);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);


        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mobileStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), Mobile_storage.class);
                startActivity(it);
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), My_photos.class);
                startActivity(it);
            }
        });

        linear_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), My_photos.class);
                it.putExtra("videos", "videos");
                startActivity(it);
            }
        });

        linearAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Audio.class);
                startActivity(it);
            }
        });

        listDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Document.class);
                startActivity(it);
            }
        });
    }
}
