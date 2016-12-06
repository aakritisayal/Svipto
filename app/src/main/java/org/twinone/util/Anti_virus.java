package org.twinone.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/15/2016.
 */
public class Anti_virus extends Activity {
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anti_virus);
        back = (RelativeLayout) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Anti_virus.this, Main_navigation.class);
                startActivity(it);
                finish();
            }
        });


        sendBroadcast (new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()))
        );

    }


    final class MyMediaScannerConnectionClient
            implements MediaScannerConnection.MediaScannerConnectionClient {

        private String mFilename;
        private String mMimetype;
        private MediaScannerConnection mConn;

        public MyMediaScannerConnectionClient
                (Context ctx, File file, String mimetype) {
            this.mFilename = file.getAbsolutePath();
            mConn = new MediaScannerConnection(ctx, this);
            mConn.connect();
        }
        @Override
        public void onMediaScannerConnected() {
            mConn.scanFile(mFilename, mMimetype);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mConn.disconnect();
        }
    }




    private String UsersDestination(Context context, ActivityManager as)
    {
        as = (ActivityManager) context
                .getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RecentTaskInfo> rtiList = as.getRecentTasks(1000,
                ActivityManager.RECENT_WITH_EXCLUDED);

        for (ActivityManager.RecentTaskInfo rti : rtiList)
        {
            if (rti.baseIntent != null && rti.baseIntent.getComponent() != null &&
                    rti.baseIntent.getComponent().getClassName() !=
                            null &&

                    rti.baseIntent.getAction().equals(Intent.ACTION_VIEW)
                    && rti.baseIntent
                    .getComponent()
                    .getClassName()
                    .equalsIgnoreCase(

                            "com.google.android.maps.driveabout.app.NavigationActivity"))
            {
                rti.baseIntent.getData().toString();
                String addressURI =
                        rti.baseIntent.getData().toString();
                System.out.println("AddressURI: " + addressURI);

                String googleNav = "google.navigation:";
                String titleNav = "title=";
                String queryNav = "&q=";
                if(addressURI.contains(queryNav))
                {
                    addressURI =
                            addressURI.substring(addressURI.indexOf(titleNav),
                                    addressURI.indexOf(queryNav));
                    addressURI =
                            addressURI.substring(titleNav.length());
                    addressURI = addressURI.replaceAll("\\+", " ");
                }
                else if(addressURI.contains(titleNav))
                {
                    addressURI =
                            addressURI.substring(addressURI.indexOf(titleNav));
                    addressURI =
                            addressURI.substring(titleNav.length());
                    addressURI = addressURI.replaceAll("\\+", " ");
                }
                else if(addressURI.contains(googleNav))
                {
                    addressURI =
                            addressURI.substring(addressURI.indexOf(googleNav));
                    addressURI =
                            addressURI.substring(googleNav.length());
                    addressURI = addressURI.replaceAll("\\+", " ");
                }

                return addressURI;
            }
        }
        return "";
    }
    private boolean IsNavigationRunning(ActivityManager as)
    {

        as = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rutiList = as.getRunningTasks(100);

        for (ActivityManager.RunningTaskInfo ruti : rutiList)
        {
            if (ruti.baseActivity
                    .getClassName()
                    .equalsIgnoreCase(

                            "com.google.android.maps.driveabout.app.NavigationActivity")
                    &&
                    ruti.baseActivity.getPackageName().equalsIgnoreCase(
                            "com.google.android.apps.maps"))
            {
                return true;
            }
        }

        return false;
    }



}
