package org.twinone.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.twinone.activity.SettingsActivity;
import org.twinone.model.AppsListItem;
import org.twinone.model.CleanerService;
import org.twinone.model.adapter.AppsListAdapter;
import org.twinone.widget.DividerDecoration;
import org.twinone.widget.RecyclerView;

import java.util.List;

import wewe.app.moboost.R;

public class CleanerFragment extends Fragment implements CleanerService.OnActionListener {

    private static final int REQUEST_STORAGE = 0;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private CleanerService mCleanerService;
    private AppsListAdapter mAppsListAdapter;
    private TextView mEmptyView;
    private SharedPreferences mSharedPreferences;
    private ProgressDialog mProgressDialog;
    private View mProgressBar;
    private TextView mProgressBarText;
    private LinearLayoutManager mLayoutManager;
    private Menu mOptionsMenu;

    private boolean mAlreadyScanned = false;
    private boolean mAlreadyCleaned = false;
    private String mSearchQuery;

    private String mSortByKey;
    private String mCleanOnAppStartupKey;
    private String mExitAfterCleanKey;
    static Button btnClearCache;
    InterstitialAd mInterstitialAd;
    String device_id;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCleanerService = ((CleanerService.CleanerServiceBinder) service).getService();
            mCleanerService.setOnActionListener(CleanerFragment.this);

            updateStorageUsage();

