package org.twinone.util;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

public class Presenter implements LoaderManager.LoaderCallbacks<List<File>> {
    private UIView mView;
    private Model mModel;
    private FileArrayAdapter mFileArrayAdapter;
    private List<File> mData;
    private AsyncTaskLoader<List<File>> mFileLoader;

    public Presenter(UIView mView) {
        this.mView = mView;
        mModel = new Model();
        mData = new ArrayList<>();
        init();
    }

    private void init() {

        mFileArrayAdapter = new FileArrayAdapter(mView.getActivity(),
                R.layout.list_row, mData);

        mView.setListAdapter(mFileArrayAdapter);
    mView.getActivity().getLoaderManager().initLoader(0, null, this);


        mFileLoader.forceLoad();
    }


    private void updateAdapter(List<File> data) {

        mFileArrayAdapter.clear();

        mFileArrayAdapter.addAll(data);

        mFileArrayAdapter.notifyDataSetChanged();
    }

    public void listItemClicked(ListView l, View v, int position, long id) {

        File fileClicked = mFileArrayAdapter.getItem(position);

        if (fileClicked.isDirectory()) {

            mModel.setmPreviousDir(mModel.getmCurrentDir());


            mModel.setmCurrentDir(fileClicked);


            if (mFileLoader.isStarted()) {
                mFileLoader.onContentChanged();
            }
        } else {
            openFile(Uri.fromFile(fileClicked));
        }
    }


    public void settings() {
        Toast.makeText(mView.getActivity(), "settings cclicked", Toast.LENGTH_LONG).show();
    }


    private void openFile(Uri fileUri) {

        String mimeType = mModel.getMimeType(fileUri);

        if (mimeType != null) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(fileUri, mimeType);
                mView.getActivity().startActivity(i);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mView.getActivity(), "The System understands this file type," +
                                "but no applications are installed to handle it.",
                        Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(mView.getActivity(), "System doesn't know how to handle that file type!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void backPressed() {
        if (mModel.hasmPreviousDir()) {
            mModel.setmCurrentDir(mModel.getmPreviousDir());

            mFileLoader.onContentChanged();
        }
    }
    public void homePressed() {

        if (mModel.hasmPreviousDir()) {
            mModel.setmCurrentDir(mModel.getmPreviousDir());

            mFileLoader.onContentChanged();
        }
    }

    @Override
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        mFileLoader = new AsyncTaskLoader<List<File>>(mView.getActivity()) {

            @Override
            public List<File> loadInBackground() {
                Log.i("Loader", "loadInBackground()");
                return mModel.getAllFiles(mModel.getmCurrentDir());
            }
        };

        return mFileLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<File>> loader, List<File> data) {

        this.mData = data;

        updateAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<List<File>> loader) {

    }
}