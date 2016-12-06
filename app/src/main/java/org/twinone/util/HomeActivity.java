package org.twinone.util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.*;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.twinone.SplashActivity;
import org.twinone.activity.CleanerActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 8/25/2016.
 */
public class HomeActivity extends Fragment {

    LinearLayout id_junk, id_ph_boost, id_antivirus, id_battery_saver, weatherLinear;
    Button id_advanceClean, cpu_detection, photo_manager;
    ImageView id_applock;
    SeekBar seekStorage, seekRam;
    private static final long MEGA_BYTE = 1048576;
    TextView txtperctStorage, txtRam, textovercast, today_temp, txtcity;

    Button check_overcast;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String description, temperture, city;
    List<String> listOfAllImages = new ArrayList<String>();
    TextView idPhotoCheck;
    SharedPreferences sp;
    SharedPreferences.Editor edt;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        View view = inflate.inflate(R.layout.homeactivity,null);

        sp = getActivity().getSharedPreferences("shared_getplaces", getActivity().MODE_MULTI_PROCESS);
        edt = sp.edit();

//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String device_id=  telephonyManager.getDeviceId();



        File dir = Environment.getExternalStorageDirectory();  // this is point to main directory at sdcard -> /mnt/storage

        File[] files = dir.listFiles();

        long size1 = 0;

        for (File f : files) {
            size1 += f.length()/1024;
        }

        Log.i("SIZE", "" + size1);

        String sizeam= Common_methods.getHumanReadableSize(size1, getActivity());


        initializeCache();

        long size = 0;

        preferences = getActivity().getSharedPreferences("list_index", Context.MODE_PRIVATE);
        editor = preferences.edit();




        today_temp = (TextView) view.findViewById(R.id.today_temp);
        textovercast = (TextView) view.findViewById(R.id.textovercast);

        seekStorage = (SeekBar) view.findViewById(R.id.seekStorage);
        check_overcast = (Button) view.findViewById(R.id.check_overcast);

        seekRam = (SeekBar) view.findViewById(R.id.seekRam);

        txtperctStorage = (TextView) view.findViewById(R.id.txtperctStorage);
        txtRam = (TextView) view.findViewById(R.id.txtRam);

        id_junk = (LinearLayout) view.findViewById(R.id.id_junk);
        id_advanceClean = (Button) view.findViewById(R.id.id_advanceClean);
        cpu_detection = (Button) view.findViewById(R.id.cpu_detection);
        photo_manager = (Button) view.findViewById(R.id.photo_manager);
        id_ph_boost = (LinearLayout) view.findViewById(R.id.id_ph_boost);
        id_antivirus = (LinearLayout) view.findViewById(R.id.id_antivirus);
        id_battery_saver = (LinearLayout) view.findViewById(R.id.id_battery_saver);

        id_applock = (ImageView) view.findViewById(R.id.id_applock);
        txtcity = (TextView) view.findViewById(R.id.txtcity);
        weatherLinear = (LinearLayout) view.findViewById(R.id.weatherLinear);
        idPhotoCheck = (TextView) view.findViewById(R.id.idPhotoCheck);

        ArrayList<String> list = getAllShownImagesPath(getActivity());


        if (list.size() > 7) {
            for (int i = 0; i < 6; i++) {

                String path = list.get(i);

                File imgFile = new File(path);

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.custom_apps, null);

                TextView txtAppNm = (TextView) v.findViewById(R.id.txtAppNm);
                ImageView thumbImage = (ImageView) v.findViewById(R.id.thumbImage);

                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    myBitmap = resizeImafge(myBitmap);

                    thumbImage.setImageBitmap(myBitmap);
                    txtAppNm.setVisibility(View.GONE);

                }


