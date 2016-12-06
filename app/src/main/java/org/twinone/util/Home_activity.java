package org.twinone.util;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IDataStatus;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.imgproc.Imgproc;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/10/2016.
 */
public class Home_activity extends Activity {

    RelativeLayout viewClick, back;
    LinearLayout otherViews;
    String formatSize, formatSizeImages;
    TextView lbl_cache_size, idTxt, photosDelete;
    public static final int FETCH_PACKAGE_SIZE_COMPLETED = 100;
    public static final int ALL_PACAGE_SIZE_COMPLETED = 200;
    IDataStatus onIDataStatus;
    TextView cache_size, total_space, free_space;
    ProgressDialog pd;
    private static final long MEGA_BYTE = 1024;
    Button btnClear, idGouNINSTALL;
    PackageManager packageManager;
    static long totalSize, sizeImages, sizeImagesVideos;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> listVideoPath = new ArrayList<String>();
    ArrayList<File> listVideoSize = new ArrayList<File>();
    Button id_photos, id_videos, audio;
    TextView txtVideoSize, txtaudio;
    ArrayList<String> listImages;
    ArrayList<String> listapps = new ArrayList<String>();
    ArrayList<Boolean> listIsBlur = new ArrayList<>();
     InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        viewClick = (RelativeLayout) findViewById(R.id.viewClick);
        lbl_cache_size = (TextView) findViewById(R.id.txtperctStorage);
        cache_size = (TextView) findViewById(R.id.cache_size);
        total_space = (TextView) findViewById(R.id.total_space);
        free_space = (TextView) findViewById(R.id.free_space);
        btnClear = (Button) findViewById(R.id.btnClear);
        idTxt = (TextView) findViewById(R.id.idTxt);
        idGouNINSTALL = (Button) findViewById(R.id.idGouNINSTALL);
        id_photos = (Button) findViewById(R.id.id_photos);
        id_videos = (Button) findViewById(R.id.id_videos);
        audio = (Button) findViewById(R.id.audio);
        photosDelete = (TextView) findViewById(R.id.photosDelete);
        txtVideoSize = (TextView) findViewById(R.id.txtVideoSize);
        txtaudio = (TextView) findViewById(R.id.txtaudio);

        back = (RelativeLayout) findViewById(R.id.back);
        otherViews = (LinearLayout) findViewById(R.id.otherViews);
        final ToggleButton idToggle = (ToggleButton) findViewById(R.id.idToggle);


        Intent it = getIntent();

        String adds = it.getStringExtra("adds");

