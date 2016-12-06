package org.twinone.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/11/2016.
 */
public class Photo_cleaner extends Activity {
    RelativeLayout back;
    GridView gridview;


    Button deletimages;

    TextView txtsize;

    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private ImageAdapter imageAdapter;

    ContentResolver resolver;
    ArrayList<String> imagesPath;

    long  sizeImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_cleaner);


        resolver = getContentResolver();

        gridview = (GridView) findViewById(R.id.gridview);
        deletimages = (Button) findViewById(R.id.deletimages);
        txtsize = (TextView) findViewById(R.id.txtsize);

        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        imagesPath = HomeActivity.getAllShownImagesPath(this);


        for(int i=0;i<imagesPath.size();i++){

            String filepath = imagesPath.get(i);

            File f = new File(filepath);
            if(f!=null || f.exists()){

                long size = Common_methods.getFileSize(f);
                sizeImages =size+sizeImages;


            }

        }

      String  sizeImgnew = Common_methods.getHumanReadableSize(sizeImages, Photo_cleaner.this);
        txtsize.setText(sizeImgnew);



        MediaScannerConnection.scanFile(Photo_cleaner.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            /*
             *   (non-Javadoc)
             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
             */
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });


        //imagesPath = Main_navigation.getAllShownImagesPath(this);


        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        this.count = imagecursor.getCount();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];
        this.thumbnailsselection = new boolean[this.count];
        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
            arrPath[i] = imagecursor.getString(dataColumnIndex);


        }
        //  GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        gridview.setAdapter(imageAdapter);
        imagecursor.close();

        //  final Button selectBtn = (Button) findViewById(R.id.selectBtn);
        deletimages.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                final int len = thumbnailsselection.length;
                int cnt = 0;
                String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (thumbnailsselection[i]) {
                        cnt++;
                        selectImages = selectImages + arrPath[i] + "|";

                    }
                }
                if (cnt == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Please select at least one image",
                            Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "You've selected Total " + cnt + " image(s).",
//                            Toast.LENGTH_LONG).show();

                    for (int i = 0; i < len; i++) {
                        if (thumbnailsselection[i]) {
                            cnt++;
                            File file = new File(arrPath[i]);

                            Common_methods.deleteFileFromMediaStore(resolver, file);

                           // file.delete();
                            Photo_cleaner.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            Uri.parse("file://" + Environment.getExternalStorageDirectory());


                            MediaScannerConnection.scanFile(Photo_cleaner.this, new String[]{file.getPath()}, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        @Override
                                        public void onScanCompleted(String path, Uri uri) {
                                            Log.i("TAG", "Scanned " + path);
                                        }
                                    });
                        }
                    }


                    MediaScannerConnection.scanFile(Photo_cleaner.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        /*
                         *   (non-Javadoc)
                         * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                         */
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });


                    ImageAdapter adapter = new ImageAdapter();
                    adapter.notifyDataSetChanged();
                    gridview.setAdapter(new ImageAdapter());

                    Intent it = new Intent(Photo_cleaner.this, Photo_cleaner.class);
                    startActivity(it);
                    overridePendingTransition(0, 0);
                    finish();


                    Log.e("SelectedImages", selectImages);
                }
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_gallery, null);
                holder.imgThumbnail = (ImageView) convertView.findViewById(R.id.imgThumbnail);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkBox.setId(position);
            holder.imgThumbnail.setId(position);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
//            holder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
//
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    int id = v.getId();
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
//                    startActivity(intent);
//                }
//            });
            holder.imgThumbnail.setImageBitmap(thumbnails[position]);
            holder.checkBox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imgThumbnail;
        CheckBox checkBox;
        int id;
    }


}
