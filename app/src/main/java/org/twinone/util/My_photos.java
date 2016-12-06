package org.twinone.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import wewe.app.moboost.R;

/**
 * Created by Android on 4/14/2016.
 */
public class My_photos extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    Button btnPhoneBoost;
    private ArrayList<GalleryPhotoAlbum> arrayListAlbums;
    private GalleryAlbumAdapter galleryAlbumAdapter;
    private ViewFlipper viewFlipperGallery;
    private Cursor mPhotoCursor = null;
    private Cursor mVideoCursor = null;
    private boolean isImage = true;
    private List<MediaObject> cursorData;
    private ListView lvPhotoAlbum;
    private ImageAdapter mAdapter;
    private GridView mGridView = null;
    RelativeLayout relativeUp;
    RelativeLayout relativeLayout5, relativeUpvIDEOS;
    String videos;
    TextView txt_head;
    ArrayList<String> checkList = new ArrayList<String>();
    ArrayList<File> listFilesize = new ArrayList<File>();

    ContentResolver resolver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_photos);

        arrayListAlbums = new ArrayList<GalleryPhotoAlbum>();

        resolver = getContentResolver();

        viewFlipperGallery = (ViewFlipper) findViewById(R.id.fragment_create_gallery_flipper);
        lvPhotoAlbum = (ListView) findViewById(R.id.fragment_create_gallery_listview);
        mGridView = (GridView) findViewById(R.id.gridview);
        relativeUp = (RelativeLayout) findViewById(R.id.relativeUp);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);
        txt_head = (TextView) findViewById(R.id.txt_head);
        relativeUpvIDEOS = (RelativeLayout) findViewById(R.id.relativeUpvIDEOS);
        btnPhoneBoost = (Button) findViewById(R.id.btnPhoneBoost);


        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        arrayListAlbums = new ArrayList<GalleryPhotoAlbum>();

        lvPhotoAlbum.setOnItemClickListener(this);
        relativeUp.setOnClickListener(this);
        relativeUpvIDEOS.setOnClickListener(this);
        mGridView.setOnItemClickListener(null);

        // getPhotoList();

        Intent it = getIntent();
        videos = it.getStringExtra("videos");

        if (videos != null) {
            getVideoList();
            viewFlipperGallery.setDisplayedChild(0);
            txt_head.setText("MY VIDEOS");
            relativeUpvIDEOS.setVisibility(View.VISIBLE);
            relativeUp.setVisibility(View.GONE);
            isImage = false;


        } else {
            getPhotoList();
            viewFlipperGallery.setDisplayedChild(0);
            txt_head.setText("MY PHOTOS");
            relativeUpvIDEOS.setVisibility(View.GONE);
            relativeUp.setVisibility(View.VISIBLE);
            isImage = true;

        }


        btnPhoneBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkList.equals("")) {


                    deleteImages(listFilesize);


                } else {
                    Toast.makeText(My_photos.this, "Please select image", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        if (viewFlipperGallery.getDisplayedChild() == 0 && isImage) {
            initPhotoImages(arrayListAlbums.get(arg2).getBucketName());
        } else {
            initVideoImages(arrayListAlbums.get(arg2).getBucketName());
        }

        viewFlipperGallery.showNext();
    }

    Set<ProcessGalleryFile> tasks = new HashSet<ProcessGalleryFile>();


    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {

            if (!cursorData.isEmpty()) {
                Log.v("cursorData.size", String.valueOf(cursorData.size()));
                mAdapter = new ImageAdapter(My_photos.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.setData(cursorData);

                        mGridView.setAdapter(mAdapter);


                        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                MediaObject cursor = cursorData.get(i);

                                String path = cursor.getPath();

                                Intent it = new Intent(My_photos.this, Display_image.class);
                                it.putExtra("path", path);
                                startActivity(it);

                            }
                        });
                    }
                });
            }
        }

    };

    private void initPhotoImages(String bucketName) {
        try {

            btnPhoneBoost.setVisibility(View.VISIBLE);

            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";

            // final String[] columns = { MediaStore.Images.Media.DATA,
            // MediaStore.Images.Media._ID };
            mPhotoCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    searchParams, null, orderBy + " DESC");

            if (mPhotoCursor.getCount() > 0) {

                cursorData = new ArrayList<MediaObject>();

                cursorData.addAll(Utils.extractMediaList(mPhotoCursor,
                        MediaType.PHOTO));

                new Thread(runnable1).start();

            }

            // setAdapter(mImageCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVideoImages(String bucketName) {
        try {

            btnPhoneBoost.setVisibility(View.VISIBLE);

            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";


            mVideoCursor = getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                    searchParams, null, orderBy + " DESC");

            if (mVideoCursor.getCount() > 0) {


                cursorData = new ArrayList<MediaObject>();


                cursorData.addAll(Utils.extractMediaList(mVideoCursor,
                        MediaType.VIDEO));

                new Thread(runnable1).start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPhotoList() {

        {

            btnPhoneBoost.setVisibility(View.GONE);

            // which image properties are we querying
            String[] PROJECTION_BUCKET = {MediaStore.Images.ImageColumns.BUCKET_ID,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATA};

            String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
            String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

            // Get the base URI for the People table in the Contacts content
            // provider.
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            Cursor cur = getContentResolver().query(images, PROJECTION_BUCKET,
                    BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

            Log.v("ListingImages", " query count=" + cur.getCount());

            GalleryPhotoAlbum album;

            if (cur.moveToFirst()) {
                String bucket;
                String date;
                String data;
                long bucketId;

                int bucketColumn = cur
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                int dateColumn = cur
                        .getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);

                int bucketIdColumn = cur
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

                do {
                    // Get the field values
                    bucket = cur.getString(bucketColumn);
                    date = cur.getString(dateColumn);
                    data = cur.getString(dataColumn);
                    bucketId = cur.getInt(bucketIdColumn);

                    if (bucket != null && bucket.length() > 0) {

                        album = new GalleryPhotoAlbum();

                        album.setBucketId(bucketId);
                        album.setBucketName(bucket);
                        album.setDateTaken(date);
                        album.setData(data);
                        album.setTotalCount(photoCountByAlbum(bucket));
                        arrayListAlbums.add(album);
                        // Do something with the values.
                        Log.v("ListingImages", " bucket=" + bucket
                                + "  date_taken=" + date + "  _data=" + data
                                + " bucket_id=" + bucketId);
                    }

                } while (cur.moveToNext());
            }
            cur.close();
            setData();

        }


    }

    private int photoCountByAlbum(String bucketName) {
        try {


            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";

            // final String[] columns = { MediaStore.Images.Media.DATA,
            // MediaStore.Images.Media._ID };
            Cursor mPhotoCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    searchParams, null, orderBy + " DESC");

            if (mPhotoCursor.getCount() > 0) {
                return mPhotoCursor.getCount();
            }
            mPhotoCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;

    }


    private void setData() {
        if (arrayListAlbums.size() > 0) {
            if (galleryAlbumAdapter == null) {
                galleryAlbumAdapter = new GalleryAlbumAdapter(My_photos.this);
            } else {
                galleryAlbumAdapter.notifyDataSetChanged();
            }
            // tvNoItem.setVisibility(View.GONE);
            galleryAlbumAdapter.setData(arrayListAlbums);
            lvPhotoAlbum.setAdapter(galleryAlbumAdapter);
        }
    }


    @Override
    public void onClick(View v) {


        arrayListAlbums.clear();

        switch (v.getId()) {

            case R.id.relativeUpvIDEOS:

                getVideoList();
                viewFlipperGallery.setDisplayedChild(0);
                isImage = false;


                break;
            case R.id.relativeUp:

                getPhotoList();
                viewFlipperGallery.setDisplayedChild(0);
                isImage = true;

                break;

            default:
                break;
        }

    }


    private void getVideoList() {
        btnPhoneBoost.setVisibility(View.GONE);
        // which image properties are we querying
        String[] PROJECTION_BUCKET = {MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME, MediaStore.Video.VideoColumns.DATE_TAKEN,
                MediaStore.Video.VideoColumns.DATA};

        String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        // Get the base URI for the People table in the Contacts content
        // provider.
        Uri images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = getContentResolver().query(images, PROJECTION_BUCKET,
                BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

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
                bucketId = cur.getInt(bucketIdColumn);

                if (bucket != null && bucket.length() > 0) {
                    album = new GalleryPhotoAlbum();



                    album.setBucketId(bucketId);
                    album.setBucketName(bucket);
                    album.setDateTaken(date);
                    album.setData(data);
                    album.setTotalCount(videoCountByAlbum(bucket));
                    arrayListAlbums.add(album);
                    // Do something with the values.
                    Log.v("ListingImages", " bucket=" + bucket
                            + "  date_taken=" + date + "  _data=" + data
                            + " bucket_id=" + bucketId);
                }

            } while (cur.moveToNext());
        }
        cur.close();


        setData();

    }

    private int videoCountByAlbum(String bucketName) {

        try {
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";

            // final String[] columns = { MediaStore.Video.Media.DATA,
            // MediaStore.Video.Media._ID };
            Cursor mVideoCursor = getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                    searchParams, null, orderBy + " DESC");

            if (mVideoCursor.getCount() > 0) {

                return mVideoCursor.getCount();
            }
            mVideoCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;

    }


    public class ImageAdapter extends CommonAdapter<MediaObject> {
        private final String TAG = ImageAdapter.class.getSimpleName();
        private Context mContext;
        private Set<ProcessGalleryFile> tasks;
        public long length;

        public ImageAdapter(Context c) {
            mContext = c;
            isFirstTime = true;
            tasks = new HashSet<ProcessGalleryFile>();
        }

        private boolean isFirstTime;

        public void setFirstTime(boolean firstTime) {
            this.isFirstTime = firstTime;
        }


        public class ViewHolder {
            public ImageView imgThumb;
            public TextView videoDuration;
            public MediaObject object;
            public int position;
            public CheckBox checkBox;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder = new ViewHolder();
                holder.position = position;
                convertView = vi.inflate(R.layout.list_item_gallery, null);
                holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumbnail);
                holder.videoDuration = (TextView) convertView.findViewById(R.id.txtDuration);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //   holder.imgThumb.setImageResource(R.drawable.icon);
            holder.checkBox.setId(position);
            holder.object = getItem(position);
            convertView.setTag(holder);


            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int id = holder.checkBox.getId();

                    MediaObject cursor = cursorData.get(id);

                    String path = cursor.getPath();
                    File f = new File(path);

                    if (isChecked) {
                        checkList.add(path);
                        listFilesize.add(f);
                    }

                    if (!isChecked) {

                        for (int j = 0; j < checkList.size(); j++) {

                            if (path.contains(checkList.get(j))) {

                                checkList.remove(j);
                                listFilesize.remove(j);

                            }

                        }
                    }


                }
            });


            String filePath = holder.object.getPath();


            if (isFirstTime) {

                ProcessGalleryFile processGalleryFile = new ProcessGalleryFile(holder.imgThumb, holder.videoDuration, holder.object.getPath(), holder.object.getMediaType(), mContext);
                if (tasks == null) {
                    tasks = new HashSet<ProcessGalleryFile>();
                }
                if (!tasks.contains(processGalleryFile)) {
                    try {
                        processGalleryFile.execute();
                        tasks.add(processGalleryFile);
                    } catch (Exception ignored) {
                    }
                }
            } else {
                try {
                    cancelAll();
                } catch (Exception ignored) {
                }
                holder.videoDuration.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        @Override
        public void setData(List<MediaObject> values) {
            if (values != null) {
                Collections.sort(values);
            }
            super.setData(values);
        }

        public void cancelAll() throws Exception {
            final Iterator<ProcessGalleryFile> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                iterator.next().cancel(true);
                iterator.remove();
            }
        }
    }

    public void deleteImages(ArrayList<File> list) {

        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);

            Common_methods.deleteFileFromMediaStore(resolver, file);


          //  file.delete();
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            MediaScannerConnection.scanFile(My_photos.this, new String[]{file.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("TAG", "Scanned " + path);
                        }
                    });


        }

        arrayListAlbums.clear();

        MediaScannerConnection.scanFile(My_photos.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            /*
             *   (non-Javadoc)
             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
             */
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });


        if(videos!=null){

            viewFlipperGallery.setDisplayedChild(0);
            isImage = false;
            getVideoList();
            Intent it = new Intent(My_photos.this, My_photos.class);
            it.putExtra("videos","videos");
            startActivity(it);
            finish();
        }

        else{

            Intent it = new Intent(My_photos.this, My_photos.class);
            it.putExtra("photos","photos");
            startActivity(it);
            finish();
        }




