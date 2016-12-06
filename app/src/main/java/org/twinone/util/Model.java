package org.twinone.util;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Model {
    private File mCurrentDir;
    private File mPreviousDir;
    private Stack<File> mHistory;
    public static final String TAG = "Current dir";

    public Model() {
        init();
    }

    private void init() {
        mHistory = new Stack<>();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mCurrentDir = Environment.getExternalStorageDirectory();

            Log.i(TAG, String.valueOf(mCurrentDir));
        } else {
            Log.i(TAG, "External storage unavailable");
        }
    }

    public File getmCurrentDir() {
        return mCurrentDir;
    }


    public void setmCurrentDir(File mCurrentDir) {
        this.mCurrentDir = mCurrentDir;
    }


    public boolean hasmPreviousDir() {
        return !mHistory.isEmpty();
    }


    public File getmPreviousDir() {
        return mHistory.pop();
    }

    public void setmPreviousDir(File mPreviousDir) {
        this.mPreviousDir = mPreviousDir;
        mHistory.add(mPreviousDir);

    }


    public List<File> getAllFiles(File f) {
        File[] allFiles = f.listFiles();


        List<File> dirs = new ArrayList<>();
        List<File> files = new ArrayList<>();

        for (File file : allFiles) {
            if (file.isDirectory()) {
                dirs.add(file);
            } else {
                files.add(file);
            }
        }

        Collections.sort(dirs);
        Collections.sort(files);

        dirs.addAll(files);

        return dirs;
    }


    public String getMimeType(Uri uri) {
        String mimeType = null;

        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());

        if (MimeTypeMap.getSingleton().hasExtension(extension)) {

            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }
}