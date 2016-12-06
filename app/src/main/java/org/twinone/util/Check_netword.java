package org.twinone.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Android on 3/31/2016.
 */
public class Check_netword {

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = Check_netword.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }


    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = Check_netword.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = Check_netword.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = Check_netword.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
//    public static String isConnectionFast(int type, int subType){
//        if(type==ConnectivityManager.TYPE_WIFI){
//            return "Wifi";
//        }else if(type==ConnectivityManager.TYPE_MOBILE){
//            switch(subType){
//                case TelephonyManager.NETWORK_TYPE_1xRTT:
//                    return "1xRTT"; // ~ 50-100 kbps
//                case TelephonyManager.NETWORK_TYPE_CDMA:
//                    return "CDMA"; // ~ 14-64 kbps
//                case TelephonyManager.NETWORK_TYPE_EDGE:
//                    return "EDGE"; // ~ 50-100 kbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_0:
//                    return "EVDO_0"; // ~ 400-1000 kbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_A:
//                    return "EVDO_A"; // ~ 600-1400 kbps
//                case TelephonyManager.NETWORK_TYPE_GPRS:
//                    return "GPRS"; // ~ 100 kbps
//                case TelephonyManager.NETWORK_TYPE_HSDPA:
//                    return "HSDPA"; // ~ 2-14 Mbps
//                case TelephonyManager.NETWORK_TYPE_HSPA:
//                    return "HSPA"; // ~ 700-1700 kbps
//                case TelephonyManager.NETWORK_TYPE_HSUPA:
//                    return "HSUPA"; // ~ 1-23 Mbps
//                case TelephonyManager.NETWORK_TYPE_UMTS:
//                    return "UMTS"; // ~ 400-7000 kbps
//            /*
//             * Above API level 7, make sure to set android:targetSdkVersion
//             * to appropriate level to use these
//             */
//                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
//                    return "EHRPD"; // ~ 1-2 Mbps
//                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
//                    return "EVDO_B"; // ~ 5 Mbps
//                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
//                    return "HSPAP"; // ~ 10-20 Mbps
//                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
//                    return "IDEN"; // ~25 kbps
//                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
//                    return "LTE"; // ~ 10+ Mbps
//                // Unknown
//                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
//                default:
//                    return "2G";
//            }
//        }else{
//            return "2G";
//        }
//    }
    public static String isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "Wifi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {

                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:

            }
            return "Unknown";

        }

        return "Unknown";
    }



}