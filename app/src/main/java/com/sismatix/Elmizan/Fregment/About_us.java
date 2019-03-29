package com.sismatix.Elmizan.Fregment;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class About_us extends Fragment {

    View v;
    TextView tv_detail1,tv_detail,tv_balance_detail,tv_about_us;
    LinearLayout lv_back_image;
    ImageView iv_au_img;

    public About_us() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_about_us, container, false);

        AllocateMemory(v);
        callAboutUsApi();

        return  v;
    }

    private void callAboutUsApi() {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> getabout_us = api.get_about_us();

        getabout_us.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){

                        JSONObject data_obj=jsonObject.getJSONObject("data");
                        Log.e("status_data_au",""+data_obj);

                        String about_us_1 = data_obj.getString("about_us_1");
                        Log.e("about_us_1_au",""+about_us_1);

                        JSONObject about_us_one = data_obj.getJSONObject("about_us_1");
                        Log.e("about_us_one",""+about_us_one);

                        String global_content = about_us_one.getString("global_content");
                        Log.e("global_cont_au1",""+global_content);

                        tv_balance_detail.setText(global_content);

                        JSONObject about_us_2 = data_obj.getJSONObject("about_us_2");
                        Log.e("about_us_two",""+about_us_2);

                        String global_content2 = about_us_2.getString("global_content");
                        Log.e("global_cont_au2",""+global_content2);

                        tv_detail.setText(global_content);

                        JSONObject about_us_3 = data_obj.getJSONObject("about_us_3");
                        Log.e("about_us_two",""+about_us_3);

                        String global_content3 = about_us_3.getString("global_content");
                        Log.e("global_cont_au3",""+global_content3);

                        tv_detail1.setText(global_content3);

                        JSONObject about_us_2_image = data_obj.getJSONObject("about_us_2_image");
                        Log.e("about_us_2_image",""+about_us_2_image);

                        String global_content4 = about_us_2_image.getString("global_content");
                        Log.e("global_cont_au2img",""+global_content4);

                        /*Glide.with(getContext()).load(global_content4).into(new SimpleTarget<Bitmap>(200,200) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(getContext().getResources(), resource);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    lv_back_image.setBackground(drawable);
                                }
                            }
                        });*/

                        Glide.with(getActivity()).load(global_content4).into(iv_au_img);

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
        tv_about_us=(TextView)v.findViewById(R.id.tv_about_us);
        tv_balance_detail=(TextView)v.findViewById(R.id.tv_balance_detail);
        tv_detail=(TextView)v.findViewById(R.id.tv_detail);
        tv_detail1=(TextView)v.findViewById(R.id.tv_detail1);
        iv_au_img = (ImageView)v.findViewById(R.id.iv_au_img);

        tv_about_us.setTypeface(Navigation_activity.typeface);
        tv_balance_detail.setTypeface(Navigation_activity.typeface);
        tv_detail.setTypeface(Navigation_activity.typeface);
        tv_detail1.setTypeface(Navigation_activity.typeface);

    }

    @Override
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
    }

}