//        arrayListAlbums.clear();
//
//
//        if (videos != null) {
//
//            getVideoList();
//            viewFlipperGallery.setDisplayedChild(0);
//            isImage = false;
//
//            View v ;
//
//
//          //  callOnclick();
//
//            Intent it = new Intent(My_photos.this, My_photos.class);
//            it.putExtra("videos", "videos");
//            startActivity(it);
//            overridePendingTransition(0, 0);
//            finish();
//
//        } else {
//
//            getPhotoList();
//            viewFlipperGallery.setDisplayedChild(0);
//            isImage = true;
//
//           // callOnclick();
//
//            Intent it = new Intent(My_photos.this, My_photos.class);
//            startActivity(it);
//            overridePendingTransition(0, 0);
//            finish();
//        }


    }


    public static boolean deleteDirectory(File path) {
        // TODO Auto-generated method stub
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }


    @Override
    protected void onResume() {
        super.onResume();

        //  arrayListAlbums.clear();

//        if (videos != null) {
//
//            getVideoList();
//            viewFlipperGallery.setDisplayedChild(0);
//            isImage = false;
//
////            Intent it = new Intent(My_photos.this, My_photos.class);
////            it.putExtra("videos", "videos");
////            startActivity(it);
////            overridePendingTransition(0, 0);
////            finish();
//
//        } else {
//
//            getPhotoList();
//            viewFlipperGallery.setDisplayedChild(0);
//            isImage = true;
//
////            Intent it = new Intent(My_photos.this, My_photos.class);
////            startActivity(it);
////            overridePendingTransition(0, 0);
////            finish();
//        }


    }

    public void callOnclick() {
        if (videos != null) {

            getVideoList();
            viewFlipperGallery.setDisplayedChild(0);
            isImage = false;

            Intent it = new Intent(My_photos.this, My_photos.class);
            it.putExtra("videos", "videos");
            startActivity(it);
            overridePendingTransition(0, 0);
            finish();

        } else {

            getPhotoList();
            viewFlipperGallery.setDisplayedChild(0);
            isImage = true;

            Intent it = new Intent(My_photos.this, My_photos.class);
            startActivity(it);
            overridePendingTransition(0, 0);
            finish();
        }

    }
}



