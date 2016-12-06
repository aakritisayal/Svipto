package org.twinone.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.*;
import android.os.Process;
import android.provider.*;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.internal.Streams;
import com.ram.speed.booster.RAMBooster;
import com.ram.speed.booster.interfaces.CleanListener;
import com.ram.speed.booster.interfaces.ScanListener;
import com.ram.speed.booster.utils.ProcessInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/20/2016.
 */
public class Phone_boost extends Activity {
    RelativeLayout back;
    String Phone_boost, strtotalSpacediff, strtotalSpace;
    TextView textViewfree, free_space, totalRunningApps;
    List<String> listMemory = new ArrayList<String>();
    List<Float> listTlMemoryUsed = new ArrayList<Float>();
    List<String> listapps = new ArrayList<String>();
    List<String> listpackageName = new ArrayList<String>();
    ArrayList<Boolean> listIsRunning = new ArrayList<Boolean>();
    ArrayList<Integer> listPid = new ArrayList<Integer>();
    ArrayList<Integer> listProcessPid = new ArrayList<Integer>();
    ProgressDialog pd;
    ProgressBar progressBar2;
    PackageManager packageManager;
    LinearLayout id_runningapps;
    double sizedouble, selectedSize, getSelectedSize;
    View v;
    Button btnPhoneBoost;

    LinearLayout check_cpu_temp;
    ArrayList<Long> totalMemory = new ArrayList<Long>();
    ArrayList<String> listPackages = new ArrayList<String>();
    ActivityManager am;
    List<ActivityManager.RunningServiceInfo> tasks4;
    private static final int FIRST_SYS_CPU_COLUMN_INDEX = 2;
    String ramavailable;

    private static final int IDLE_SYS_CPU_COLUMN_INDEX = 5;
    private RAMBooster booster;
    int apppid;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_boost);

        String device_id = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        apppid = android.os.Process.myPid();

        Intent it = getIntent();

       String adds= it.getStringExtra("adds");

        if(adds!=null){

            mInterstitialAd = new InterstitialAd(this);

            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            // Check the LogCat to get your test device ID
                    .addTestDevice(device_id)
                    .build();

            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }

                @Override
                public void onAdClosed() {
                  //  Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLeftApplication() {
                  //  Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
                  //  Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
                }
            });

        }

   //     cpuTemp = (TextView) findViewById(R.id.cpuTemp);

        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        tasks4 = am.getRunningServices(BIND_IMPORTANT);


        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        check_cpu_temp = (LinearLayout) findViewById(R.id.check_cpu_temp);

        totalRunningApps = (TextView) findViewById(R.id.totalRunningApps);
        textViewfree = (TextView) findViewById(R.id.textViewfree);
        free_space = (TextView) findViewById(R.id.free_space);
        id_runningapps = (LinearLayout) findViewById(R.id.id_runningapps);
        btnPhoneBoost = (Button) findViewById(R.id.btnPhoneBoost);


        check_cpu_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Cpu_cooler.class);
                startActivity(it);
            }
        });


        back = (RelativeLayout) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        Boost_phone boost = new Boost_phone();
        boost.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    public class Boost_phone extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            getApps();

            Log.e("listIsRunning", "" + listIsRunning);
            Log.e("totalMemory", "" + totalMemory);


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            int size = listPackages.size();

            //  progressBar2.setMax(size);

            for (int i = 0; i < listPackages.size(); i++) {
                //   progressBar2.setProgress(i);

                String packages = listPackages.get(i);
                try {
                    final ApplicationInfo app = getPackageManager().getApplicationInfo(packages, 0);

                    if (packages.equals("wewe.app.moboost")) {
                        System.out.println(">>>>>>packages is system package" + app.packageName);
                    } else {
                        final String name = getPackageManager().getApplicationLabel(app).toString();

                        listapps.add(name);
                        final Drawable icon = getPackageManager().getApplicationIcon(app);

                        LayoutInflater inflater = getLayoutInflater();
                        v = inflater.inflate(R.layout.custom_junk_files, null);
                        TextView cache_size = (TextView) v.findViewById(R.id.cache_size);
                        TextView app_name = (TextView) v.findViewById(R.id.app_name);
                        final LinearLayout id_linear = (LinearLayout) v.findViewById(R.id.id_linear);
                        ImageView app_icon = (ImageView) v.findViewById(R.id.app_icon);
                        final CheckBox check_junk = (CheckBox) v.findViewById(R.id.check_junk);
                        check_junk.setId(i);
                        id_linear.setId(i);
                        id_runningapps.addView(v);
                        app_icon.setImageDrawable(icon);
                        app_name.setText(name);
                        cache_size.setVisibility(View.GONE);
                        check_junk.setVisibility(View.GONE);


                        id_linear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int id = id_linear.getId();
                                int pids =  listProcessPid.get(id);

                                String packages = listPackages.get(id);

                                Log.e("pacakges", "" + packages);


                                Intent intent = new Intent();
                                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",packages,null);
                                intent.setData(uri);
                                startActivity(intent);

                            }
                        });

