package org.twinone.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.w3c.dom.Text;

import wewe.app.moboost.R;

/**
 * Created by Android on 3/29/2016.
 */
public class Me_activity extends Fragment {

    LinearLayout id_cm_family, id_update, id_settings;
    TextView name, sign_up;
    SharedPreferences sp;
    SharedPreferences.Editor edt;
    String token,stremail,profile_image;
    ImageView img_personal_details;
    String url="https://login.moboost.co/images/";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {

        View view = inflate.inflate(R.layout.me_activity,null);


        sp = getActivity().getSharedPreferences("Shared_value", getActivity().MODE_MULTI_PROCESS);
        edt = sp.edit();

        token = sp.getString("token", "");
        stremail =sp.getString("stremail", "");
        profile_image =sp.getString("profile_image", "");



        id_cm_family = (LinearLayout) view.findViewById(R.id.id_cm_family);
        id_update = (LinearLayout) view.findViewById(R.id.id_update);
        id_settings = (LinearLayout) view.findViewById(R.id.id_settings);
        img_personal_details =(ImageView) view.findViewById(R.id.img_personal_details);



        //   UrlImageViewHelper.setUrlDrawable(img_personal_details,profile_image,R.drawable.sign_activity);

        name = (TextView) view.findViewById(R.id.name);
        sign_up = (TextView) view.findViewById(R.id.sign_up);


        if (!token.equals("")) {

            name.setText(stremail);
            name.setVisibility(View.VISIBLE);
            sign_up.setVisibility(View.GONE);
            img_personal_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent it = new Intent(getActivity(),Personal_information.class);
                    startActivity(it);
                }
            });

        } else {
            name.setVisibility(View.GONE);
            sign_up.setVisibility(View.VISIBLE);

            img_personal_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "You are not logged in. ", Toast.LENGTH_SHORT).show();
                }
            });

        }

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(),Sign_up.class);
                startActivity(it);

            }
        });





        id_cm_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Cm_family.class);
                startActivity(it);


            }
        });

        id_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent it = new Intent(Me_activity.this, Me_update.class);
//                startActivity(it);

                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=wewe.app.moboost");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToMarket);

            }
        });

        id_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(getActivity(), Me_settings.class);
                startActivity(it);


            }
        });
      return  view;
    }




    @Override
    public void onResume() {
        super.onResume();

        profile_image =sp.getString("profile_image", "");
        profile_image =url+profile_image;
        profile_image =profile_image.replaceAll(" ","%20");
        UrlImageViewHelper.setUrlDrawable(img_personal_details,profile_image,R.drawable.sign_activity);
    }
}
