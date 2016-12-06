package org.twinone.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wewe.app.moboost.R;

/**
 * Created by Android on 5/11/2016.
 */
public class Apk_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        View view = inflate.inflate(R.layout.apk_fragment,null);
        return view;
    }
}
