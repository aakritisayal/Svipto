package org.twinone.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/20/2016.
 */
public class Home_junked extends Activity {
    Button btn_clearJunked;
    RelativeLayout back;
    LinearLayout cachejunk;
    TextView cache_size, app_name, idcache_clear, total_size, free_space;
    ImageView app_icon;

    double dbl;
    private static final long CACHE_APP = Long.MAX_VALUE;
    private CachePackageDataObserver mClearCacheObserver;

    List<String> listSize = new ArrayList<String>();
    List<String> listpackageName = new ArrayList<String>();
    List<String> listAppName = new ArrayList<String>();
    List<Drawable> listAppImage = new ArrayList<Drawable>();
    List<String> listMemory = new ArrayList<String>();
    List<String> listInfo = new ArrayList<String>();

    double sizedouble, selectedSize, getSelectedSize;
    ArrayList<Integer> listAppSize = new ArrayList<Integer>();
    ArrayList<String> listPackage = new ArrayList<String>();


    ProgressBar progressBar2;
    TextView txtAdvanced;
    String getPackage;
    ProgressDialog pd;

    String Package_Name = "wewe.app.moboost";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
  //  private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_junked);


        showProgress();

        //    clearApplicationData(this);
        txtAdvanced = (TextView) findViewById(R.id.txtAdvanced);
        btn_clearJunked = (Button) findViewById(R.id.btn_clearJunked);
        back = (RelativeLayout) findViewById(R.id.back);
        cachejunk = (LinearLayout) findViewById(R.id.cachejunk);
        idcache_clear = (TextView) findViewById(R.id.idcache_clear);
        total_size = (TextView) findViewById(R.id.total_size);
        free_space = (TextView) findViewById(R.id.free_space);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        // progressBar2.setMax(100);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Home_junked.this, Main_navigation.class);
                startActivity(it);
                finish();

            }
        });


        txtAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Home_junked.this, Home_activity.class);
                startActivity(it);


            }
        });

        //  getTotalSize();

        GetJunkFiles files = new GetJunkFiles();
        files.execute();

        // new Thread(null, Login_service, "").start();
        //showDialog(progress_id);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    //    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void getTotalSize() {
        // final int[] totalSize = {0};

        PackageManager packageManager = getApplicationContext().getPackageManager();

        List<PackageInfo> packs = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

        int size = packs.size();

        progressBar2.setMax(size);


        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            progressBar2.setProgress(i);


            String packageName = p.packageName;

            listpackageName.add(packageName);

            try {

                final ApplicationInfo app = this.getPackageManager().getApplicationInfo(packageName, 0);
                String name = getPackageManager().getApplicationLabel(app).toString();
                listAppName.add(name);
                Drawable icon = getPackageManager().getApplicationIcon(app);
                listAppImage.add(icon);

                Method getPackageSizeInfo;

                getPackageSizeInfo = packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                getPackageSizeInfo.invoke(packageManager, p.packageName, new IPackageStatsObserver.Stub() {
                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

                        long cacheSize = pStats.cacheSize;


                        long memory = pStats.dataSize;


                        listMemory.add(String.valueOf(memory));


                        // cacheSize = cacheSize*MEGA_BYTE;
                        String size = String.valueOf(cacheSize);
                        listSize.add(size);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        Log.e("info", "" + listInfo);
        Log.e("information", "" + listInfo);


    }


    public String getDataUsage(ApplicationInfo appInfo) {

        int uid = appInfo.uid;

        double received = (double) TrafficStats.getUidRxBytes(uid) / (1024 * 1024);
        double sent = (double) TrafficStats.getUidTxBytes(uid) / (1024 * 1024);

        double total = received + sent;

        return String.format("%.2f", total) + " MB";

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    //    client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Home_junked Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://master.clean.moboost/http/host/path")
//        );
     //   AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Home_junked Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://master.clean.moboost/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    public class GetJunkFiles extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            getTotalSize();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pd.isShowing()) {
                pd.dismiss();
            }


            sizedouble = 0;

            int g = listSize.size();

            Log.e("listmemeory", "" + listMemory);


            for (int i = 0; i < listSize.size(); i++) {

                Drawable d = listAppImage.get(i);
                String appname = listAppName.get(i);
                String size = listSize.get(i);
                String memory = listMemory.get(i);

                dbl = Double.valueOf(size) / 1024;

                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.custom_junk_files, null);
                cache_size = (TextView) v.findViewById(R.id.cache_size);
                app_name = (TextView) v.findViewById(R.id.app_name);
                LinearLayout id_linear = (LinearLayout) v.findViewById(R.id.id_linear);
                app_icon = (ImageView) v.findViewById(R.id.app_icon);
                final CheckBox check_junk = (CheckBox) v.findViewById(R.id.check_junk);
                check_junk.setId(i);
                check_junk.setVisibility(View.GONE);

                if (size.equals("0.0") || size.equals("0") || appname.equals("Moboost")) {

                    id_linear.setVisibility(View.GONE);


                } else {
                    id_linear.setVisibility(View.VISIBLE);
                    app_icon.setImageDrawable(d);
                    app_name.setText(appname);

                    sizedouble = sizedouble + dbl;

                    check_junk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            int id = check_junk.getId();
                            String size = listSize.get(id);
                            String packageNm = listpackageName.get(id);

                            if (isChecked) {


                                listAppSize.add(Integer.valueOf(size));
                                listPackage.add(packageNm);

                                selectedSize = 0;
                                for (int j = 0; j < listAppSize.size(); j++) {

                                    int size1 = listAppSize.get(j);

                                    selectedSize = selectedSize + size1;


                                }

                                selectedSize = Double.valueOf(selectedSize) / 1024;


                                if (selectedSize > 1024) {

                                    selectedSize = selectedSize / 1024;
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    selectedSize = Double.valueOf(df.format(selectedSize));
                                    free_space.setText("Selected: " + String.valueOf(selectedSize) + "MB");


                                } else {

                                    DecimalFormat df = new DecimalFormat("#.##");
                                    selectedSize = Double.valueOf(df.format(selectedSize));
                                    free_space.setText("Selected: " + String.valueOf(selectedSize) + "KB");
                                }


                            }
                            if (!isChecked) {
                                int id1 = check_junk.getId();
                                String size1 = listSize.get(id1);

                                String packageNm1 = listpackageName.get(id1);


                                for (int i = 0; i < listAppSize.size(); i++) {
                                    if (size1.contains(String.valueOf(listAppSize.get(i)))) {
                                        listAppSize.remove(i);
                                        listPackage.remove(i);
                                        getSelectedSize = 0;

                                        for (int j = 0; j < listAppSize.size(); j++) {

                                            int size12 = listAppSize.get(j);

                                            getSelectedSize = getSelectedSize + size12;


                                        }

                                        getSelectedSize = Double.valueOf(getSelectedSize) / 1024;


                                        if (getSelectedSize > 1024) {

                                            getSelectedSize = getSelectedSize / 1024;
                                            DecimalFormat df = new DecimalFormat("#.##");
                                            getSelectedSize = Double.valueOf(df.format(getSelectedSize));
                                            free_space.setText("Selected: " + String.valueOf(getSelectedSize) + "MB");
                                        } else {

                                            DecimalFormat df = new DecimalFormat("#.##");
                                            getSelectedSize = Double.valueOf(df.format(getSelectedSize));
                                            free_space.setText("Selected: " + String.valueOf(getSelectedSize) + "KB");
                                        }


                                        break;
                                    }
                                }

                            }
                        }
                    });


                    if (dbl > 1024) {

                        dbl = dbl / 1024;
                        DecimalFormat df = new DecimalFormat("#.##");
                        dbl = Double.valueOf(df.format(dbl));
                        cache_size.setText(String.valueOf(dbl) + "MB");
                    } else {

                        DecimalFormat df = new DecimalFormat("#.##");
                        dbl = Double.valueOf(df.format(dbl));

                        cache_size.setText(String.valueOf(dbl) + "KB");
                    }
                }

                cachejunk.addView(v);

            }


            btn_clearJunked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    for (int i = 0; i < listPackage.size(); i++) {


                    //          getPackage = listPackage.get(i);

                    clearCache(getPackage);

                    clearAllCache();


//                        try {
//                            deleteCache(createPackageContext(getPackage,CONTEXT_IGNORE_SECURITY));
//                        } catch (PackageManager.NameNotFoundException e) {
//                            e.printStackTrace();
//                        }
                    pd.show();

                    //        }

                    ((ViewGroup) cachejunk.getParent()).removeView(cachejunk);

                    listSize.clear();
                    listpackageName.clear();
                    listAppName.clear();
                    listAppImage.clear();
                    listMemory.clear();
                    listInfo.clear();
                    listAppSize.clear();
                    listPackage.clear();

                    GetJunkFiles files = new GetJunkFiles();
                    files.execute();

                }
            });

            if (sizedouble > 1024) {

                sizedouble = sizedouble / 1024;
                DecimalFormat Format = new DecimalFormat("#.##");
                sizedouble = Double.valueOf(Format.format(sizedouble));
                total_size.setText(String.valueOf(sizedouble) + "MB");

            } else {

                DecimalFormat Format = new DecimalFormat("#.##");
                sizedouble = Double.valueOf(Format.format(sizedouble));
                total_size.setText(String.valueOf(sizedouble) + "KB");
            }


            String cache = total_size.getText().toString();

            if (total_size.getText().equals("0.0KB")) {


                Intent it = new Intent(getApplicationContext(), Cleared.class);
                it.putExtra("junk_cleared", "junk_cleared");
                startActivity(it);
                finish();


            }


            btn_clearJunked.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }


