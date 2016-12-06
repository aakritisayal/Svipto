package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/15/2016.
 */
public class Main_page extends Activity {

    TextView my_cloud, tools, cloud_auto_backup, space_cleaner, photo_cleaner, app_manager, quite_notification, i_swipe, cm_games, tools_app_lock, home_antivirus, battery_saver;
    TextView junked_standard, cm_family, sign_up, settings, tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        my_cloud = (TextView) findViewById(R.id.my_cloud);
        tools = (TextView) findViewById(R.id.tools);
        cloud_auto_backup = (TextView) findViewById(R.id.cloud_auto_backup);
        space_cleaner = (TextView) findViewById(R.id.space_cleaner);
        photo_cleaner = (TextView) findViewById(R.id.photo_cleaner);
        cm_games = (TextView) findViewById(R.id.cm_games);

        app_manager = (TextView) findViewById(R.id.app_manager);
        quite_notification = (TextView) findViewById(R.id.quite_notification);
        i_swipe = (TextView) findViewById(R.id.i_swipe);
        tools_app_lock = (TextView) findViewById(R.id.tools_app_lock);
        home_antivirus = (TextView) findViewById(R.id.home_antivirus);
        battery_saver = (TextView) findViewById(R.id.battery_saver);

        junked_standard = (TextView) findViewById(R.id.junked_standard);
        cm_family = (TextView) findViewById(R.id.cm_family);
        sign_up = (TextView) findViewById(R.id.sign_up);
        settings = (TextView) findViewById(R.id.settings);
        tool = (TextView) findViewById(R.id.tool);

//        my_cloud.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent it = new Intent(Main_page.this, MainActivity.class);
//                startActivity(it);
//            }
//        });

        tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Main_page.this, Settings.class);
                startActivity(it);
            }
        });
        cloud_auto_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Main_page.this, Sign_up.class);
                startActivity(it);
            }
        });

        space_cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Main_page.this, Home_activity.class);
                startActivity(it);
            }
        });

        photo_cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Main_page.this, Photo_cleaner.class);
                startActivity(it);
            }
        });

        app_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Main_page.this, App_manager.class);
                startActivity(it);

            }
        });
//        quite_notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent it = new Intent(Main_page.this, Quite_notification.class);
//                startActivity(it);
//
//            }
//        });

        i_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Main_page.this, I_swipe.class);
                startActivity(it);

            }
        });


        tools_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Main_page.this, Tools_app_lock.class);
                startActivity(it);

            }
        });
        home_antivirus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Main_page.this, Anti_virus.class);
                startActivity(it);
            }
        });


    }
}