            if (!mCleanerService.isCleaning() && !mCleanerService.isScanning()) {
                if (mSharedPreferences.getBoolean(mCleanOnAppStartupKey, false) &&
                        !mAlreadyCleaned) {
                    mAlreadyCleaned = true;

                    cleanCache();
                } else if (!mAlreadyScanned) {
                    mCleanerService.scanCache();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCleanerService.setOnActionListener(null);
            mCleanerService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        mSortByKey = getString(R.string.sort_by_key);
        mCleanOnAppStartupKey = getString(R.string.clean_on_app_startup_key);
        mExitAfterCleanKey = getString(R.string.exit_after_clean_key);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mAppsListAdapter = new AppsListAdapter();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle(R.string.cleaning_cache);
        mProgressDialog.setMessage(getString(R.string.cleaning_in_progress));
        mProgressDialog.setIcon(R.drawable.icon);
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return true;
            }
        });

        getActivity().getApplication().bindService(new Intent(getActivity(), CleanerService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cleaner_fragment, container, false);

        device_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        MobileAds.initialize(getActivity(), "ca-app-pub-9480918527727973~5322887640");

        mInterstitialAd = new InterstitialAd(getActivity());
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                showAddView();
            }
        });



       // showAddView();
        mEmptyView = (TextView) rootView.findViewById(R.id.empty_view);
        btnClearCache = (Button) rootView.findViewById(R.id.btnClearCache);

        mLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAppsListAdapter);
        recyclerView.setEmptyView(mEmptyView);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));

        mProgressBar = rootView.findViewById(R.id.progressBar);
        mProgressBarText = (TextView) rootView.findViewById(R.id.progressBarText);

        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCleanerService != null && !mCleanerService.isScanning() &&
                        !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
                    mAlreadyCleaned = false;

                    cleanCache();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;

        inflater.inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchQuery = query;

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (ViewCompat.isLaidOut(searchView) && mSearchQuery != null) {
                    String oldText = mSearchQuery;

                    mSearchQuery = newText;

                    if (!oldText.equals(newText)) {
                        mAppsListAdapter.sortAndFilter(getActivity(), getSortBy(), newText);
                    }
                }

                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        if (mSearchQuery == null) {
                            mSearchQuery = "";
                        }

                        mAppsListAdapter.setShowHeaderView(false);

                        mEmptyView.setText(R.string.no_such_app);

                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mSearchQuery = null;

                        mAppsListAdapter.clearFilter();

                        mAppsListAdapter.setShowHeaderView(true);

                        if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            mLayoutManager.scrollToPosition(0);
                        }

                        mEmptyView.setText(R.string.empty_cache);


                        return true;
                    }
                });

        if (mSearchQuery != null) {
            MenuItemCompat.expandActionView(searchItem);

            searchView.setQuery(mSearchQuery, false);
        }

        updateOptionsMenu();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clean:
                if (mCleanerService != null && !mCleanerService.isScanning() &&
                        !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
                    mAlreadyCleaned = false;

                    cleanCache();
                }
                return true;

            case R.id.action_refresh:
                if (mCleanerService != null && !mCleanerService.isScanning() &&
                        !mCleanerService.isCleaning()) {
                    mCleanerService.scanCache();
                }
                return true;

            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;

            case R.id.action_sort_by_app_name:
                setSortBy(AppsListAdapter.SortBy.APP_NAME);
                updateOptionsMenu();
                return true;

            case R.id.action_sort_by_cache_size:
                setSortBy(AppsListAdapter.SortBy.CACHE_SIZE);
                updateOptionsMenu();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        updateOptionsMenu();
    }

    @Override
    public void onDestroyOptionsMenu() {
        mOptionsMenu = null;
    }

    @Override
    public void onResume() {
        updateStorageUsage();

        updateOptionsMenu();

        if (mCleanerService != null) {
            if (mCleanerService.isScanning() && !isProgressBarVisible()) {
                showProgressBar(true);
            } else if (!mCleanerService.isScanning() && isProgressBarVisible()) {
                showProgressBar(false);
            }

            if (mCleanerService.isCleaning() && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        getActivity().getApplication().unbindService(mServiceConnection);

        super.onDestroy();
    }

    private void updateOptionsMenu() {
        if (mOptionsMenu != null) {
            mOptionsMenu.findItem(R.id.action_sort_by_app_name).setVisible(
                    getSortBy() == AppsListAdapter.SortBy.CACHE_SIZE);
            mOptionsMenu.findItem(R.id.action_sort_by_cache_size).setVisible(
                    getSortBy() == AppsListAdapter.SortBy.APP_NAME);
        }
    }

    private void updateStorageUsage() {
        if (mAppsListAdapter != null) {
            StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());

            long totalMemory = (long) stat.getBlockCount() * (long) stat.getBlockSize();
            long medMemory = mCleanerService != null ? mCleanerService.getCacheSize() : 0;
            long lowMemory = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                    !Environment.isExternalStorageEmulated()) {
                stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());

                totalMemory += (long) stat.getBlockCount() * (long) stat.getBlockSize();
                lowMemory += (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
            }

            long highMemory = totalMemory - medMemory - lowMemory;

            mAppsListAdapter.updateStorageUsage(totalMemory, lowMemory, medMemory, highMemory);
        }
    }

    private AppsListAdapter.SortBy getSortBy() {
        try {
            return AppsListAdapter.SortBy.valueOf(mSharedPreferences.getString(mSortByKey,
                    AppsListAdapter.SortBy.CACHE_SIZE.toString()));
        } catch (ClassCastException e) {
            return AppsListAdapter.SortBy.CACHE_SIZE;
        }
    }

    private void setSortBy(AppsListAdapter.SortBy sortBy) {
        mSharedPreferences.edit().putString(mSortByKey, sortBy.toString()).apply();

        if (mCleanerService != null && !mCleanerService.isScanning() &&
                !mCleanerService.isCleaning()) {
            mAppsListAdapter.sortAndFilter(getActivity(), sortBy, mSearchQuery);
        }
    }

    private boolean isProgressBarVisible() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showStorageRationale() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle(R.string.rationale_title);
        dialog.setMessage(getString(R.string.rationale_storage));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
    }

    private void cleanCache() {
        if (!CleanerService.canCleanExternalCache(getActivity())) {
            if (shouldShowRequestPermissionRationale(PERMISSIONS_STORAGE[0])) {
                showStorageRationale();
            } else {
                requestPermissions(PERMISSIONS_STORAGE, REQUEST_STORAGE);
            }
        } else {
            mCleanerService.cleanCache();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCleanerService.cleanCache();
            } else {
                showStorageRationale();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onScanStarted(Context context) {
        if (isAdded()) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            btnClearCache.setVisibility(View.GONE);

            mProgressBarText.setText(R.string.scanning);
            showProgressBar(true);
        }
    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max) {
        if (isAdded()) {
            mProgressBarText.setText(getString(R.string.scanning_m_of_n, current, max));
        }
    }

    @Override
    public void onScanCompleted(Context context, List<AppsListItem> apps) {
        mAppsListAdapter.setItems(getActivity(), apps, getSortBy(), mSearchQuery);

        if (isAdded()) {
            updateStorageUsage();

            showProgressBar(false);
        }
        btnClearCache.setVisibility(View.VISIBLE);

        if (apps.size() == 0) {
            btnClearCache.setVisibility(View.GONE);

     //       showAdd();

            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Toast.makeText(getActivity(), "Ad did not load", Toast.LENGTH_SHORT).show();
                // startGame();
            }



//            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//            }


//            mInterstitialAd = new InterstitialAd(getActivity());
//
//            // set the ad unit ID
//            mInterstitialAd.setAdUnitId("ca-app-pub-9480918527727973/5183286843");
//
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                            // Check the LogCat to get your test device ID
//                    .build();
//
//            mInterstitialAd.loadAd(adRequest);
//
//            mInterstitialAd.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    showInterstitial();
//                }
//
//                @Override
//                public void onAdClosed() {
//                    //   Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onAdFailedToLoad(int errorCode) {
//                    Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onAdLeftApplication() {
//                    // Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onAdOpened() {
//                    // Toast.makeText(getActivity(), "Ad is opened!", Toast.LENGTH_SHORT).show();
//                }
//            });


        }
        mAlreadyScanned = true;
    }

    @Override
    public void onCleanStarted(Context context) {
        if (isAdded()) {
            if (isProgressBarVisible()) {
                showProgressBar(false);
            }

            if (!getActivity().isFinishing()) {
                mProgressDialog.show();
            }
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onCleanCompleted(Context context, boolean succeeded) {
        if (succeeded) {
            mAppsListAdapter.trashItems();
        }

        if (isAdded()) {
            updateStorageUsage();

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
        btnClearCache.setVisibility(View.GONE);


        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(getActivity(), "Ad did not load", Toast.LENGTH_SHORT).show();
           // startGame();
        }

       // showAdd();

//        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }

//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//
//            @Override
//            public void onAdClosed() {
//               // Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//               // Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//              //  Toast.makeText(getActivity(), "Ad is opened!", Toast.LENGTH_SHORT).show();
//            }
//        });

        Toast.makeText(context, succeeded ? R.string.cleaned : R.string.toast_could_not_clean,
                Toast.LENGTH_LONG).show();

        if (succeeded && getActivity() != null && !mAlreadyCleaned &&
                mSharedPreferences.getBoolean(mExitAfterCleanKey, false)) {
            getActivity().finish();
        }
    }


    public void showAddView() {

//        MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544~3347511713");
//
//        mInterstitialAd = new InterstitialAd(getActivity());
//
//        // set the ad unit ID
//        mInterstitialAd.setAdUnitId("ca-app-pub-9480918527727973/5183286843");
//
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                        // Check the LogCat to get your test device ID
//                .addTestDevice(device_id)
//                .build();
//
//        mInterstitialAd.loadAd(adRequest);

        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }



    }


//    public void showAdd(){
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                //  Toast.makeText(getActivity(), "Ad is opened!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}
