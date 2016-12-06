package org.twinone.util;

import android.content.pm.ApplicationInfo;

/**
 * Created by Android on 5/3/2016.
 */
public class CommLockInfo {


    private Long id;
    private String packageName;
    private Boolean isLocked;
    private Boolean isFaviterApp;
    private ApplicationInfo appInfo;

    public CommLockInfo() {
    }

    public CommLockInfo(Long id) {
        this.id = id;
    }

    public CommLockInfo(Long id, String packageName, Boolean isLocked,
                        Boolean isFaviterApp) {
        this.id = id;
        this.packageName = packageName;
        this.isLocked = isLocked;
        this.isFaviterApp = isFaviterApp;
    }

    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getIsFaviterApp() {
        return isFaviterApp;
    }

    public void setIsFaviterApp(Boolean isFaviterApp) {
        this.isFaviterApp = isFaviterApp;
    }

}
