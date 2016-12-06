package org.twinone.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/15/2016.
 */
public class Tools_app_lock extends Activity {

    RelativeLayout back;
    String Tools_app_lock;
    List<String> appName = new ArrayList<String>();
    List<Drawable> appLogo = new ArrayList<Drawable>();
    LinearLayout otherViews;
    TextView textView;
    Button protect;
    private List<ResolveInfo> listAllApplication = null;
    List<Drawable> listImages  =new ArrayList<Drawable>();
    List<String> listappname  =new ArrayList<String>();

    List<String> listpackageNm  =new ArrayList<String>();
    ArrayList<String> listpackageNmNew  =new ArrayList<String>();
    TinyDB db;



    private ProgressDialog loading;

    PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools_app_lock);



        db= new TinyDB(this);

        otherViews = (LinearLayout) findViewById(R.id.otherViews);
        textView = (TextView) findViewById(R.id.textView);
        protect = (Button) findViewById(R.id.protect);

        packageManager = getApplicationContext().getPackageManager();

        GetListapps app = new GetListapps();
        app.execute();


        back = (RelativeLayout) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }
    public class GetListapps extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {

            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            Boolean included = false;

            listAllApplication = getPackageManager().queryIntentActivities(
                    mainIntent, 0);
            int length = listAllApplication.size();



            Log.e("hssssssss", "async len " + length);

            for (int i = 0; i < length; i++) {
                ResolveInfo info = listAllApplication.get(i);

                String strChkApp = info.activityInfo.packageName;
                listpackageNm.add(strChkApp);
                //   Drawable icon = getPackageManager().getApplicationIcon(strChkApp);

                Drawable icon=    info.loadIcon(packageManager);
                listImages.add(icon);

                String appnam= (String) info.loadLabel(packageManager);
                listappname.add(appnam);


            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            for(int i =0;i<listImages.size();i++){
                String name = listappname.get(i);
                Drawable icon = listImages.get(i);

                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.custom_junk_files, null);
                TextView cache_size = (TextView) v.findViewById(R.id.cache_size);
                TextView app_name = (TextView) v.findViewById(R.id.app_name);
                LinearLayout id_linear = (LinearLayout) v.findViewById(R.id.id_linear);
                ImageView app_icon = (ImageView) v.findViewById(R.id.app_icon);
                final CheckBox check_junk = (CheckBox) v.findViewById(R.id.check_junk);
                check_junk.setId(i);
                cache_size.setVisibility(View.GONE);
                otherViews.addView(v);

                app_icon.setImageDrawable(icon);
                app_name.setText(name);

                textView.setText(listappname.size() + " apps with privacy issues");

                check_junk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        int id = check_junk.getId();
                        String packagenm = listpackageNm.get(id);

                        if (isChecked) {
                            listpackageNmNew.add(packagenm);
                        }

                        if (!isChecked) {
                            for (int i = 0; i < listpackageNmNew.size(); i++) {
                                if (packagenm.contains(listpackageNm.get(i))) {


                                    listpackageNmNew.remove(i);

                                    break;
                                }
                            }

                        }

                    }
                });


            }

//            protect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent it = new Intent(getApplicationContext(), LockApps.class);
//                    db.putListString("listpackageNmNew",listpackageNmNew);
//                    startActivity(it);
//                }
//            });


            if (loading.isShowing()) {
                loading.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Tools_app_lock.this);
            loading.setTitle("Please wait");
            loading.setMessage("Fetching applications... ");
            loading.show();

        }
    }


}




