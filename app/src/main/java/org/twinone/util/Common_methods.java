package org.twinone.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.util.Log;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import wewe.app.moboost.R;

/**
 * Created by Android on 4/13/2016.
 */
public class Common_methods extends Application {

    public static String ERROR = "Something went wrong";


    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return formatSize(totalBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    private static final long MEGA_BYTE = 1048576;


    public static long availableMemory(Context ct) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) ct.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        return availableMegs;

    }

    public static long getTotalRAM(Context ct) {

        ActivityManager activityManager = (ActivityManager) ct.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem / 1048576L;

        return totalMemory;

    }

    public static double totalSpace(boolean external) {
        StatFs statFs = getStats(external);
        long total = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / MEGA_BYTE;
        return total;
    }

    private static StatFs getStats(boolean external) {
        String path;

        if (external) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            path = Environment.getRootDirectory().getAbsolutePath();
        }

        return new StatFs(path);
    }

    public static double freeSpace(boolean external) {
        StatFs statFs = getStats(external);
        long availableBlocks = statFs.getAvailableBlocks();
        long blockSize = statFs.getBlockSize();
        long freeBytes = availableBlocks * blockSize;

        return (freeBytes / MEGA_BYTE);
    }


    public static String getAppName(Context ct, int pID) {
        String processName = "";
        ActivityManager am = (ActivityManager) ct.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = ct.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    //Log.d("Process", "Id: "+ info.pid +" ProcessName: "+ info.processName +"  Label: "+c.toString());
                    //processName = c.toString();
                    processName = info.processName;
                }
            } catch (Exception e) {
                //Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public static int getPidMemorySize(int pid, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int[] myMempid = new int[]{pid};
        Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
        memoryInfo[0].getTotalSharedDirty();

        // int memSize = memoryInfo[0].dalvikPrivateDirty;
        // TODO PSS
        int memSize = memoryInfo[0].getTotalPss();
        // int memSize = memoryInfo[0].getTotalPrivateDirty();
        return memSize;
    }

    private String getAppName(int pID) {
        String processName = "";
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    //Log.d("Process", "Id: "+ info.pid +" ProcessName: "+ info.processName +"  Label: "+c.toString());
                    //processName = c.toString();
                    processName = info.processName;

                    //   listpackageName.add(processName);

                }
            } catch (Exception e) {
                //Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public static int getPss(Context ct, String processName) {
        ActivityManager am = (ActivityManager) (ActivityManager) ct.getSystemService(ACTIVITY_SERVICE);

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
    private void getAllFilesOfDir(File directory) {
        Log.e("tag", "Directory: " + directory.getAbsolutePath() + "\n");

        final File[] files = directory.listFiles();
       // list = new ArrayList<File>(Arrays.asList(files));

        if (files != null) {
            for (File file : files) {
                if (file != null) {
                    if (file.isDirectory()) {  // it is a folder...
                        getAllFilesOfDir(file);

                        //   File fl = getAllFilesOfDir(file);
                        //   listfileFolder.add(fl);


                    } else {  // it is a file...
                        Log.d("tag", "File: " + file.getAbsolutePath() + "\n");
                      //  listfiledDirectory.add(file.getAbsolutePath());

                    }

                }
            }
        }

    }
    public static boolean isThisServiceRunning( final Context context, final String servicePackageName) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (final ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (servicePackageName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static String getHumanReadableSize(long apkSize,Context context) {
        String humanReadableSize;
        if (apkSize < 1024) {
            humanReadableSize = String.format(
                    context.getString(R.string.app_size_b),
                    (double) apkSize
            );
        } else if (apkSize < Math.pow(1024, 2)) {
            humanReadableSize = String.format(
                    context.getString(R.string.app_size_kib),
                    (double) (apkSize / 1024)
            );
        } else if (apkSize < Math.pow(1024, 3)) {
            humanReadableSize = String.format(
                    context.getString(R.string.app_size_mib),
                    (double) (apkSize / Math.pow(1024, 2))
            );
        } else {
            humanReadableSize = String.format(
                    context.getString(R.string.app_size_gib),
                    (double) (apkSize / Math.pow(1024, 3))
            );
        }
        return humanReadableSize;
    }

    public static String getAllMusic(Activity activity) {
     long sizeImages = 0;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listMusicFiles = new ArrayList<String>();
        String absolutePathOfImage = null;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

         cursor = activity.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);


        List<String> songs = new ArrayList<String>();
        while (cursor.moveToNext()) {


            String path = cursor.getString(3);

            File imgFile = new File(path);

            long size = imgFile.getTotalSpace();

            sizeImages = size + sizeImages;


        }


        sizeImages = sizeImages / MEGA_BYTE;

        String videos = Common_methods.getHumanReadableSize(sizeImages,activity);
        return videos;
    }
//    public static void opencvProcess(ArrayList<String> imagepath) {
//
//        for(int i=0;i<imagepath.size();i++){
//
//            String picFilePath =imagepath.get(i);
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inDither = true;
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//
//            Bitmap image = decodeSampledBitmapFromFile(picFilePath, 2000, 2000);
//            int l = CvType.CV_8UC1; //8-bit grey scale image
//            Mat matImage = new Mat();
//            org.opencv.android.Utils.bitmapToMat(image, matImage);
//            Mat matImageGrey = new Mat();
//            Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);
//
//            Bitmap destImage;
//            destImage = Bitmap.createBitmap(image);
//            Mat dst2 = new Mat();
//            org.opencv.android.Utils.bitmapToMat(destImage, dst2);
//            Mat laplacianImage = new Mat();
//            dst2.convertTo(laplacianImage, l);
//            Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);
//            Mat laplacianImage8bit = new Mat();
//            laplacianImage.convertTo(laplacianImage8bit, l);
//
//            Bitmap bmp = Bitmap.createBitmap(laplacianImage8bit.cols(), laplacianImage8bit.rows(), Bitmap.Config.ARGB_8888);
//            org.opencv.android.Utils.matToBitmap(laplacianImage8bit, bmp);
//            int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
//            bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight()); // bmp为轮廓图
//
//            int maxLap = -16777216; // 16m
//            for (int pixel : pixels) {
//                if (pixel > maxLap)
//                    maxLap = pixel;
//            }
//
//            int soglia = -6118750;
//            if (maxLap <= soglia) {
//                Log.e("is blur image","imahe is blur....");
//            }
//            soglia += 6118750;
//            maxLap += 6118750;
//            String photodesc="Photos Location=" + picFilePath + "\nimage.w=" + image.getWidth()+ "image.h=" + image.getHeight()
//                    + "\nmaxLap= " + maxLap + "(Clear scope:0~6118750)"
//                    + "\n" + Html.fromHtml("<font color='#eb5151'><b>" + (maxLap <= soglia ? "blurry" : "Clear") + "</b></font>");
//
//            Log.e("photodesc",""+photodesc);
//            boolean opencvEnd = true;
//            boolean isBlur = maxLap <= soglia;
//
//            Log.e(String.valueOf(maxLap),String.valueOf(soglia) +isBlur);
//            Log.e(String.valueOf(maxLap),String.valueOf(soglia) +opencvEnd);
//
//
//
//        }
//
//
//    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    public static Bitmap blurRenderScript(Context context,Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }
    public static long getFileSize(final File file)
    {
        if(file==null||!file.exists())
            return 0;
        if(!file.isDirectory())
            return file.length();
        final List<File> dirs=new LinkedList<File>();
        dirs.add(file);
        long result=0;
        while(!dirs.isEmpty())
        {
            final File dir=dirs.remove(0);
            if(!dir.exists())
                continue;
            final File[] listFiles=dir.listFiles();
            if(listFiles==null||listFiles.length==0)
                continue;
            for(final File child : listFiles)
            {
                result+=child.length();
                if(child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;
    }

    public static boolean isNetworkConnected(Context ct) {
        ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public static void showSettingsAlert(final Context ct) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ct);

        alertDialog.setTitle("No Internet");

        alertDialog.setMessage(ct.getResources().getString(R.string.please_check_internet));

        alertDialog.setPositiveButton(ct.getResources().getString(R.string.check),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ct.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));

                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton(ct.getResources().getString(R.string.btn_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }


    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
            }
        }
    }

}
