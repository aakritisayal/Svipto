package org.twinone.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 7/11/2016.
 */
public class Personal_information extends Activity {

    TextView email;
    ImageView image;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    String token, stremail, profile_image, responseUpload, realPath;
    public static final int progress_id = 0;
    private ProgressDialog prgDialog;
    RelativeLayout back;
    public static final int REQUEST_CAMERA = 0x1;
    public static final int SELECT_FILE = 0x2;
    Bitmap bitMap;
    String url="https://login.moboost.co/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_information);

        sp = getSharedPreferences("Shared_value", MODE_MULTI_PROCESS);
        edt = sp.edit();

        token = sp.getString("token", "");
        stremail = sp.getString("stremail", "");
        profile_image = sp.getString("profile_image", "");


        email = (TextView) findViewById(R.id.email);
        image = (ImageView) findViewById(R.id.image);
        back = (RelativeLayout) findViewById(R.id.back);

        profile_image =url+profile_image;
        profile_image =profile_image.replaceAll(" ","%20");

        UrlImageViewHelper.setUrlDrawable(image, profile_image, R.drawable.sign_activity);


        email.setText(stremail);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


    public class Change_image extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... param) {

            String urlLink = "https://login.moboost.co/uploadProfilePic";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(urlLink);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", stremail));
                org.apache.http.entity.mime.MultipartEntity entity = new org.apache.http.entity.mime.MultipartEntity();
                entity.addPart("profile_image", new FileBody(new File(realPath)));
                entity.addPart("email", new StringBody(stremail));

                post.setEntity(entity);

                HttpResponse httpresponse = client.execute(post);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent(), "UTF-8"));

                responseUpload = reader.readLine();


            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return responseUpload;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {

                try {
                    JSONObject obj = new JSONObject(s);
                    String message =obj.getString("message");
                    if(message.equals("Successfully upload!")){

                        String image_name =obj.getString("image_name");
                        Intent intent = new Intent(Personal_information.this,Personal_information.class);
                        edt.putString("profile_image",image_name);
                        edt.commit();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent, 0);
                        overridePendingTransition(0, 0);
                        finish();

                        Toast.makeText(Personal_information.this, "Image uploaded Successfully !", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        Toast.makeText(Personal_information.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(Personal_information.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            }

            dismissDialog(progress_id);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_id);
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_id:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Loading..!");
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;

            default:
                return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                bitMap = (Bitmap) data.getExtras().get("data");
                Uri uri = getImageUri(getApplicationContext(), bitMap);
                realPath = getRealPathFromURI(getApplicationContext(), uri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitMap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            //    image.setImageBitmap(bitMap);


                Change_image image = new Change_image();
                image.execute();

                //  idlogo.setBackgroundDrawable(dr);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                realPath = getRealPathFromURI(getApplicationContext(), selectedImageUri);
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bitMap = BitmapFactory.decodeFile(selectedImagePath, options);

            //    image.setImageBitmap(bitMap);

                Change_image image = new Change_image();
                image.execute();

            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void onPickImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Images");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

}
