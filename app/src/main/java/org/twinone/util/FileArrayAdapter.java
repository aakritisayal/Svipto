package org.twinone.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import wewe.app.moboost.R;


public class FileArrayAdapter extends ArrayAdapter<File> {
    private Context mContext;
    private int mResource;
    private List<File> mObjects;

    public FileArrayAdapter(Context c, int res, List<File> o) {
        super(c, res, o);
        mContext = c;
        mResource = res;
        mObjects = o;
    }

    public FileArrayAdapter(Context c, int res) {
        super(c, res);
        mContext = c;
        mResource = res;
    }

    @Override
    public File getItem(int i) {
        return mObjects.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater.from(mContext));

            v = inflater.inflate(mResource, null);
        }


        ImageView iv = (ImageView) v.findViewById(R.id.imageView);

        TextView nameView = (TextView) v.findViewById(R.id.name_text_view);

        TextView detailsView = (TextView) v.findViewById(R.id.details_text_view);

        File file = getItem(position);


        if (file.isDirectory()) {
            iv.setImageResource(R.drawable.new_files);
        } else {
            iv.setImageResource(R.drawable.file);
            if (file.length() > 0) {
                detailsView.setText(String.valueOf(file.length()));
            }
        }

        nameView.setText(file.getName());


        return v;
    }
}