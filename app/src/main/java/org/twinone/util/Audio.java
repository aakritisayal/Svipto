package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;

/**
 * Created by Android on 4/25/2016.
 */
public class Audio extends Activity {

    ListView listAudio;

    ArrayList<String> listpath = new ArrayList<String>();
    ArrayList<String> listSongNm = new ArrayList<String>();
    ArrayList<String> checkList = new ArrayList<String>();
    LinearLayout fragment_create_gallery_flipper;
    Button btnPhoneBoost;
    TextView files_count;
    RelativeLayout relativeLayout5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);

        fragment_create_gallery_flipper = (LinearLayout) findViewById(R.id.fragment_create_gallery_flipper);
        btnPhoneBoost = (Button) findViewById(R.id.btnPhoneBoost);
        files_count = (TextView) findViewById(R.id.files_count);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);

        btnPhoneBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkList.equals("")) {

                    deleteFiles(checkList);

                } else {
                    Toast.makeText(Audio.this, "Please select some audio file", Toast.LENGTH_SHORT).show();
                }

            }
        });

        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

        List<String> songs = new ArrayList<String>();
        while (cursor.moveToNext()) {

            String cursor0 = cursor.getString(0);
            String cursor1 = cursor.getString(1);
            String cursor2 = cursor.getString(2);
            listSongNm.add(cursor2);

            String cursor3 = cursor.getString(3);
            listpath.add(cursor3);

            String cursor4 = cursor.getString(4);
            String cursor5 = cursor.getString(5);


            songs.add(cursor.getString(0) + "||" + cursor.getString(1) + "||" + cursor.getString(2) + "||" + cursor.getString(3) + "||" + cursor.getString(4) + "||" + cursor.getString(5));
        }

        Log.e("songs", "" + songs);
        files_count.setText(listSongNm.size() + " Audio files");

        for (int i = 0; i < listSongNm.size(); i++) {

            String songNm = listSongNm.get(i);
            final String path = listpath.get(i);

            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.custom_list_music, null);

            TextView txtsongName = (TextView) v.findViewById(R.id.txtsongName);
            final CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            final LinearLayout playmusic = (LinearLayout) v.findViewById(R.id.playmusic);
            txtsongName.setText(songNm);
            playmusic.setId(i);
            checkBox.setId(i);

            playmusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int id = playmusic.getId();

                    String uri = listpath.get(id);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File file = new File(uri);
                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    startActivity(intent);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int id = checkBox.getId();


                    String path = listpath.get(id);
                    File f = new File(path);

                    if (isChecked) {
                        checkList.add(path);
                        // listFilesize.add(f);

                        if (checkList.size() == 0) {
                            btnPhoneBoost.setText("DELETE ");
                        } else {
                            btnPhoneBoost.setText("DELETE " + checkList.size());
                        }


                    }

                    if (!isChecked) {

                        for (int j = 0; j < checkList.size(); j++) {

                            if (path.contains(checkList.get(j))) {
                                checkList.remove(j);

                            }

                        }

                        if (checkList.size() == 0) {
                            btnPhoneBoost.setText("DELETE ");
                        } else {
                            btnPhoneBoost.setText("DELETE " + checkList.size());
                        }


                    }


                }
            });


            fragment_create_gallery_flipper.addView(v);

        }

    }


    public void deleteFiles(ArrayList<String> list) {


        for (int i = 0; i < list.size(); i++) {

            String strFiles = list.get(i);
            File f = new File(strFiles);

            if (f.exists()) {
                f.delete();
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
            }

        }

        Intent it = new Intent(Audio.this, Audio.class);
        startActivity(it);
        overridePendingTransition(0, 0);
        finish();


    }

}
