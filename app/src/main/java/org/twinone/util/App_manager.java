package org.twinone.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/15/2016.
 */
public class App_manager extends FragmentActivity {
    RelativeLayout back;
    ViewPager pager_fragment;
    ArrayList<Fragment> list;
    LinearLayout linear1, linear2, linear3;
    TextView txtUninsatll, txt_apk, txt_move;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_manager);

        Intent it = getIntent();
        String App_manager = it.getStringExtra("App_manager");


        back = (RelativeLayout) findViewById(R.id.back);
        pager_fragment = (ViewPager) findViewById(R.id.pager_fragment);

        linear1 = (LinearLayout) findViewById(R.id.linear1);
        linear2 = (LinearLayout) findViewById(R.id.linear2);
        linear3 = (LinearLayout) findViewById(R.id.linear3);

        txtUninsatll = (TextView) findViewById(R.id.txtUninsatll);
        txt_apk = (TextView) findViewById(R.id.txt_apk);
        txt_move = (TextView) findViewById(R.id.txt_move);


        list = new ArrayList<Fragment>();

        list.add(new Uninsatll_fragment());
        list.add(new Move_fragment());
        list.add(new Apk_fragment());


        if (App_manager != null) {
            pager_fragment.setCurrentItem(1);
        } else {
            pager_fragment.setCurrentItem(0);
        }


        txtUninsatll.setText(Html.fromHtml("<u>UNINSTALL</u>"));
        txt_apk.setText("APK FILES");
        txt_move.setText("MOVE");

        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager_fragment.setCurrentItem(0);
                txtUninsatll.setText(Html.fromHtml("<u>UNINSTALL</u>"));
                txt_apk.setText("APK FILES");
                txt_move.setText("MOVE");

            }
        });

        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager_fragment.setCurrentItem(1);
                txtUninsatll.setText("UNINSTALL");
                txt_apk.setText(Html.fromHtml("<u>APK FILES</u>"));
                txt_move.setText("MOVE");
            }
        });

        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager_fragment.setCurrentItem(2);

                txtUninsatll.setText("UNINSTALL");
                txt_apk.setText("APK FILES");
                txt_move.setText(Html.fromHtml("<u>MOVE</u>"));
            }
        });


        pager_fragment.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == 0) {
                    txtUninsatll.setText(Html.fromHtml("<u>UNINSTALL</u>"));
                    txt_apk.setText("APK FILES");
                    txt_move.setText("MOVE");
                }
                if (position == 1) {
                    txtUninsatll.setText("UNINSTALL");
                    txt_apk.setText(Html.fromHtml("<u>APK FILES</u>"));
                    txt_move.setText("MOVE");
                }
                if (position == 2) {
                    txtUninsatll.setText("UNINSTALL");
                    txt_apk.setText("APK FILES");
                    txt_move.setText(Html.fromHtml("<u>MOVE</u>"));
                }

            }

            public void onPageSelected(int position) {
            }
        });


        pager_fragment.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {

                Log.e("count","" +i);
                return list.get(i);


            }

            @Override
            public int getCount() {


                return list.size();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
