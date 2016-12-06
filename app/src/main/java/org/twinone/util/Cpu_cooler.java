package org.twinone.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.twinone.service.PeripheralService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import wewe.app.moboost.R;


/**
 * Created by Android on 3/20/2016.
 */
public class Cpu_cooler extends Activity {
    RelativeLayout back;
    String Cpu_cooler;
    TextView temp;
    public static final String TAG = Main_navigation.class.getSimpleName();

    private PeripheralService mService;
    String PACKAGE_NAME = "master.clean.moboost";

    TextView labelCpuTemp, txtTempOk, count;
    Button buttonUpdate;
    ArrayList<String> listpackageNm = new ArrayList<String>();
    List<String> listpackages = new ArrayList<String>();
    List<String> listapps = new ArrayList<String>();
    List<Integer> listpss = new ArrayList<Integer>();

    List<String> listSize = new ArrayList<String>();
    List<String> listpackageName = new ArrayList<String>();
    List<Boolean> listcheck = new ArrayList<Boolean>();
    List<Drawable> listAppImage = new ArrayList<Drawable>();
    List<String> listMemory = new ArrayList<String>();
    TextView id_cool;

    Button btnCoolDown;

    ProgressBar progressBar2;
    LinearLayout weatherLinear;
    ActivityManager localActivityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpu_cooler);


        txtTempOk = (TextView) findViewById(R.id.txtTempOk);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        btnCoolDown = (Button) findViewById(R.id.btnCoolDown);
        weatherLinear = (LinearLayout) findViewById(R.id.weatherLinear);
        id_cool = (TextView) findViewById(R.id.id_cool);
        count = (TextView) findViewById(R.id.count);

        localActivityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);


        if (!PeripheralService.isServiceRunning(this)) {
            Intent startServiceIntent = new Intent(this, PeripheralService.class);
            startServiceIntent.setAction(PeripheralService.ACTION_START_SERVER);
            startService(startServiceIntent);
        }
        Intent bindServiceIntent = new Intent(this, PeripheralService.class);
        bindService(bindServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(mUartStatusChangeReceiver, makeGattUpdateIntentFilter());

        labelCpuTemp = (TextView) findViewById(R.id.temp);

        Intent it = getIntent();
        Cpu_cooler = it.getStringExtra("Cpu_cooler");

        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();


            }
        });

        if (!Local.fetch(Cpu_cooler.this, "clear").contains("true")) {
            GetJunkFile files = new GetJunkFile();
            files.execute();


            Log.e("listapps", "" + listapps);
        } else {

            Intent itt = new Intent(Cpu_cooler.this, Cleared.class);
            itt.putExtra("cpu_temp", "cpu_temp");
            startActivity(itt);
            finish();
            //  id_cool.setText("No running applications");
        }


    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PeripheralService.BROADCAST_CONNECTION_STATUS);
        intentFilter.addAction(PeripheralService.BROADCAST_CPU_TEMPERATURE_UPDATE);
        intentFilter.addAction(PeripheralService.BROADCAST_DEVICES_CONNECTED);
        intentFilter.addAction(PeripheralService.BROADCAST_SERVICE_CLOSED);
        return intentFilter;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mUartStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        unbindService(mServiceConnection);
        mService = null;
    }

    private final BroadcastReceiver mUartStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            double temperatu = intent.getDoubleExtra("temperature", 0);
            String tfinal =String.format("%.1f°C", temperatu);

            Log.e("final_temp","" +tfinal);



            final Intent mIntent = intent;
            //*********************//
            if (PeripheralService.BROADCAST_CPU_TEMPERATURE_UPDATE.equals(action)) {
                double temperature = intent.getDoubleExtra("temperature", 0);
                labelCpuTemp.setText(String.format("%.1f°C", temperature));

                if (temperature >= 50) {
                    txtTempOk.setText("CPU temperature is overheating");
                } else if (temperature >= 35 || temperature < 50) {
                    txtTempOk.setText("CPU is little hot");
                } else if (temperature < 35) {
                    txtTempOk.setText("CPU temperature is OK");
                    Intent it = new Intent(Cpu_cooler.this, Cleared.class);
                    it.putExtra("cpu_temp", "cpu_temp");
                    startActivity(it);

                    finish();
                }

            } else if (PeripheralService.BROADCAST_CONNECTION_STATUS.equals(action)) {
                String message = intent.getStringExtra("message");

                Log.e("message","" +message);

             //   labelCpuTemp.setText(message);
            } else if (PeripheralService.BROADCAST_DEVICES_CONNECTED.equals(action)) {
                int num = intent.getIntExtra("num", 0);
                final String message = getString(R.string.status_devicesConnected) + " "
                        + num;
                //  labelDevicesNum.setText(message);
            } else if (PeripheralService.BROADCAST_SERVICE_CLOSED.equals(action)) {
                finish();
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((PeripheralService.LocalBinder) rawBinder).getService();
            Log.d(TAG, "onServiceConnected mService= " + mService);
        }

        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
        }
    };


    public void killprocess(String processname) {


        try {
            Process suProcess = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

            os.writeBytes("adb shell" + "\n");
            os.flush();

            os.writeBytes("am force-stop " + processname + "\n");
            os.flush();
            os.close();
            suProcess.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public class GetJunkFile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            getData();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            id_cool.setText("Cool the CPU further");

            Set<String> hs = new HashSet<>();
            hs.addAll(listpackageName);
            listpackageName.clear();
            listpackageName.addAll(hs);

//            Set<Drawable> hs1 = new HashSet<>();
//            hs1.addAll(listAppImage);
//            listAppImage.clear();
//            listAppImage.addAll(hs1);
//
//            Set<String> hs2 = new HashSet<>();
//            hs2.addAll(listapps);
//            listapps.clear();
//            listapps.addAll(hs2);

            for (int i = 0; i < listpackageName.size(); i++) {

                String packageName = listpackageName.get(i);
                if (!packageName.equals("wewe.app.moboost")) {
                    listpackages.add(packageName);

                    try {
                        ApplicationInfo app = getPackageManager().getApplicationInfo(packageName, 0);


                        if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                            Drawable icon = getPackageManager().getApplicationIcon(app);
                            listAppImage.add(icon);


                            String name = getPackageManager().getApplicationLabel(app).toString();
                            listapps.add(name);
                        }


                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                }

            }


            for (int i = 0; i < listAppImage.size(); i++) {

                String name = listapps.get(i);
                listSize.add(name);

                String packages = listpackages.get(i);
                listpackageNm.add(packages);

                Drawable icon = listAppImage.get(i);

                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.custom_apps, null);

                TextView txtAppNm = (TextView) v.findViewById(R.id.txtAppNm);
                ImageView thumbImage = (ImageView) v.findViewById(R.id.thumbImage);

                txtAppNm.setText(name);
                thumbImage.setImageDrawable(icon);

                if (txtTempOk.getText().equals("")) {
                    txtTempOk.setText("Cool down the CPU");
                }

                weatherLinear.addView(v);
                btnCoolDown.setVisibility(View.VISIBLE);

            }

            int countvalue = listSize.size();

            count.setText("Clean " + countvalue + " running apps");

            btnCoolDown.setVisibility(View.VISIBLE);


            btnCoolDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Local.save(Cpu_cooler.this, "clear", "true");
                    Intent service = new Intent(Cpu_cooler.this, MyServiceNew.class);
                    startService(service);

                    if (!listpackageNm.equals("")) {
                        killProcess(listpackageNm);

                        if (killProcess(listpackageNm)) {

                            Intent it = new Intent(Cpu_cooler.this, Cleared.class);
                            it.putExtra("cpu_temp", "cpu_temp");
                            startActivity(it);
                            finish();

                        }

                    }


                }
            });


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            id_cool.setText("Loading apps...");

        }
    }


    public static boolean isTopActivity(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        int id = context.checkCallingOrSelfPermission(android.Manifest.permission.GET_TASKS);
        if (PackageManager.PERMISSION_GRANTED != id) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public void getData() {

        List<ActivityManager.RunningServiceInfo> tasks1 = localActivityManager.getRunningServices(BIND_IMPORTANT);

        List<ActivityManager.RunningServiceInfo> tasks4 = localActivityManager.getRunningServices(BIND_ALLOW_OOM_MANAGEMENT);

        //    List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);
        progressBar2.setMax(tasks1.size());
        //

        long seed = System.nanoTime();
        Collections.shuffle(tasks1, new Random(seed));

        for (int i = 0; i < tasks1.size(); i++) {

            progressBar2.setProgress(i);


            ActivityManager.RunningServiceInfo info = tasks1.get(i);

            ComponentName mComponentName = info.service;

            String packageName = mComponentName.getPackageName();
            listpackageName.add(packageName);


        }


    }

    public boolean killProcess(ArrayList<String> packagename) {
        progressBar2.setMax(packagename.size());

        id_cool.setText("Dropping temprature....");


        for (int i = 0; i < packagename.size(); i++) {

            progressBar2.setProgress(i);

            String package_name = packagename.get(i);

            localActivityManager.killBackgroundProcesses(package_name);


        }

        return true;

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
