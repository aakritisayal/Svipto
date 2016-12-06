package org.twinone.util;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import wewe.app.moboost.R;

import static android.os.Environment.getExternalStorageState;

/**
 * Created by Android on 5/11/2016.
 */
public class Move_fragment extends Fragment {
    ArrayList<String> listfileFolder = new ArrayList<>();
    ArrayList<String> listfile = new ArrayList<>();

    ArrayList<String> listPackages = new ArrayList<>();

    PackageManager manager;
    TextView appsL;
    Button clean;
    LinearLayout id_runningapps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {

        View view = inflate.inflate(R.layout.move_fragment, null);

        final String state = getExternalStorageState();
        final PackageManager pm = getActivity().getPackageManager();

        manager = getActivity().getPackageManager();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  // we can read the External Storage...
            getAllFilesOfDir(Environment.getExternalStorageDirectory());
        }


        appsL = (TextView) view.findViewById(R.id.apps);
        id_runningapps = (LinearLayout) view.findViewById(R.id.id_runningapps);
        clean = (Button) view.findViewById(R.id.clean);

        Log.e("files", "" + listfileFolder);

        for (int i = 0; i < listfile.size(); i++) {

            String files = listfile.get(i);


            PackageInfo    pi = getActivity().getPackageManager().getPackageArchiveInfo(files, 0);

            if(pi!=null)
            {
                // the secret are these two lines....
                pi.applicationInfo.sourceDir       = files;
                pi.applicationInfo.publicSourceDir = files;

                Drawable APKicon = pi.applicationInfo.loadIcon(pm);
                String   AppName = (String)pi.applicationInfo.loadLabel(pm);

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View v = inflater.inflate(R.layout.custom_junk_files, null);
                TextView cache_size = (TextView) v.findViewById(R.id.cache_size);
                TextView app_name = (TextView) v.findViewById(R.id.app_name);
                LinearLayout id_linear = (LinearLayout) v.findViewById(R.id.id_linear);
                ImageView app_icon = (ImageView) v.findViewById(R.id.app_icon);
                final CheckBox check_junk = (CheckBox) v.findViewById(R.id.check_junk);
                check_junk.setId(i);

                check_junk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        int id = check_junk.getId();
                        String packlageNm = listfile.get(id);

                        if (isChecked) {


                            listPackages.add(packlageNm);

                        }


                        if (!isChecked) {
                            int id1 = check_junk.getId();


                            String packageName1 = listfile.get(id);

                            for (int i = 0; i < listPackages.size(); i++) {
                                if (packageName1.contains(listfile.get(i))) {


                                    listPackages.remove(i);


                                    break;
                                }
                            }


                        }




                    }
                });


                cache_size.setVisibility(View.GONE);
                app_name.setText(AppName);
                app_icon.setImageDrawable(APKicon);
                appsL.setText("Apps: " + String.valueOf(listfile.size()));
                id_runningapps.addView(v);

            }
            }

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listPackages.size()!=0) {

                    deleteFiles(listPackages);

                } else {
                    Toast.makeText(getActivity(), "Please select apk file", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }


    private void getAllFilesOfDir(File directory) {

        final File[] files = directory.listFiles();


        String apk = ".apk";


        if (files != null) {
            for (File file : files) {
                if (file != null) {
                    if (file.isDirectory()) {  // it is a folder...
                        getAllFilesOfDir(file);

                    } else {  // it is a file...
                        Log.d("tag", "File: " + file.getAbsolutePath() + "\n");
                        //  listfiledDirectory.add(file.getAbsolutePath());

                    }
                    if (file.getName().endsWith(apk)) {

                        listfileFolder.add(file.getName());

                        String path = file.getPath();
                        listfile.add(path);


                    }
                }
            }
        }

    }


    public static String getAppLabel(PackageManager pm, String pathToApk) {
        PackageInfo packageInfo = pm.getPackageArchiveInfo(pathToApk, 0);

     //   if (Build.VERSION.SDK_INT >= 8) {
            // those two lines do the magic:
            packageInfo.applicationInfo.sourceDir = pathToApk;
            packageInfo.applicationInfo.publicSourceDir = pathToApk;
     //   }

        CharSequence label = pm.getApplicationLabel(packageInfo.applicationInfo);
        return label != null ? label.toString() : null;
    }

    public void deleteFiles(ArrayList<String> list) {


        for (int i = 0; i < list.size(); i++) {

            String strFiles = list.get(i);
            File f = new File(strFiles);

            if (f.exists()) {
                f.delete();
                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
            }


        }

        Intent it = new Intent(getActivity(), App_manager.class);
        it.putExtra("App_manager", "App_manager");
        startActivity(it);
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();

    }


}
