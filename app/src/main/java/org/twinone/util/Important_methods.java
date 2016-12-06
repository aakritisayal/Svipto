package org.twinone.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Android on 4/20/2016.
 */
public class Important_methods extends Activity {


    public void getData(){

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (int i = 0; i < recentTasks.size(); i++)
        {

            String app = recentTasks.get(i).baseActivity.toShortString();


            Log.d("Executed app", "Application executed : " + recentTasks.get(i).baseActivity.toShortString() + "\t\t ID: " + recentTasks.get(i).id + "");
        }


    }


    public void getAllBackgroundServices(){

        PackageManager pm = this.getPackageManager();

        ActivityManager localActivityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> service = localActivityManager.getRunningServices(100);


        for (int i = 0; i < service.size(); i++) {

            ActivityManager.RunningServiceInfo info = service.get(i);

            String packages = info.process;


            try {
                PackageInfo pi = pm.getPackageInfo(packages, 0);
                ApplicationInfo ai = pm.getApplicationInfo(pi.packageName, 0);

                if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

                    String packages1 = pi.packageName;


                    int p = info.pid;


                    int memory = getPidMemorySize(p, this);


//                    Collections.sort(listsort,Collections.reverseOrder());

                    String strmemory = getFileSize(memory);


                    int pss = getPss(packages1);


                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }


    }
    public int getPidMemorySize(int pid, Context context) {
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


}
