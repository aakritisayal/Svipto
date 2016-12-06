package org.twinone.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;
/**
 * Created by Android on 6/4/2016.
 */
public class Forgot_password extends Activity {

    EditText edtEmail;
    Button btn_send;
    String stremail, response;
    public static final int progress_id = 0;
    private ProgressDialog prgDialog;
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btn_send = (Button) findViewById(R.id.btn_send);
        back = (RelativeLayout) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                stremail = edtEmail.getText().toString();

                if (stremail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide Email", Toast.LENGTH_SHORT).show();
                } else {
                   ReterievePassword password = new ReterievePassword();
                    password.execute();




                }
            }
        });
    }


    public class ReterievePassword extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... param) {

            String urlLink = "https://login.moboost.co/forgetpassword";
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("email", stremail));
//
//
//            Webservices services = new Webservices();
//            response = services.passValues(urlLink, params);


            DefaultHttpClient client = new DefaultHttpClient();
            ResponseHandler<String> handler = new BasicResponseHandler();
            HttpPost post = new HttpPost(urlLink);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("email", stremail));

            try {
                post.setEntity(new UrlEncodedFormEntity(list));



                HttpResponse httpresponse = client.execute(post);

                HttpEntity resEntity = httpresponse.getEntity();
               String  responseEt = EntityUtils.toString(resEntity);


                response = client.execute(post, handler);

//                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent(), "UTF-8"));
//
//                    responseEdit = reader.readLine();

                String res = httpresponse.getStatusLine().toString();


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {


                    if(s.contains("success")){
                        JSONObject obj = new JSONObject(s);

                        String data = obj.getString("message");
                        if (data.equals("Email is not exist in any domain!")) {
                            Toast.makeText(Forgot_password.this, "Email not exist", Toast.LENGTH_SHORT).show();
                        } else if (data.equals("success")) {
                            Toast.makeText(Forgot_password.this, "Email sent to your registerd Email Id", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                        else {
                            Toast.makeText(Forgot_password.this, s, Toast.LENGTH_LONG).show();
                        }
                    }

                    else {
                        Toast.makeText(Forgot_password.this, s, Toast.LENGTH_LONG).show();

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(Forgot_password.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
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


}
