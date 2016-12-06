package org.twinone.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 5/11/2016.
 */
public class Uninsatll_fragment extends Fragment {


    String App_manager;
    PackageManager packageManager;
    ArrayList<String> list = new ArrayList<String>();

    //   Integer selected_position = -1;

    SharedPreferences sp;
    SharedPreferences.Editor edt;
    LinearLayout uninstall;
    String packlageNm;
    Button uninstall_button, button_backup;
    ArrayList<String> listPackages = new ArrayList<String>();
    ArrayList<Integer> listPos = new ArrayList<Integer>();

    ArrayList<String> listapkPath = new ArrayList<String>();
    ArrayList<String> listapkPathCheck = new ArrayList<String>();


    ArrayList<String> listappName = new ArrayList<String>();
   ArrayList<String> listappNameNew = new ArrayList<String>();

    ArrayList<Drawable> listimage = new ArrayList<Drawable>();
    LinearLayout linaerApps;
    int Count;
    TextView appsL, txtoccupied;

    double value;
    String apk = ".apk";
    String apkPath;
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {

        View view = inflate.inflate(R.layout.uninstall_fragment, null);

        sp = getActivity().getSharedPreferences("Shared_value", getActivity().MODE_MULTI_PROCESS);
        edt = sp.edit();

        linaerApps = (LinearLayout) view.findViewById(R.id.linaerApps);
        uninstall = (LinearLayout) view.findViewById(R.id.uninstall);
        uninstall_button = (Button) view.findViewById(R.id.uninstall_button);
        txtoccupied = (TextView) view.findViewById(R.id.txtoccupied);
        button_backup = (Button) view.findViewById(R.id.button_backup);


        appsL = (TextView) view.findViewById(R.id.apps);


        Log.e("position", "" + listPos);


        uninstall_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (listPackages.size() == 0) {
                    Toast.makeText(getActivity(), "Please select one or more application to uninstall", Toast.LENGTH_SHORT).show();
                }

                else {
                    uninstallPackage(listPackages);
                }


            }
        });

        button_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("Creating backup");

                if (listapkPathCheck.size() == 0) {
                    Toast.makeText(getActivity(), "Please select one or more application for Backup", Toast.LENGTH_SHORT).show();
                } else {
                  boolean value =  createApk(listapkPathCheck);


                }
            }
        });


        return view;
    }
    private void showProgress(String message) {
        pd = new ProgressDialog(getActivity());
        pd.setIcon(R.drawable.icon);
        pd.setTitle("Please Wait...");
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.show();

    }

    public boolean uninstallPackage(List<String> list) {

        boolean value = true;
        showProgress("Uninstalling");
        for (int i = 0; i < list.size(); i++) {
            String packlageNm = list.get(i);

            try {
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + packlageNm));
                startActivity(intent);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pd.dismiss();
        return true;


    }


    public boolean createApk(ArrayList<String> listapkPath) {

        boolean value = false;
        for (int i = 0; i < listapkPath.size(); i++) {

            apkPath = listapkPath.get(i);
            String appName =listappNameNew.get(i);

            File in = new File(apkPath);
            if (in.getName().endsWith(apk)) {

                File to = new File(String.valueOf(Environment.getExternalStorageDirectory()));
                boolean success = true;
                if (!to.exists()) {
                    success = to.mkdir();

                }
                if (success) {


                    try {


                        String sdCard = Environment.getExternalStorageDirectory().toString();

                        File dir = new File(sdCard+"/Moboost_Backup");
                        if(!dir.exists())
                        {
                            dir.mkdirs();
                            if (dir.mkdirs()) {
                                System.out.println("Directory created");
                            } else {
                                System.out.println("Directory is not created");
                            }
                        }



                        String ss = apkPath.substring(apkPath.lastIndexOf('/') + 1);

                        String name =in.getName();
                        String nn = in.getParent();
                        String hgg = in.getPath();

                        File targetLocation = new File(sdCard + "/Moboost_Backup/" + in.getName());

                        File fileLocation=   new File(sdCard + "/Moboost_Backup/", listappNameNew.get(i) + ".apk");

                        if(fileLocation.exists()){
                            Toast.makeText(getActivity(), "You aready craeted backup for this application", Toast.LENGTH_SHORT).show();

                            pd.dismiss();
                            refresh();
                        }

                        else{

                            InputStream in1 = new FileInputStream(in);
                            OutputStream out = new FileOutputStream(targetLocation);

                            // Copy the bits from instream to outstream
                            byte[] buf = new byte[1024];
                            int len;

                            while ((len = in1.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }

                            in1.close();
                            out.close();

                            targetLocation.renameTo(new File(sdCard + "/Moboost_Backup/", listappNameNew.get(i) + ".apk"));

                            Toast.makeText(getActivity(), "Backup Created", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            value =true;
                            refresh();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.e("not coppied", "not coppied");
                }

            }


        }

       return  value;
    }

    public void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }


    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static String formateFileSize(Context ct,long size) {
        return Formatter.formatFileSize(ct,size);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("", "");
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();

    }



    @Override
    public void onStop() {
        super.onStop();

        Log.d("", "");
    }


    public void refresh()
    {

        int positionapps = -1;
        long  sizedouble = 0;
        list.clear();
        listappName.clear();
        listapkPath.clear();
        listimage.clear();
        listPackages.clear();
        listapkPathCheck.clear();
        listappNameNew.clear();

        Log.d("", "");
        linaerApps.removeAllViews();
        packageManager = getActivity().getApplicationContext().getPackageManager();

        List<ApplicationInfo> apps = getActivity().getPackageManager().getInstalledApplications(0);


        for (int i = 0; i < apps.size(); i++) {

            ApplicationInfo app = apps.get(i);

            if ((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                String packageNm = app.processName;
            } else {

                final String packageNm = app.processName;


                long apkSize = new File(app.sourceDir).length();

                sizedouble = sizedouble + apkSize;


                String name = getActivity().getPackageManager().getApplicationLabel(app).toString();
                listappName.add(name);

                String apkPath = app.sourceDir;
                listapkPath.add(apkPath);

                final Drawable icon = getActivity().getPackageManager().getApplicationIcon(app);
                listimage.add(icon);


                list.add(packageNm);
                positionapps = positionapps + 1;

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View convertView = inflater.inflate(R.layout.custom_junk_files, null);


                TextView cache_size = (TextView) convertView.findViewById(R.id.cache_size);
                TextView app_name = (TextView) convertView.findViewById(R.id.app_name);
                ImageView app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
                final CheckBox check_junk = (CheckBox) convertView.findViewById(R.id.check_junk);

                cache_size.setText(Common_methods.getHumanReadableSize(apkSize, getActivity()));
                app_icon.setImageDrawable(icon);
                app_name.setText(name);
                check_junk.setId(positionapps);


                check_junk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        int id = check_junk.getId();
                        packlageNm = list.get(id);
                        String apkPath = listapkPath.get(id);
                        String appnm = listappName.get(id);

                        if (isChecked) {


                            listPackages.add(packlageNm);
                            listapkPathCheck.add(apkPath);
                           listappNameNew.add(appnm);


                        }


                        if (!isChecked) {
                            int id1 = check_junk.getId();


                            String packageName1 = list.get(id1);

                            for (int i = 0; i < listPackages.size(); i++) {
                                if (packageName1.contains(listPackages.get(i))) {

                                    listPackages.remove(i);
                                    listapkPathCheck.remove(i);
                                   listappNameNew.remove(i);

                                    break;
                                }
                            }


                        }


                    }
                });

                linaerApps.addView(convertView);
            }


        }

        txtoccupied.setText("OCCUPIED: " + formateFileSize(getActivity(),sizedouble));

        Count = list.size();

        appsL.setText("Apps: " + String.valueOf(Count));
    }
}
