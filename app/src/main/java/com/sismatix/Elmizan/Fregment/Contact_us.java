package com.sismatix.Elmizan.Fregment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Contact_us extends Fragment {

    TextView tv_gmail,tv_no,tv_instagram,tv_twitter,tv_contact;
    ImageView iv_cu;
    ProgressBar progressBar_contact;
    LinearLayout lv_contact;

    View v;
    public Contact_us() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_contact_us, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setTypeface(Navigation_activity.typeface);
        lang_arbi();
        AllocateMemory(v);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            callContactUsApi();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return  v;
    }

    private void callContactUsApi() {

        progressBar_contact.setVisibility(View.VISIBLE);
        lv_contact.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> getabout_us = api.get_contact_us();

        getabout_us.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);


                progressBar_contact.setVisibility(View.GONE);
                lv_contact.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){

                        JSONObject data_obj=jsonObject.getJSONObject("data");
                        Log.e("status_data_au",""+data_obj);

                        String contact_emai_one = data_obj.getString("contact_email");
                        Log.e("contact_email",""+contact_emai_one);

                        //Glide.with(getActivity()).load(data_obj.getString("user_avatar_url")).into(iv_lawyer_profile);

                        JSONObject contact_email = data_obj.getJSONObject("contact_email");
                        Log.e("contact_emaill",""+contact_email);

                        String global_content = contact_email.getString("global_content");
                        Log.e("global_cont_au_email",""+global_content);

                        tv_gmail.setText(global_content);

                        JSONObject contact_phone = data_obj.getJSONObject("contact_phone");
                        Log.e("contact_phone",""+contact_phone);

                        String global_content2 = contact_phone.getString("global_content");
                        Log.e("contact_phone",""+contact_phone);

                        tv_no.setText(global_content2);

                        JSONObject contact_insta = data_obj.getJSONObject("contact_insta");
                        Log.e("contact_insta",""+contact_insta);

                        String global_content3 = contact_insta.getString("global_content");
                        Log.e("global_cont_insta",""+global_content3);

                        tv_instagram.setText(global_content3);

                        JSONObject contact_twitter = data_obj.getJSONObject("contact_twitter");
                        Log.e("contact_twitter",""+contact_twitter);

                        String contact_twitterr = contact_twitter.getString("global_content");
                        Log.e("global_cont_twitter",""+contact_twitterr);

                        tv_twitter.setText(contact_twitterr);

                        JSONObject contact_image = data_obj.getJSONObject("contact_image");
                        Log.e("contact_image",""+contact_image);

                        String global_content4 = contact_image.getString("global_content_url");
                        Log.e("global_cont_image",""+global_content4);

                       /* Glide.with(getContext()).load(global_content4).into(new SimpleTarget<Bitmap>(200,200) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    lv_back_image.setBackground(drawable);
                                }
                            }
                        });*/

                        Glide.with(getActivity()).load(global_content4).into(iv_cu);

                    }else if (status.equalsIgnoreCase("error")){
                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AllocateMemory(View v) {
        tv_contact = (TextView)v.findViewById(R.id.tv_contact);
        progressBar_contact = (ProgressBar) v.findViewById(R.id.progressBar_contact);
        lv_contact = (LinearLayout) v.findViewById(R.id.lv_contact);
        tv_gmail = (TextView)v.findViewById(R.id.tv_gmail);
        tv_no = (TextView)v.findViewById(R.id.tv_no);
        tv_instagram = (TextView)v.findViewById(R.id.tv_instagram);
        tv_twitter = (TextView)v.findViewById(R.id.tv_twitter);
        iv_cu = (ImageView)v.findViewById(R.id.iv_cu);


        tv_contact.setTypeface(Navigation_activity.typeface);
        tv_gmail.setTypeface(Navigation_activity.typeface);
        tv_no.setTypeface(Navigation_activity.typeface);
        tv_instagram.setTypeface(Navigation_activity.typeface);
        tv_twitter.setTypeface(Navigation_activity.typeface);
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    /*@Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }*/
}
