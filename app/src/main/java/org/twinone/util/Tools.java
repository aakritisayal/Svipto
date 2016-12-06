package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.twinone.SplashActivity;

import wewe.app.moboost.R;


/**
 * Created by Android on 3/20/2016.
 */
public class Tools extends Fragment {

    LinearLayout id_battery_saver, cpu_cooler, my_files, space_cleaner, app_manager;
    LinearLayout i_swipe, app_lock, check_network_state,id_cm_family;
    String speed;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {

        View view = inflate.inflate(R.layout.tools,null);
        id_cm_family =(LinearLayout) view.findViewById(R.id.id_cm_family);
        id_battery_saver = (LinearLayout) view.findViewById(R.id.id_battery_saver);
        cpu_cooler = (LinearLayout) view.findViewById(R.id.cpu_cooler);
        my_files = (LinearLayout) view.findViewById(R.id.my_files);
        space_cleaner = (LinearLayout) view.findViewById(R.id.space_cleaner);
        app_manager = (LinearLayout) view.findViewById(R.id.app_manager);
        i_swipe = (LinearLayout) view.findViewById(R.id.i_swipe);

        app_lock = (LinearLayout) view.findViewById(R.id.app_lock);
        check_network_state = (LinearLayout) view.findViewById(R.id.check_network_state);


        id_cm_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(),Cm_family.class);
                startActivity(it);
            }
        });


        check_network_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), Network_traffic.class);
                startActivity(it);

            }
        });

        id_battery_saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), Home_battery_saver.class);
                it.putExtra("battery_saver", "battery_saver");
                startActivity(it);

            }
        });
        cpu_cooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), Cpu_cooler.class);

                startActivity(it);

            }
        });

        my_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), Settings.class);
                it.putExtra("Settings", "Settings");
                startActivity(it);
            }
        });


        space_cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Home_activity.class);
                it.putExtra("Home_activity", "Home_activity");
                startActivity(it);


            }
        });


        app_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), App_manager.class);
                startActivity(it);

            }
        });

//        i_swipe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(getActivity(), I_swipe.class);
//                it.putExtra("I_swipe", "I_swipe");
//                startActivity(it);
//                finish();
//            }
//        });


        app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), SplashActivity.class);
                it.putExtra("Tools_app_lock", "Tools_app_lock");
                startActivity(it);
                //   finish();

            }
        });

        return  view;

    }



}
