package org.twinone.util;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import wewe.app.moboost.R;

//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.imgproc.Imgproc;

/**
 * Created by Android on 6/27/2016.
 */
public class Duplicate_photos extends Activity {
    RelativeLayout back_manager;
    ArrayList<String> listImages;

    ArrayList<String> listblurImgPath = new ArrayList<>();
    ArrayList<Bitmap> listBitmap = new ArrayList<>();
    ArrayList<Long>  listSize = new ArrayList<>();

    ArrayList<String> listblurImgPathnew = new ArrayList<>();
    ArrayList<Bitmap> listBitmapnew = new ArrayList<>();
    ArrayList<Long>  listSizenew = new ArrayList<>();

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA =12 ;
     long  sizeImages;
    TextView txtsize,txtTotalimages;
    private static final long MEGA_BYTE = 1024;
    GridView gridview;
    LinearLayout linearGif,display_photos;
    ProgressDialog pd;
    Delete_duplicate duplicate;
    boolean taskDelete;
   Button btnCoolDown;
    String sizeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duplicate_photos);

        sizeImages =0;
        showProgress("Fetching Blurry Photos");

         pd.show();

        gridview =(GridView) findViewById(R.id.gridview);
        linearGif =(LinearLayout) findViewById(R.id.linearGif);
        display_photos =(LinearLayout) findViewById(R.id.display_photos);
        btnCoolDown =(Button) findViewById(R.id.btnCoolDown);

        txtsize =(TextView) findViewById(R.id.txtsize);
        txtTotalimages =(TextView) findViewById(R.id.txtTotalimages);
        back_manager =(RelativeLayout) findViewById(R.id.back_manager);
        back_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        listImages = HomeActivity.getAllShownImagesPath(this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {



            } else {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);


            }
        }



    }

//    public void onResume()
//    {
//        super.onResume();
//        if (!OpenCVLoader.initDebug()) {
//            Log.e("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
//        } else {
//            Log.e("OpenCV", "OpenCV library found inside package. Using it!");
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//        }
//    }


//    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//                case LoaderCallbackInterface.SUCCESS: {
//
//                     duplicate = new Delete_duplicate();
//                     duplicate.execute();
//
//
//
//                } break;
//                default:
//                {
//                    super.onManagerConnected(status);
//                } break;
//            }
//        }
//    };



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
  public class Duplicate_Adapter extends BaseAdapter {

      ArrayList<Bitmap> bitmapImages = new ArrayList<Bitmap>();
      ArrayList<Long> imagesSize = new ArrayList<Long>();
      ArrayList<String> blurImgPath = new ArrayList<>();
      private LayoutInflater layoutInflater;
      Context mContext;

    public Duplicate_Adapter(Context ct,ArrayList<String> listblurImgPath, ArrayList<Bitmap> bitmap, ArrayList<Long> size ) {

        layoutInflater = LayoutInflater.from(ct);
        bitmapImages = bitmap;
        imagesSize = size;
        blurImgPath =listblurImgPath;
        mContext =ct;

    }


    @Override
    public int getCount() {
        return bitmapImages.size();
    }

    @Override
    public Object getItem(int position) {
        return bitmapImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
            holder.position = position;
            convertView = vi.inflate(R.layout.list_item_gallery, null);
            holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumbnail);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.checkBox.setId(position);
        holder.imgThumb.setImageBitmap(bitmapImages.get(position));


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int id = holder.checkBox.getId();

                 Bitmap  bitmapImg = bitmapImages.get(id);
                 long imgSize = imagesSize.get(id);
                 String imgPath = blurImgPath.get(id);

                if (isChecked) {

                    listBitmapnew.add(bitmapImg);
                    listSizenew.add(imgSize);
                    listblurImgPathnew.add(imgPath);

                }

                if (!isChecked) {


                    for (int i = 0; i < listblurImgPathnew.size(); i++) {
                        if (imgPath.contains(listblurImgPathnew.get(i))) {

                            listBitmapnew.remove(i);
                            listSizenew.remove(i);
                            listblurImgPathnew.remove(i);

                            break;
                        }
                    }


                }


            }
        });

        convertView.setTag(holder);

        return convertView;
}
      public class ViewHolder {
          public ImageView imgThumb;
          public MediaObject object;
          public int position;
          public CheckBox checkBox;
      }

}
    private void showProgress(String message) {
        pd = new ProgressDialog(this);
        pd.setIcon(R.drawable.icon);
        pd.setTitle("Please Wait...");
        pd.setMessage(message);
        pd.setCancelable(true);
        pd.show();

    }


    public class Delete_duplicate extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            taskDelete =true;


              //  getBlurryImages();


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            if(listBitmapnew.size()!=0){

                listblurImgPath= listblurImgPathnew;
                listBitmap=listBitmapnew;
                listSize =listSizenew;

            }


            if(sizeImg!=null){
                txtsize.setText(sizeImg);
            }
             else{
                txtsize.setText("0.00");
                btnCoolDown.setVisibility(View.GONE);
            }

            linearGif.setVisibility(View.GONE);
            display_photos.setVisibility(View.VISIBLE);
            txtTotalimages.setText(String.valueOf(listblurImgPath.size()) + " photos to be proceed");
            gridview.setAdapter(new Duplicate_Adapter(getApplicationContext(), listblurImgPath, listBitmap, listSize));

            btnCoolDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    long sizeImagesnew =0;
                    String sizeImgnew =null;
                    for (int i = 0; i < listblurImgPathnew.size(); i++) {


                        String imgPath = listblurImgPathnew.get(i);
                        Bitmap bitmapImg = listBitmapnew.get(i);
                        long imgSize = listSizenew.get(i);

                        if (listblurImgPath.contains(imgPath)) {
                            listblurImgPath.remove(imgPath);
                        }
                        if (listBitmap.contains(bitmapImg)) {
                            listBitmap.remove(bitmapImg);
                        }

                        if (listSize.contains(imgSize)) {
                            listSize.remove(imgSize);
                        }


                        File file = new File(imgPath);
                        file.delete();
                        Duplicate_photos.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                    }

                    for (int i = 0; i < listSize.size(); i++) {

                        long size = listSize.get(i);
                        sizeImagesnew = size + sizeImagesnew;

                        sizeImgnew = Common_methods.getHumanReadableSize(sizeImagesnew, Duplicate_photos.this);

                    }

                    txtsize.setText(sizeImgnew);
                    txtTotalimages.setText(String.valueOf(listSize.size()) + " photos to be proceed");
                    Duplicate_Adapter adapter = new Duplicate_Adapter(Duplicate_photos.this, listblurImgPath, listBitmap, listSize);
                    adapter.notifyDataSetChanged();
                    gridview.setAdapter(new Duplicate_Adapter(getApplicationContext(), listblurImgPath, listBitmap, listSize));


                }
            });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(taskDelete){
            duplicate.cancel(true);
        }
        finish();



    }

