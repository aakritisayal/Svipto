package org.twinone.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.plus.Plus;
//import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wewe.app.moboost.R;


/**
 * Created by Android on 3/7/2016.
 */
public class Sign_up extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Button id_login, btn_login_email;
    TextView id_txtSign_up;
    private boolean mIntentInProgress;
   // private GoogleApiClient mGoogleApiClient;
    private boolean mSignInClicked;
    private static final int RC_SIGN_IN = 778;
    private CallbackManager callbackManager;
    Button idFacebook, id_google;
    String social_id, social_type, responseLogin;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    public static final int progress_id = 0;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        sp = getSharedPreferences("Shared_value", MODE_MULTI_PROCESS);
        edt = sp.edit();

        idFacebook = (Button) findViewById(R.id.idFacebook);
        id_google = (Button) findViewById(R.id.id_google);
        id_login = (Button) findViewById(R.id.id_login);
        btn_login_email = (Button) findViewById(R.id.btn_login_email);
        id_txtSign_up = (TextView) findViewById(R.id.id_txtSign_up);


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


                LoginManager.getInstance().logInWithReadPermissions(Sign_up.this, Arrays.asList("public_profile", "user_friends"));

                // loginfacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
            }
        });

//        id_google.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!mGoogleApiClient.isConnecting()) {
//                    mSignInClicked = true;
//                    mGoogleApiClient.connect();
//                }
//
//            }
//        });


        id_txtSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Sign_up.this, Register.class);
                startActivity(it);


            }
        });

        id_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Sign_up.this, Login.class);
                startActivity(it);

            }
        });
        btn_login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Sign_up.this, Register.class);
                startActivity(it);

            }
        });


    }

    @Override
    public void onConnected(Bundle bundle) {

//        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//
//            String personName = currentPerson.getDisplayName();
//            String id = currentPerson.getId();
//
//            Log.e("id", "" + id);
//
//
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    //    mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            if (mSignInClicked && result.hasResolution()) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                try {
                    result.startResolutionForResult(this, RC_SIGN_IN);
                    mIntentInProgress = true;
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    mIntentInProgress = false;
                   // mGoogleApiClient.connect();
                }
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

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
            Toast.makeText(Sign_up.this, "Some error occurred while register! Please try again.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            Toast.makeText(Sign_up.this, "Some error occurred while register! Please try again.", Toast.LENGTH_SHORT).show();
        }
    };


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
                        Toast.makeText(Sign_up.this, data, Toast.LENGTH_LONG).show();
                    } else if (s.contains("success")) {
                        JSONObject obj = new JSONObject(s);
                        JSONObject data = obj.getJSONObject("data");
                        String token = data.getString("token");

                        edt.putString("token", token);
                        edt.commit();

                        Intent it = new Intent(Sign_up.this, Me_activity.class);
                        finishAffinity();
                        startActivity(it);
                    } else {
                        Toast.makeText(Sign_up.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(Sign_up.this, "Something went wrong", Toast.LENGTH_LONG).show();
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
