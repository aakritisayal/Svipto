package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import wewe.app.moboost.R;

import static android.os.Environment.getExternalStorageState;

/**
 * Created by Android on 4/26/2016.
 */
public class Document extends Activity {

    //  private File file;
    //   private List<String> myList = new ArrayList<String>();
    ListView listView;
    ArrayList<File> list;
    ArrayList<String> listfileFolder = new ArrayList<>();
    ArrayList<String> listfiledDirectory = new ArrayList<>();
    ArrayList<File> listfile = new ArrayList<>();
    ArrayList<File> listfilepath = new ArrayList<>();

    ArrayList<String> listAddfile = new ArrayList<String>();

    LinearLayout listFiles;
    Button btnPhoneBoost;
    RelativeLayout relativeLayout5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);

        listFiles = (LinearLayout) findViewById(R.id.listFiles);
        btnPhoneBoost = (Button) findViewById(R.id.btnPhoneBoost);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.relativeLayout5);

        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String state = getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  // we can read the External Storage...
            getAllFilesOfDir(Environment.getExternalStorageDirectory());
        }


        Log.e("listfileFolder", "" + listfileFolder);
        Log.e("listfiledDirectory", "" + listfiledDirectory);

        Log.e("list", "" + listfile);
        Log.e("listfiledDirectory", "" + listfilepath);


        btnPhoneBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!listAddfile.equals("")) {

                    deleteFiles(listAddfile);

                } else {
                    Toast.makeText(Document.this, "Please select some audio file", Toast.LENGTH_SHORT).show();
                }

            }
        });

        for (int i = 0; i < listfileFolder.size(); i++) {

            String fileNm = listfileFolder.get(i);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.custom_list_music, null);

            TextView txtsongName = (TextView) v.findViewById(R.id.txtsongName);
            final CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            final LinearLayout playmusic = (LinearLayout) v.findViewById(R.id.playmusic);
            txtsongName.setText(fileNm);
            playmusic.setId(i);
            checkBox.setId(i);


            playmusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int id = playmusic.getId();

                    String uri = listfiledDirectory.get(id);
                    File file1 = new File(uri);
                    Uri urii = Uri.parse("file://" + file1.getAbsolutePath());

                    String doc = "application/msword";
                    String pdf = "application/pdf";

                    if (uri.contains(".pdf")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file = new File(uri);
                        intent.setDataAndType(Uri.fromFile(file), pdf);
                        startActivity(intent);
                    } else if (uri.contains("doc")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        File file = new File(uri);
                        intent.setDataAndType(Uri.fromFile(file), doc);
                        startActivity(intent);
                    } else if (uri.contains(".txt")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(urii, "text/plain");
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(urii);
                        startActivity(intent);
                    }

                }
            });


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int id = checkBox.getId();

                    String path = listfiledDirectory.get(id);
                    File f = new File(path);

                    if (isChecked) {
                        listAddfile.add(path);
                        // listFilesize.add(f);

                        if (listAddfile.size() == 0) {
                            btnPhoneBoost.setText("DELETE ");
                        } else {
                            btnPhoneBoost.setText("DELETE " + listAddfile.size());
                        }


                    }

                    if (!isChecked) {

                        for (int j = 0; j < listAddfile.size(); j++) {

                            if (path.contains(listAddfile.get(j))) {
                                listAddfile.remove(j);

                            }

                        }

                        if (listAddfile.size() == 0) {
                            btnPhoneBoost.setText("DELETE ");
                        } else {
                            btnPhoneBoost.setText("DELETE " + listAddfile.size());
                        }


                    }


                }
            });


            listFiles.addView(v);
        }


    }

    public void walkdir(File dir) {
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        //Do what ever u want

                    }
                }
            }
        }
    }


    private void getAllFilesOfDir(File directory) {

        final File[] files = directory.listFiles();

        String pdfPattern = ".pdf";
        String txt = ".txt";
        String xlsx = ".xlsx";
        String doc = ".doc";
        String ppt = ".ppt";
       // String cache ="cache";
        // list = new ArrayList<File>(Arrays.asList(files));

        if (files != null) {
            for (File file : files) {
                if (file != null) {
                    if (file.isDirectory()) {  // it is a folder...
                        getAllFilesOfDir(file);

                    } else {  // it is a file...
                        Log.d("tag", "File: " + file.getAbsolutePath() + "\n");
                        //  listfiledDirectory.add(file.getAbsolutePath());

                    }
                    if (file.getName().endsWith(pdfPattern) || file.getName().endsWith(txt) || file.getName().endsWith(xlsx) || file.getName().endsWith(doc) || file.getName().endsWith(doc)) {

                        listfileFolder.add(file.getName());
                        listfiledDirectory.add(file.getAbsolutePath());
                        listfilepath.add(file.getAbsoluteFile());
                        listfile.add(file);

                    }
                }
            }
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

        Intent it = new Intent(Document.this, Document.class);
        startActivity(it);
        overridePendingTransition(0, 0);
        finish();

    }


}
