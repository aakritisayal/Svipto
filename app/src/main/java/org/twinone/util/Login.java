package org.twinone.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.plus.Plus;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wewe.app.moboost.R;


/**
 * Created by Android on 3/30/2016.
 */
public class Login extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    RelativeLayout back;
    Button btn_login;
    TextView sign_up;
    private static final long CACHE_APP = Long.MAX_VALUE;
    private CachePackageDataObserver mClearCacheObserver;
    private GoogleApiClient client;
    String stremail, strpassword, responseLogin, social_type, social_id;
    EditText edtEmail, edtPassword;
    public static final int progress_id = 0;
    private ProgressDialog prgDialog;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    Button idFacebook;
    private static final int RC_SIGN_IN = 778;
    private CallbackManager callbackManager;
    private boolean mSignInClicked;
    private boolean mIntentInProgress;
  //  private GoogleApiClient mGoogleApiClient;
    LinearLayout forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sp = getSharedPreferences("Shared_value", MODE_MULTI_PROCESS);
        edt = sp.edit();

        idFacebook = (Button) findViewById(R.id.idFacebook);
        forgotPassword = (LinearLayout) findViewById(R.id.forgotPassword);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        back = (RelativeLayout) findViewById(R.id.back);
        btn_login = (Button) findViewById(R.id.btn_login);
        sign_up = (TextView) findViewById(R.id.sign_up);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, loginResultFacebookCallback);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .build();

        idFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "user_friends"));

            }
        });


        // clearCache();
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Login.this, Register.class);
                startActivity(it);
//                finish();

            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Login.this, Forgot_password.class);
                startActivity(it);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stremail = edtEmail.getText().toString();
                strpassword = edtPassword.getText().toString();

                if (stremail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide Email", Toast.LENGTH_SHORT).show();
                } else if (strpassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide Password", Toast.LENGTH_SHORT).show();
                } else {
                    LoginUser user = new LoginUser();
                    user.execute();
                }

//
//                Intent it = new Intent(Login.this, Main_navigation.class);
//                startActivity(it);
//                finish();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent it = new Intent(Login.this, Sign_up.class);
//                startActivity(it);
                finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setDataFromFacebookJsonObject(final JSONObject facebookJsonObject) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                social_type = "facebook";

                String id = null, name = null, email = null, profilePic = null;
                try {
                    if (facebookJsonObject.has("id")) {
                        social_id = facebookJsonObject.getString("id");
                    }
                    if (facebookJsonObject.has("email")) {
                        email = facebookJsonObject.getString("email");
                    }
                    if (facebookJsonObject.has("name")) {
                        name = facebookJsonObject.getString("name");
                    }
                    LoginSocial social = new LoginSocial();
                    social.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            AccessToken accessToken = loginResult.getAccessToken();
//                        String userId = accessToken.getUserId();

            //new GetFacebookDataAsync().execute(userId);
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            System.out.println("object = " + object);
                            setDataFromFacebookJsonObject(object);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // App code
            Toast.makeText(Login.this, "Some error occurred while register! Please try again.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            Toast.makeText(Login.this, "Some error occurred while register! Please try again.", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

//        for google plus login
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

//            if (!mGoogleApiClient.isConnected()) {
//                mGoogleApiClient.reconnect();
//            }
        }


//        for facebook login
        callbackManager.onActivityResult(requestCode, resultCode, intent);
    }


    void clearCache() {
        if (mClearCacheObserver == null) {
            mClearCacheObserver = new CachePackageDataObserver();
        }

        PackageManager mPM = getPackageManager();

        @SuppressWarnings("rawtypes")
        final Class[] classes = {Long.TYPE, IPackageDataObserver.class};

        Long localLong = Long.valueOf(CACHE_APP);

        try {

            Method localMethod = mPM.getClass().getMethod("freeStorageAndNotify", classes);
      /*
       * Start of inner try-catch block
       */
            try {
                localMethod.invoke(mPM, localLong, mClearCacheObserver);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
      /*
       * End of inner try-catch block
       */
        } catch (NoSuchMethodException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }//End of clearCache() method

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://master.clean.moboost/http/host/path")
//        );
     //   AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://master.clean.moboost/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class CachePackageDataObserver extends IPackageDataObserver.Stub {
        public void onRemoveCompleted(String packageName, boolean succeeded) {

        }//End o

    }

    public class LoginUser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... param) {

            String urlLink = "https://login.moboost.co/auth/login";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", stremail));
            params.add(new BasicNameValuePair("password", strpassword));

            Webservices services = new Webservices();
            responseLogin = services.passValues(urlLink, params);

            return responseLogin;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {

                    if (s.contains("failure")) {
                        JSONObject obj = new JSONObject(s);
                        String data = obj.getString("data");
                        Toast.makeText(Login.this, data, Toast.LENGTH_LONG).show();
                    } else if (s.contains("success")) {
                        JSONObject obj = new JSONObject(s);
                        JSONObject data = obj.getJSONObject("data");
                        String token = data.getString("token");
                        String profile_image = data.getString("profile_image");

                        edt.putString("token", token);
                        edt.putString("profile_image",profile_image);
                        edt.putString("stremail", stremail);
                        edt.commit();

                        Intent it = new Intent(Login.this, Me_activity.class);
                        finishAffinity();
                        startActivity(it);
                    } else {
                        Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

    public class LoginSocial extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... param) {

            String urlLink = "https://login.moboost.co/facebooklogin";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("social_id", social_id));
            params.add(new BasicNameValuePair("social_type", social_type));


            Webservices services = new Webservices();
            responseLogin = services.passValues(urlLink, params);

            return responseLogin;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {

                    if (s.contains("failure")) {
                        JSONObject obj = new JSONObject(s);
                        String data = obj.getString("data");
                        Toast.makeText(Login.this, data, Toast.LENGTH_LONG).show();
                    } else if (s.contains("success")) {
                        JSONObject obj = new JSONObject(s);
                        JSONObject data = obj.getJSONObject("data");
                        String token = data.getString("token");

                        edt.putString("token", token);
                        edt.commit();

                        Intent it = new Intent(Login.this, Me_activity.class);
                        finishAffinity();
                        startActivity(it);

                    } else {
                        Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

            dismissDialog(progress_id);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_id);
        }
    }


}