       // String device_id = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        if(adds!=null){

        }

        File lisVideos = getVideoList();

        long sizeVideo = lisVideos.length();


        packageManager = getApplicationContext().getPackageManager();


        ArrayList<String> listVideos = HomeActivity.getAllVideos(this);
        if (listVideos.size() > 0) {
            for (int i = 0; i < listVideos.size(); i++) {

                String path = listVideos.get(i);

                File imgFile = new File(path);

                long size = imgFile.length();

                sizeImagesVideos = size + sizeImagesVideos;


            }

            sizeImagesVideos = sizeImagesVideos / MEGA_BYTE;

            String videos = Common_methods.getHumanReadableSize(sizeImagesVideos, Home_activity.this);

            txtVideoSize.setText(" Delete to save " + videos);

        } else {
            txtVideoSize.setText(" No files found ");
        }

        //////////////////////////

        listImages = HomeActivity.getAllShownImagesPath(this);
        if (listImages.size() > 0) {
            for (int i = 0; i < listImages.size(); i++) {

                String path = listImages.get(i);

                File f = new File(path);
                if(f!=null || f.exists()){

                    long size = Common_methods.getFileSize(f);
                    sizeImages =size+sizeImages;
                }

            }


            String images = Common_methods.getHumanReadableSize(sizeImages, Home_activity.this);

            photosDelete.setText(" Delete to save " + images);

        } else {
            photosDelete.setText(" No files found ");
        }

        String strAudio = Common_methods.getAllMusic(Home_activity.this);

        txtaudio.setText(" Delete to save " + strAudio);


        float freespace = freeSpace(true);

        float totalSpace = totalSpace(true);


        float perct = (freespace / totalSpace) * 100;
        int f = Integer.valueOf((int) perct);

        f = 100 - f;

        lbl_cache_size.setText(Integer.valueOf(f) + "% ");


        String formatfreespace = Common_methods.getAvailableExternalMemorySize();
        String formattotalSpace = Common_methods.getTotalExternalMemorySize();

        total_space.setText("Total: " + formattotalSpace);
        free_space.setText("Free: " + formatfreespace);

        packageSize = 0;
        showProgress("Calculating Cache Size..!!!");

        refresh();

        Log.e("list", "" + list);


        File dir = Environment.getExternalStorageDirectory();  // this is point to main directory at sdcard -> /mnt/storage

        File[] files = dir.listFiles();


        int size = 0;

        for (File fl : files) {
            size += fl.length() / 1024;
        }

        Log.i("SIZE", "" + size);

        new Thread(new Runnable() {

            @Override
            public void run() {
                getpackageSize();


            }
        }).start();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        id_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Home_activity.this, My_photos.class);
                startActivity(it);

            }
        });

        id_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Home_activity.this, My_photos.class);
                it.putExtra("videos", "videos");
                startActivity(it);

            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Home_activity.this, Audio.class);

                startActivity(it);

            }
        });

        viewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otherViews.getVisibility() == View.VISIBLE) {
                    otherViews.setVisibility(View.GONE);
                    idToggle.setChecked(false);
                } else if (otherViews.getVisibility() != View.VISIBLE) {
                    otherViews.setVisibility(View.VISIBLE);
                    idToggle.setChecked(true);
                }

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllCache();

            }
        });

    }


    public long freeSpace(boolean external) {
        StatFs statFs = HomeActivity.getStats(external);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        long freeBytes = availableBlocks * blockSize;

        return (long) (freeBytes / MEGA_BYTE);
    }

    public long totalSpace(boolean external) {
        StatFs statFs = HomeActivity.getStats(external);
        long total = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize()));
        return (long) (total / MEGA_BYTE);
    }

    private void showProgress(String message) {
        pd = new ProgressDialog(this);
        pd.setIcon(R.drawable.logo_blue);
        pd.setTitle("Please Wait...");
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.show();

    }

    long packageSize = 0, size = 0;
    AppDetails cAppDetails;
    public ArrayList<AppDetails.PackageInfoStruct> res;

    private void getpackageSize() {
        cAppDetails = new AppDetails(this);
        res = cAppDetails.getPackages();
        if (res == null)
            return;
        for (int m = 0; m < res.size(); m++) {
            PackageManager pm = getPackageManager();
            Method getPackageSizeInfo;
            try {
                getPackageSizeInfo = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class,
                        IPackageStatsObserver.class);
                getPackageSizeInfo.invoke(pm, res.get(m).pname,
                        new cachePackState());
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        handle.sendEmptyMessage(ALL_PACAGE_SIZE_COMPLETED);
        Log.v("Total Cache Size", " " + packageSize);

    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FETCH_PACKAGE_SIZE_COMPLETED:
                    if (packageSize > 0)
                        size = (packageSize / 1024000);
                    cache_size.setText(size + "MB" + " of junk can be cleaned");
                    break;
                case ALL_PACAGE_SIZE_COMPLETED:
                    if (null != pd)
                        if (pd.isShowing())
                            pd.dismiss();

                    break;
                default:
                    break;
            }

        }

    };

    private class cachePackState extends IPackageStatsObserver.Stub {


        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException {
            Log.d("Package Size", pStats.packageName + "");
            Log.i("Cache Size", pStats.cacheSize + "");
            Log.w("Data Size", pStats.dataSize + "");
            packageSize = packageSize + pStats.cacheSize;
            Log.v("Total Cache Size", " " + packageSize);

            long codeSize = pStats.codeSize;
            long cacheSize = pStats.cacheSize;
            long dataSize = pStats.dataSize;

            long total = codeSize + cacheSize + dataSize;


            handle.sendEmptyMessage(FETCH_PACKAGE_SIZE_COMPLETED);
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

        Intent it = new Intent(Home_activity.this, Home_activity.class);
        it.putExtra("adds","adds");
        startActivity(it);
        overridePendingTransition(0, 0);
        finish();
    }



    private File getVideoList() {

        // which image properties are we querying
        String[] PROJECTION_BUCKET = {MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME, MediaStore.Video.VideoColumns.DATE_TAKEN,
                MediaStore.Video.VideoColumns.DATA};

        String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        // Get the base URI for the People table in the Contacts content
        // provider.
        Uri images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        File f = new File(images.getPath());

        listVideoSize.add(f);

        long size = f.length();

        Cursor cur = getContentResolver().query(images, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

        Log.v("ListingImages", " query count=" + cur.getCount());

        GalleryPhotoAlbum album;

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            String data;
            long bucketId;

            int bucketColumn = cur.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(MediaStore.Video.Media.DATA);

            int bucketIdColumn = cur.getColumnIndex(MediaStore.Video.Media.BUCKET_ID);

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                listVideoPath.add(data);


                bucketId = cur.getInt(bucketIdColumn);


            } while (cur.moveToNext());
        }
        cur.close();


        return f;
    }



    public void refresh()
    {

        int positionapps = -1;
        long  sizedouble = 0;
        list.clear();

        Log.d("", "");

        packageManager = getApplicationContext().getPackageManager();

        List<ApplicationInfo> apps = getPackageManager().getInstalledApplications(0);


        for (int i = 0; i < apps.size(); i++) {

            ApplicationInfo app = apps.get(i);

            if ((app.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                String packageNm = app.processName;
            } else {

                final String packageNm = app.processName;


                long apkSize = new File(app.sourceDir).length();

                sizedouble = sizedouble + apkSize;


                String name = getPackageManager().getApplicationLabel(app).toString();
                listapps.add(name);


                String apkPath = app.sourceDir;

                positionapps = positionapps + 1;


            }


        }

        int   Count = listapps.size();

        idTxt.setText(String.valueOf(Count) +" Apps are using " + Uninsatll_fragment.formateFileSize(this,sizedouble));


        idGouNINSTALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Home_activity.this, App_manager.class);
                startActivity(it);
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

   public void loadAdd(){
       mInterstitialAd = new InterstitialAd(this);

       // set the ad unit ID
       mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

       AdRequest adRequest = new AdRequest.Builder()
               .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                       // Check the LogCat to get your test device ID
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
               //   Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onAdOpened() {
               //   Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
           }
       });

   }
}