//                        check_junk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                             int id = check_junk.getId();
//
//
//
//
//
//                            }
//                        });


                    }


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }


            Log.e("listpackageName", "" + listpackageName);
            Log.e("listPid", "" + listPid);


            if (null != pd) {
                if (pd.isShowing())
                    pd.dismiss();
            }

            btnPhoneBoost.setVisibility(View.VISIBLE);


            sizedouble = 0;

            double freeSpace = Common_methods.availableMemory(Phone_boost.this);

            double totalSpace = Common_methods.getTotalRAM(Phone_boost.this);

            double totalSpacediff = totalSpace - freeSpace;


            if (totalSpacediff > 1000) {

                totalSpacediff = totalSpacediff / 1024;
                DecimalFormat Format = new DecimalFormat("#.##");
                totalSpacediff = Double.valueOf(Format.format(totalSpacediff));
                ramavailable =String.valueOf(totalSpacediff) + "GB";




            } else {

                DecimalFormat Format = new DecimalFormat("#.##");
                totalSpacediff = Double.valueOf(Format.format(totalSpacediff));
                int intspace = Integer.valueOf((int) totalSpacediff);
                //   strtotalSpacediff = String.valueOf(intspace) + "MB";
                ramavailable =String.valueOf(intspace) + "MB";


            }


            if (freeSpace > 1000) {

                freeSpace = freeSpace / 1024;
                DecimalFormat Format = new DecimalFormat("#.##");
                freeSpace = Double.valueOf(Format.format(freeSpace));

                strtotalSpacediff = String.valueOf(freeSpace) + "GB";
                textViewfree.setText(strtotalSpacediff);

                //    textViewfree.setText(String.valueOf(totalSpacediff) + "GB");

            } else {

                DecimalFormat Format = new DecimalFormat("#.##");
                freeSpace = Double.valueOf(Format.format(freeSpace));
                int intspace = Integer.valueOf((int) freeSpace);
                strtotalSpacediff = String.valueOf(intspace) + "MB";
                textViewfree.setText(strtotalSpacediff);

                //  textViewfree.setText(String.valueOf(intspace) + "MB");

            }


            if (totalSpace > 1000) {

                totalSpace = totalSpace / 1024;
                DecimalFormat Format = new DecimalFormat("#.##");
                totalSpace = Double.valueOf(Format.format(totalSpace));

                strtotalSpace = String.valueOf(totalSpace) + "GB";


            } else {

                DecimalFormat Format = new DecimalFormat("#.##");
                totalSpace = Double.valueOf(Format.format(totalSpace));
                int intspace = Integer.valueOf((int) totalSpace);

                strtotalSpace = String.valueOf(intspace) + "MB";

            }

            totalRunningApps.setText(String.valueOf(listapps.size()));
            free_space.setText(ramavailable + "/" + strtotalSpace);


            btnPhoneBoost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (booster == null)
                        booster = null;
                    booster = new RAMBooster(Phone_boost.this);
                    booster.setDebug(true);
                    booster.setScanListener(new ScanListener() {
                        @Override
                        public void onStarted() {
                            Log.e("tagg", "Scan started");

                            //    idtxt.setText("Scan started");
                        }

                        @Override
                        public void onFinished(long availableRam, long totalRam, List<ProcessInfo> appsToClean) {

                            Log.e("tagg", String.format(Locale.US,
                                    "Scan finished, available RAM: %dMB, total RAM: %dMB",
                                    availableRam, totalRam));

//                        idtxt.setText(String.format(Locale.US,
//                                "Scan finished, available RAM: %dMB, total RAM: %dMB",
//                                availableRam, totalRam));


                            List<String> apps = new ArrayList<String>();
                            for (ProcessInfo info : appsToClean) {
                                apps.add(info.getProcessName());
                            }


                            Log.e("tagg", String.format(Locale.US,
                                    "Going to clean founded processes: %s", Arrays.toString(apps.toArray())));
//                        idtxt.setText( String.format(Locale.US,
//                                "Going to clean founded processes: %s", Arrays.toString(apps.toArray())));

                            booster.startClean();
                        }
                    });
                    booster.setCleanListener(new CleanListener() {
                        @Override
                        public void onStarted() {

                            Log.d("tagg", "Clean started");

                            //     idtxt.setText("Clean started");
                        }


                        @Override
                        public void onFinished(long availableRam, long totalRam) {

                            Log.e("", String.format(Locale.US,
                                    "Clean finished, available RAM: %dMB, total RAM: %dMB",
                                    availableRam, totalRam));
                            Thread th = new Thread(r);
                            th.start();

                            booster = null;

                        }
                    });
                    booster.startScan(true);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress("Calculating app..");
        }
    }


    public void getApps() {


        for (int i = 0; i < tasks4.size(); i++) {


            ActivityManager.RunningServiceInfo info = tasks4.get(i);

            String process = info.process;


            ComponentName mComponentName = info.service;

            String packageName = mComponentName.getPackageName();

            int pid = info.pid;
            int[] pids = new int[1];
            pids[0] = info.pid;

            android.os.Debug.MemoryInfo[] MI = am.getProcessMemoryInfo(pids);


            long total = MI[0].dalvikPrivateDirty + MI[0].dalvikSharedDirty + MI[0].dalvikPss + MI[0].nativePrivateDirty + MI[0].nativeSharedDirty + MI[0].nativePss + MI[0].otherPrivateDirty + MI[0].otherSharedDirty + MI[0].otherPss;
            if (total != 0 || !packageName.equals("wewe.app.moboost")) {


                boolean checkRun = Common_methods.isThisServiceRunning(this, packageName);
                listIsRunning.add(checkRun);

                totalMemory.add(total);
                listProcessPid.add(pid);
                listMemory.add(process);

                listPackages.add(packageName);
            }

        }


        Log.e("listMemory", "" + listMemory);

        Set<String> hs = new HashSet<>();
        hs.addAll(listPackages);
        listPackages.clear();
        listPackages.addAll(hs);
    }


    private void showProgress(String message) {
        pd = new ProgressDialog(this);
        pd.setIcon(R.drawable.icon);
        pd.setTitle("Please Wait...");
        pd.setMessage(message);
        pd.setCancelable(true);
        pd.show();

    }


    Runnable r = new Runnable() {
        @Override
        public void run() {

            Intent it = new Intent(Phone_boost.this, Phone_boost.class);
            it.putExtra("adds","adds");
            startActivity(it);
            overridePendingTransition(0, 0);
            finish();
        }
    };
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
