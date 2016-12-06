package org.twinone.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import wewe.app.moboost.R;


/**
 * Created by Android on 4/26/2016.
 */
public class Mobile_storage extends Activity  {
    private UIView mView;
    RelativeLayout relativeLayout5,relativeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_storage);

        relativeLayout5 =(RelativeLayout) findViewById(R.id.relativeLayout5);
        relativeUp =(RelativeLayout) findViewById(R.id.relativeUp);


        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mView = (UIView) getFragmentManager().findFragmentById(R.id.file_list);

        relativeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.goBack();

            }
        });

    }

//    @Override
//    public void onBackPressed() {
//
//        mView.goBack();
//
//    }
}