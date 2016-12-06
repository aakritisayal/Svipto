package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import wewe.app.moboost.R;

/**
 * Created by Android on 4/25/2016.
 */
public class Display_image extends Activity {

    String path;
    ImageView img_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image);

        img_delete =(ImageView) findViewById(R.id.img_delete);

        Intent it = getIntent();

        path =it.getStringExtra("path");

        File imgFile = new  File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            Log.e("bitmap", "" + myBitmap);

            img_delete.setImageBitmap(myBitmap);

        }
    }
}