//    public void getBlurryImages(){
//        for (int i = 0; i < listImages.size(); i++){
//            Log.e("OpenCV", "OpenCV loaded successfully");
//            String picFilePath = listImages.get(i);
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inDither = true;
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//            Bitmap image = Common_methods.decodeSampledBitmapFromFile(picFilePath, 500, 500);
//
//            image = Bitmap.createScaledBitmap(image, 150, 150, true);
//          //  resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
//
//
//            int l = CvType.CV_8UC1; //8-bit grey scale image
//            Mat matImage = new Mat();
//            org.opencv.android.Utils.bitmapToMat(image, matImage);
//            Mat matImageGrey = new Mat();
//            Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);
//
//            Bitmap destImage;
//            destImage = Bitmap.createBitmap(image);
//            Mat dst2 = new Mat();
//            org.opencv.android.Utils.bitmapToMat(destImage, dst2);
//            Mat laplacianImage = new Mat();
//            dst2.convertTo(laplacianImage, l);
//            Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);
//            Mat laplacianImage8bit = new Mat();
//            laplacianImage.convertTo(laplacianImage8bit, l);
//
//            Bitmap bmp = Bitmap.createBitmap(laplacianImage8bit.cols(), laplacianImage8bit.rows(), Bitmap.Config.ARGB_8888);
//            org.opencv.android.Utils.matToBitmap(laplacianImage8bit, bmp);
//            int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
//            bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight()); // bmp为轮廓图
//
//            int maxLap = -16777216; // 16m
//            for (int pixel : pixels) {
//                if (pixel > maxLap)
//                    maxLap = pixel;
//            }
//
//            int soglia = -6118750;
//            if (maxLap <= soglia) {
//                Log.e("is blur image", "imahe is blur....");
//            }
//            soglia += 6118750;
//            maxLap += 6118750;
//
//            boolean opencvEnd = true;
//            boolean isBlur = maxLap <= soglia;
//
//
//            if(isBlur){
//
//                listblurImgPath.add(picFilePath);
//
//                File imgFile = new File(picFilePath);
//
//               // long size = imgFile.getTotalSpace();
//
//
//
//
//                if(imgFile.exists()){
//
//                    File imagefile = new File(picFilePath);
//                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                    Bitmap bitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath(),bmOptions);
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] imageInByte = stream.toByteArray();
//                    long lengthbmp = imageInByte.length;
//
//                    listSize.add(lengthbmp);
//
//                    sizeImages = lengthbmp + sizeImages;
//
//
//                    sizeImg = Common_methods.getHumanReadableSize(sizeImages, Duplicate_photos.this);
//
//                    Log.e("size_image","" +sizeImg);
//
//
//                    final int THUMBSIZE = 64;
//
//
//                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picFilePath), THUMBSIZE, THUMBSIZE);
//                    listBitmap.add(ThumbImage);
//
//
//                }
//
//
//            }
//
//        }
//
//    }

}
