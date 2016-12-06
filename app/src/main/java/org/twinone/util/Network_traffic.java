package org.twinone.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/30/2016.
 */
public class Network_traffic extends Activity {

    RelativeLayout back;
    String speed;
    TextView txtCurrentNetwork;

    private long mStartRX = 0;

    private long mStartTX = 0;
    List<Integer> listUid = new ArrayList<Integer>();
    List<Long> listUidRxBytes = new ArrayList<Long>();
    List<Long> listUidTxBytes = new ArrayList<Long>();
    ArrayList<String> listpackageNm = new ArrayList<String>();
    LinearLayout id_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_traffic);

        txtCurrentNetwork = (TextView) findViewById(R.id.txtCurrentNetwork);
        id_linear = (LinearLayout) findViewById(R.id.id_linear);

        NetworkInfo network = Check_netword.getNetworkInfo(Network_traffic.this);

        boolean h = Check_netword.isConnectedFast(Network_traffic.this);


        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {


            if ((packageInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {

                String packageNm = packageInfo.processName;


            } else {

                String packagename = packageInfo.processName;
                listpackageNm.add(packagename);


                final Drawable icon = getPackageManager().getApplicationIcon(packageInfo);
                String name = getPackageManager().getApplicationLabel(packageInfo).toString();

                //get the UID for the selected app
                int UID = packageInfo.uid;


                    listUid.add(UID);
                    mStartRX = TrafficStats.getUidRxBytes(UID);
                    listUidRxBytes.add(mStartRX);

                    mStartTX = TrafficStats.getUidTxBytes(UID);
                    listUidTxBytes.add(mStartTX);


                    String SIZES = Common_methods.formatSize(mStartRX);

                    LayoutInflater inflater = getLayoutInflater();
                    View v = inflater.inflate(R.layout.custom_junk_files, null);
                    TextView cache_size = (TextView) v.findViewById(R.id.cache_size);
                    TextView app_name = (TextView) v.findViewById(R.id.app_name);
                    ImageView app_icon = (ImageView) v.findViewById(R.id.app_icon);
                    LinearLayout id_linear1 = (LinearLayout) v.findViewById(R.id.id_linear);
                    final CheckBox check_junk = (CheckBox) v.findViewById(R.id.check_junk);
                    check_junk.setVisibility(View.GONE);


                if (mStartTX == 0) {

                    id_linear1.setVisibility(View.GONE);
                }
                else{
                    id_linear1.setVisibility(View.VISIBLE);

                    app_icon.setImageDrawable(icon);
                    app_name.setText(name);
                    cache_size.setText(SIZES);
                    id_linear.addView(v);
                }

            }


        }

        Log.e("pacakges_name", "" + listpackageNm);
        Log.e("listUidRxBytes", "" + listUidRxBytes);
        Log.e("listUidTxBytes", "" + listUidTxBytes);


        int g = network.getType();
        int gs = network.getSubtype();

        speed = Check_netword.isConnectionFast(network.getType(), network.getSubtype());

        txtCurrentNetwork.setText("Current Network: " + speed);


        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });
    }

//    public void networkUsed(){
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
//
//        for(ActivityManager.RunningAppProcessInfo runningApp : runningApps){
//            // Get UID of the selected process
//
//            int uid = ((ActivityManager.RunningAppProcessInfo)this.getListAdapter().getItem(position)).uid;
//
//           // int uid = ((ActivityManager.RunningAppProcessInfo)getListAdapter().getItem(position)).uid;
//
//            // Get traffic data
//            long received = TrafficStats.getUidRxBytes(uid);
//            long send   = TrafficStats.getUidTxBytes(uid);
//            Log.e("" + uid, "Send :" + send + ", Received :" + received);
//        }
//    }


}