                weatherLinear.addView(v);
            }

        } else if (list.size() < 7) {
            for (int i = 0; i < list.size(); i++) {

                String path = list.get(i);

                File imgFile = new File(path);

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.custom_apps, null);

                TextView txtAppNm = (TextView) v.findViewById(R.id.txtAppNm);
                ImageView thumbImage = (ImageView) v.findViewById(R.id.thumbImage);

                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    myBitmap = resizeImafge(myBitmap);

                    thumbImage.setImageBitmap(myBitmap);
                    txtAppNm.setVisibility(View.GONE);

                }


                weatherLinear.addView(v);
            }


        } else {
            idPhotoCheck.setText("No photos exist");
            photo_manager.setVisibility(View.GONE);
        }


        photo_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Duplicate_photos.class);
                startActivity(it);
            }
        });

        seekStorage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        seekRam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        float sp = freeSpace(true);
        float totalSpace = totalSpace(true);
        float perct = (sp / totalSpace) * 100;
        int f = Integer.valueOf((int) perct);

        f = 100 - f;

        float memoryAvail = availableMemory();
        float ram = getTotalRAM();
        float perct1 = (memoryAvail / ram) * 100;
        int rampercentage = Integer.valueOf((int) perct1);
        rampercentage = 100 - rampercentage;

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // will update the "progress" propriety of seekbar until it reaches progress
            ObjectAnimator animation = ObjectAnimator.ofInt(seekStorage, "progress", f);
            animation.setDuration(4000); // 0.5 second
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();

            ObjectAnimator animation1 = ObjectAnimator.ofInt(seekRam, "progress", rampercentage);
            animation1.setDuration(4000); // 0.5 second
            animation1.setInterpolator(new DecelerateInterpolator());
            animation1.start();


            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues(0, f);
            animator.setDuration(4000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtperctStorage.setText("" + (int) animation.getAnimatedValue() + "%");
                }
            });
            animator.start();

            ValueAnimator animator1 = new ValueAnimator();
            animator1.setObjectValues(0, rampercentage);
            animator1.setDuration(4000);
            animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    txtRam.setText("" + (int) animation.getAnimatedValue() + "%");
                }
            });
            animator1.start();
        } else {
            seekStorage.setProgress(f);
            txtperctStorage.setText(String.valueOf(f) + "%");
            seekRam.setProgress(rampercentage);
            txtRam.setText(String.valueOf(rampercentage) + "%");
        }


        check_overcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), WeatherReport.class);
                startActivity(it);

            }
        });



        id_applock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), SplashActivity.class);
                startActivity(it);

            }
        });


        cpu_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Cpu_cooler.class);
                startActivity(it);

            }
        });

        id_advanceClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Home_activity.class);
                startActivity(it);
                // finish();
            }
        });



        id_junk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), CleanerActivity.class);
                startActivity(it);


            }
        });
        id_ph_boost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Phone_boost.class);
                startActivity(it);


            }
        });
        id_antivirus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), App_manager.class);
                startActivity(it);


            }
        });

        id_battery_saver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Home_battery_saver.class);
                startActivity(it);


            }
        });



        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        description = sp.getString("description", "");
        temperture = sp.getString("temperture", "");
        city = sp.getString("place", "");

        textovercast.setText(description);
        today_temp.setText(temperture);
        txtcity.setText(city);
    }


    public static int freeSpace(boolean external) {
        StatFs statFs = getStats(external);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        long freeBytes = availableBlocks * blockSize;

        return (int) (freeBytes / MEGA_BYTE);
    }

    static StatFs getStats(boolean external) {
        String path;

        if (external) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            path = Environment.getRootDirectory().getAbsolutePath();
        }

        return new StatFs(path);
    }


    public static int totalSpace(boolean external) {
        StatFs statFs = getStats(external);
        long total = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / MEGA_BYTE;
        return (int) total;
    }

    public long availableMemory() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        return availableMegs;

    }

    public long getTotalRAM() {

        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem / 1048576L;
        return totalMemory;

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    private void initializeCache() {
        long size = 0;
        size += getDirSize(getActivity().getCacheDir());
        size += getDirSize(getActivity().getExternalCacheDir());

        String total = readableFileSize(size);


        //    ((TextView) findViewById(R.id.yourTextView)).setText(readableFileSize(size));
    }


    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public long getDirSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public void getAllPhotos() {
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


        Cursor cur = getActivity().managedQuery(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        Log.e("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            String data;
            int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);

            int imageDataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);


            do {
                // Get the field values

                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(imageDataColumn);

                //  listOfAllImages.add(data);

                // Do something with the values.
                Log.i("ListingImages", " bucket=" + bucket + "  date_taken=" + date);
            } while (cur.moveToNext());

        }


    }

    public static ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }


    public static ArrayList<String> getAllVideos(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllVideos = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllVideos.add(absolutePathOfImage);
        }
        return listOfAllVideos;
    }


    public Bitmap resizeImafge(Bitmap bitImage) {


        int width = bitImage.getWidth();
        int height = bitImage.getHeight();
        int newWidth = 160;
        int newHeight = 160;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // rotate the Bitmap
        matrix.postRotate(0);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitImage, 0, 0, width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the BitMap
        // to the ImageView, ImageButton or what ever


        return resizedBitmap;
    }





}
