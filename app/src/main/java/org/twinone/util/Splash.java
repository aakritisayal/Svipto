package org.twinone.util;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/23/2016.
 */
public class Splash extends Activity {

    SharedPreferences sp;
    SharedPreferences.Editor edt;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        sp = getSharedPreferences("Shared_value", MODE_MULTI_PROCESS);
        edt = sp.edit();

        boolean state = usageAccessGranted(this);
        Log.e("stata", "" + state);


        boolean checkUser = check();

        Log.e("checkUser","" +checkUser);



        token = sp.getString("token", "");

     //  showHashKey(this);

       Thread th = new Thread(r);
        th.start();
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                // TODO: handle exception
            }

            if (!token.equals("")) {
                Intent it = new Intent(Splash.this, Main_navigation.class);
                startActivity(it);
                finish();
            } else {
                Intent it = new Intent(Splash.this, Main_navigation.class);
                startActivity(it);
                finish();
            }

        }
    };


    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "wewe.app.moboost", PackageManager.GET_SIGNATURES); //Your package name here
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String key = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                String hashkeys = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("key", "" + hashkeys);

                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }


    public static boolean usageAccessGranted(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public  boolean check(){

        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