//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//        return dir.delete();
//    }

    //    private void deleteCache(Context context) {
//
//        File app = context.getCacheDir();
//
////        File cache = getCacheDir();
//        File appDir = new File(String.valueOf(app));
//
//        if (appDir.exists()) {
//            String[] children = appDir.list();
//            for (String s : children) {
//             //   if (!s.equals("lib")) {
//                    deleteDir(new File(appDir, s));
//                    Log.e("TAG", "**************** File "+getPackage+" " + s + " DELETED *******************");
//              //  }
//            }
//        }
//    }
    private void showProgress() {
        pd = new ProgressDialog(this);
        pd.setIcon(R.drawable.ic_delete);
        pd.setTitle("Please Wait...");
        pd.setMessage("removing cache...");
        pd.setCancelable(false);


    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return super.createPackageContext(packageName, flags);
    }


    private class CachePackageDataObserver extends IPackageDataObserver.Stub {


        public void onRemoveCompleted(String packageName, boolean succeeded) {


        }//End of onRemoveCompleted() method
    }//E


    void clearCache(String packagenm) {
        if (mClearCacheObserver == null) {
            mClearCacheObserver = new CachePackageDataObserver();
            mClearCacheObserver.onRemoveCompleted(packagenm, true);

        }

        PackageManager mPM = getPackageManager();

        @SuppressWarnings("rawtypes")
        final Class[] classes = {Long.TYPE, IPackageDataObserver.class};


        Long localLong = Long.valueOf(CACHE_APP);

        try {
            Method localMethod =
                    mPM.getClass().getMethod("freeStorageAndNotify", classes);

      /*
       * Start of inner try-catch block
       */
            try {
                localMethod.invoke(mPM, localLong, mClearCacheObserver);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
      /*
       * End of inner try-catch block
       */
        } catch (NoSuchMethodException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    private void clearAllCache() {
        PackageManager pm = getPackageManager();
        Method[] methods = pm.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("freeStorage")) {
                try {
                    long desiredFreeStorage = Long.MAX_VALUE;
                    m.invoke(pm, desiredFreeStorage, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }


    }

}
