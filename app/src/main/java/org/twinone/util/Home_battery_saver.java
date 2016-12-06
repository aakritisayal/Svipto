package org.twinone.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/20/2016.
 */
public class Home_battery_saver extends Activity {
    RelativeLayout back;
    Button btn_optimize;

    TextView drainingfast, Optimize;
    String PACKAGE_NAME = "master.clean.moboost.moboost";
    List<String> listappname = new ArrayList<String>();
    List<Drawable> listimages1 = new ArrayList<Drawable>();
    List<Drawable> listimages2 = new ArrayList<Drawable>();

    List<String> listapps = new ArrayList<String>();
    List<Integer> listpss = new ArrayList<Integer>();
    List<String> listInstalledapps = new ArrayList<String>();
    List<Integer> listaddValue = new ArrayList<Integer>();

    List<String> listpackages = new ArrayList<String>();
    ArrayList<String> listpackageNm = new ArrayList<String>();
    LinearLayout linearGrid1,linearGrid2;

    ImageView battery;
    private boolean[] thumbnailsselection;
    private boolean[] thumbnailsselection1;
    private int count,count1;
    private ImageAdapter imageAdapter;
    private ImageAdapter1 imageAdapter1;
    private Drawable[] thumbnails;
    private Drawable[] thumbnails1;
    private String[] appname;

    TextView txtHybernateApps,txtrecomnd;

    int addvalue;
    ActivityManager localActivityManager;
    RelativeLayout listItems1,listItems2;
    ToggleButton idToggle1,idToggle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_battery_saver);

        txtHybernateApps =(TextView) findViewById(R.id.txtHybernateApps);
        txtrecomnd =(TextView) findViewById(R.id.txtrecomnd);

        listItems1 = (RelativeLayout) findViewById(R.id.listItems1);
        listItems2 = (RelativeLayout) findViewById(R.id.listItems2);
        drainingfast = (TextView) findViewById(R.id.drainingfast);
        back = (RelativeLayout) findViewById(R.id.back);
        battery = (ImageView) findViewById(R.id.battery);
        Optimize = (TextView) findViewById(R.id.Optimize);
        btn_optimize = (Button) findViewById(R.id.btn_optimize);
        idToggle1 = (ToggleButton) findViewById(R.id.idToggle1);
        idToggle2 = (ToggleButton) findViewById(R.id.idToggle2);
        linearGrid1 = (LinearLayout) findViewById(R.id.linearGrid1);
        linearGrid2 = (LinearLayout) findViewById(R.id.linearGrid2);
        //    getBatteryPercentage();

        PackageManager pm = this.getPackageManager();

        localActivityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

        if (!Local.fetch(Home_battery_saver.this, "clear_battery").contains("true")) {
            installedApps();
            getapp();
            Log.e("listapps", "" + listapps);
        } else {

            Intent itt = new Intent(Home_battery_saver.this, Cleared.class);
            //   itt.putExtra("cpu_temp", "cpu_temp");
            startActivity(itt);
            finish();
            //  id_cool.setText("No running applications");
        }


        btn_optimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Local.save(Home_battery_saver.this, "clear_battery", "true");
                Intent service = new Intent(Home_battery_saver.this, MyServiceBattery.class);
                startService(service);
                if (!listpackageNm.equals("")) {
                    killProcess(listpackageNm);
                    if (killProcess(listpackageNm)) {
                        Intent it = new Intent(Home_battery_saver.this, Cleared.class);
                        it.putExtra("add","add");
                        startActivity(it);
                        finish();

                    }

                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();


            }
        });

        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drainingfast.setText(String.valueOf(batteryLevel));
            }
        });

        new Thread(new ThreadM()).start();


        listItems1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearGrid1.getVisibility() == View.VISIBLE) {
                    linearGrid1.setVisibility(View.GONE);
                    idToggle1.setChecked(false);
                } else if (linearGrid1.getVisibility() != View.VISIBLE) {
                    linearGrid1.setVisibility(View.VISIBLE);
                    idToggle1.setChecked(true);
                }

            }
        });

        listItems2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearGrid2.getVisibility() == View.VISIBLE) {
                    linearGrid2.setVisibility(View.GONE);
                    idToggle2.setChecked(false);
                } else if (linearGrid2.getVisibility() != View.VISIBLE) {
                    linearGrid2.setVisibility(View.VISIBLE);
                    idToggle2.setChecked(true);
                }

            }
        });



        if(listimages2.size()==0){
            listItems2.setVisibility(View.GONE);
        }

    }


    BatteryReceiver mArrow;

    private class ThreadM implements Runnable {
        @Override
        public void run() {
            mArrow = new BatteryReceiver();
            IntentFilter mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(Intent.ACTION_BATTERY_LOW);
            mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
            mIntentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
            Intent batteryIntent = registerReceiver(mArrow, mIntentFilter);
            batteryLevel = getBatteryLevel(batteryIntent);
            Log.e("Battery Level", String.valueOf(batteryLevel));
        }
    }

    float batteryLevel;

    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equalsIgnoreCase(Intent.ACTION_BATTERY_LOW) || arg1.getAction().equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED) || arg1.getAction().equalsIgnoreCase(Intent.ACTION_BATTERY_OKAY)) {
                int level = arg1.getIntExtra("level", 0);


                // Toast.makeText(Home_battery_saver.this, "Current Battery " + level + " %", Toast.LENGTH_LONG).show();

                drainingfast.setText(String.valueOf("Battery Level Remaining:" + level + "%"));
            }
        }
    }

    public float getBatteryLevel(Intent batteryIntent) {
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100.0f;
    }


    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.custom_gridview, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                holder.txtAppNm = (TextView) convertView.findViewById(R.id.txtAppNm);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.txtAppNm.setId(position);
            holder.checkbox.setChecked(true);
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    int id = holder.checkbox.getId();

                    String value = listpackages.get(id);

                    if (isChecked) {

                        addvalue = addvalue + 2;

                        Optimize.setText("Optimize to save " + addvalue + " minutes");

                        listpackageNm.add(value);
                        listaddValue.add(addvalue);
                        thumbnailsselection[id] = true;

                    }

                    if (!isChecked) {

                        addvalue = addvalue - 2;

                        Optimize.setText("Optimize to save " + addvalue + " minutes");

                        thumbnailsselection[id] = false;

                        for (int i = 0; i < listpackageNm.size(); i++) {

                            if (value.contains(listpackages.get(i))) {

                                listpackageNm.remove(i);
                                listaddValue.remove(i);

                                break;
                            }
                        }

                    }
                }
            });


            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
