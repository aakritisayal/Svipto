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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wewe.app.moboost.R;


/**
 * Created by Android on 3/30/2016.
 */
public class Register extends Activity {

    RelativeLayout back;
    TextView login;
    Button btn_sign_up;
    String responseRegister, stremail, strpassword, strConfmPass;
    EditText edtReEntrPass, edtPassword, edtEmail;
    public static final int progress_id = 0;
    private ProgressDialog prgDialog;
    TextView txt_privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        back = (RelativeLayout) findViewById(R.id.back);
        login = (TextView) findViewById(R.id.login);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);
        txt_privacy =(TextView) findViewById(R.id.txt_privacy);


        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtReEntrPass = (EditText) findViewById(R.id.edtReEntrPass);

        txt_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Register.this,PrivatePolicyMobost.class);
                startActivity(it);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stremail = edtEmail.getText().toString();
                strpassword = edtPassword.getText().toString();
                strConfmPass = edtReEntrPass.getText().toString();


                if (stremail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide Email", Toast.LENGTH_SHORT).show();
                } else if (strpassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide Password", Toast.LENGTH_SHORT).show();
                } else if (strConfmPass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please confirm your Password", Toast.LENGTH_SHORT).show();
                } else if (!strConfmPass.equals(strpassword)) {
                    Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
                } else {

                    Registration register = new Registration();
                    register.execute();
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Register.this, Login.class);
                startActivity(it);
//                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent it = new Intent(Register.this, Sign_up.class);
//                startActivity(it);
                finish();
            }
        });
    }

    public class Registration extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... param) {

            String urlLink = "https://login.moboost.co/auth/register";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", stremail));
            params.add(new BasicNameValuePair("password", strpassword));

            Webservices services = new Webservices();
            responseRegister = services.passValues(urlLink, params);

            return responseRegister;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {

                    if (s.contains("failure")) {
                        JSONObject obj = new JSONObject(s);
                        String data = obj.getString("data");
                        Toast.makeText(Register.this, data, Toast.LENGTH_SHORT).show();
                    } else if (s.contains("success")) {
                        JSONObject obj = new JSONObject(s);
                        String data = obj.getString("data");
                        Toast.makeText(Register.this, "Sign up Sucessfully", Toast.LENGTH_SHORT).show();

                        Intent it = new Intent(Register.this, Sign_up.class);
                        startActivity(it);
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
