package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/15/2016.
 */
public class I_swipe extends Activity {
    RelativeLayout back;
    String I_swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_swipe);

        Intent it = getIntent();
        I_swipe =it.getStringExtra("I_swipe");

        back =(RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(I_swipe !=null){
                    Intent it = new Intent(I_swipe.this, Tools.class);
                    startActivity(it);
                    finish();
                }
                else{
                    Intent it = new Intent(I_swipe.this, Main_navigation.class);
                    startActivity(it);
                    finish();
                }

            }
        });
    }
}