//                    startActivity(intent);
                }
            });


            holder.txtAppNm.setText(listapps.get(position));
            holder.imageview.setImageDrawable(listimages1.get(position));
            holder.checkbox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        TextView txtAppNm;
        int id;
    }


    private int getPss(String processName) {
        ActivityManager am = (ActivityManager) (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> apps = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo proc : apps) {
            if (!proc.processName.equals(processName)) {
                continue;
            }

            int[] pids = {
                    proc.pid};

            Debug.MemoryInfo meminfo = am.getProcessMemoryInfo(pids)[0];
            return meminfo.getTotalPss();

        }
        return -1;
    }


    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public boolean killProcess(ArrayList<String> packagename) {


        for (int i = 0; i < packagename.size(); i++) {

            String package_name = packagename.get(i);

            localActivityManager.killBackgroundProcesses(package_name);

            return true;

        }

        return true;

    }

    public void getapp() {
        List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);
        for (int i = 0; i < apps.size(); i++) {
            ApplicationInfo app = apps.get(i);
            if ((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                String packageNm = app.processName;
                listappname.add(packageNm);
            } else {
                String packageNm = app.processName;
                if (packageNm.equals(PACKAGE_NAME)) {
                    Log.e("pss", "");
                } else {
                    int pss = getPss(packageNm);
                    if (pss == -1) {
                        Log.e("pss", "");
                    } else {
                        addvalue = addvalue + 2;
                        listpackages.add(packageNm);
                        listpss.add(pss);
                        String name = getPackageManager().getApplicationLabel(app).toString();
                        listapps.add(name);
                        final Drawable icon = getPackageManager().getApplicationIcon(app);
                        listimages1.add(icon);
                    }
                }
                txtHybernateApps.setText(listimages1.size()+ " apps");
            }
        }

        Log.e("listpackages", "" + listappname);
        if (listpackages.equals("")) {
            Intent it = new Intent(Home_battery_saver.this, Cleared.class);
            it.putExtra("no_apps", "no_apps");
            startActivity(it);
            finish();
        }
        Log.e("addition", "" + addvalue);
        Optimize.setText("Optimize to save " + addvalue + " minutes");
        thumbnails = listimages1.toArray(new Drawable[listimages1.size()]);
        this.count = listapps.size();
        this.thumbnailsselection = new boolean[this.count];
        this.thumbnails = new Drawable[this.count];
        this.appname = new String[this.count];

        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
    }

    public void installedApps()
    {
        String whatsapp="com.whatsapp";
        String twitter ="com.twitter.android";
        String insta   ="com.instagram.android";
        String facebook="com.facebook.katana";
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            String packageName= packInfo.applicationInfo.processName;
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("App № " + Integer.toString(i), appName);
                if( packageName.equals(whatsapp)
                        ||packageName.equals(facebook)||packageName.equals(insta)||packageName.equals(twitter)) {
                    listInstalledapps.add(appName);
                    Log.e("packname123", "" + packInfo.applicationInfo.processName + "," + appName);
                    Drawable icon = getPackageManager().getApplicationIcon(packInfo.applicationInfo);
                    listimages2.add(icon);
                }
            }
        }

        txtrecomnd.setText(String.valueOf(listimages2.size()));

        Log.e("listpackages", "" + listappname);
        if (listpackages.equals("")) {
            Intent it = new Intent(Home_battery_saver.this, Cleared.class);
            it.putExtra("no_apps", "no_apps");
            startActivity(it);
            finish();
        }
        thumbnails1 = listimages2.toArray(new Drawable[listimages2.size()]);
        this.count1 = listInstalledapps.size();
        this.thumbnailsselection1 = new boolean[this.count1];
        this.thumbnails1 = new Drawable[this.count1];
        this.appname = new String[this.count1];


        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid2);
        imageAdapter1 = new ImageAdapter1();
        imagegrid.setAdapter(imageAdapter1);
    }

    public class ImageAdapter1 extends BaseAdapter{
        private LayoutInflater mInflater;
        public ImageAdapter1() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count1;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.custom_gridview, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                holder.txtAppNm = (TextView) convertView.findViewById(R.id.txtAppNm);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.txtAppNm.setId(position);
            holder.checkbox.setChecked(false);


            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    int id = holder.checkbox.getId();

                    String value = listInstalledapps.get(id);

                    if (isChecked) {

                        addvalue = addvalue + 2;

                        Optimize.setText("Optimize to save " + addvalue + " minutes");

//                        listpackageNm.add(value);
//                        listaddValue.add(addvalue);
//                        thumbnailsselection[id] = true;

                    }

                    if (!isChecked) {

                        addvalue = addvalue - 2;

                        Optimize.setText("Optimize to save " + addvalue + " minutes");

                    //    thumbnailsselection[id] = false;

//                        for (int i = 0; i < listpackageNm.size(); i++) {
//
//                            if (value.contains(listpackages.get(i))) {
//
//                                listpackageNm.remove(i);
//                                listaddValue.remove(i);
//
//                                break;
//                            }
//                        }

                    }
                }
            });


//
//        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                int id = holder.checkbox.getId();
//                String value = listpackages.get(id);
//                if (isChecked) {
//                    listpackageNm.add(value);
//                    thumbnailsselection1[id] = true;
//
//                }
//
//                if (!isChecked) {
//                    thumbnailsselection1[id] = false;
//                    for (int i = 0; i < listpackageNm.size(); i++) {
//                        if (value.contains(listpackages.get(i))) {
//                            listpackageNm.remove(i);
//                            break;
//                        }
//                    }
//
//                }
//            }
//        });


            holder.imageview.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
//                    startActivity(intent);
                }
            });


            holder.txtAppNm.setText(listInstalledapps.get(position));
            holder.imageview.setImageDrawable(listimages2.get(position));
            holder.checkbox.setChecked(thumbnailsselection1[position]);
            holder.id = position;
            return convertView;
        }
    }
}
