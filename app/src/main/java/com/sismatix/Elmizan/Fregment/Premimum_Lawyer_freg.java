package com.sismatix.Elmizan.Fregment;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Premium_Lawyer_Video_adapter;
import com.sismatix.Elmizan.Adapter.Premium_Lawyer_adapter;
import com.sismatix.Elmizan.Adapter.Premium_article_adpter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Article_model;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Model;
import com.sismatix.Elmizan.Model.Premium_Lawyer_Videos_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sismatix.Elmizan.Adapter.Premium_Lawyer_Video_adapter.MyViewHolder.YPlayerr;

/**
 * A simple {@link Fragment} subclass.
 */

public class Premimum_Lawyer_freg extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    RecyclerView recycler_pre_lawyer_article;
    ProgressBar progressBar_article_lawyer;
    private List<Article_model> article_models = new ArrayList<Article_model>();
    private Premium_article_adpter articles_adapter;
    IndefinitePagerIndicator indicater;
    LinearLayout lv_primium_click;
    Bundle bundle;
    String user_id;
    View view;
    ProgressBar progressBar_premium;
    String phone_no;

    TextView tv_premium_article, tv_edit_your_profile, tv_premium_descr, tv_premium_usernm, tv_premium_site, tv_premium_email, tv_premium_address, tv_premium_phone,
            tv_pre_country_text, tv_pre_country, tv_premium_offline, tv_premium_appeal, tv_premium_name, tv_article_data_not_found, tv_premium_online;
    ImageView iv_premium_twitter, iv_premium_insta, iv_premium_fb, iv_profile_premium, iv_add_article, iv_add_media;
    LinearLayout lv_premium_twitter, lv_more_btn, lv_premium_insta, lv_premium_fb, lv_premium_call, lv_edit_your_profile, lv_pre_online, lv_pre_offline;

    /*Timer timer;
    final long DELAY_MS = 800, PERIOD_MS = 4000;
    int currentPage = 0;
    private ViewPager mPager;
    StringRequest stringRequest;
    private String URL_HOMEPAGE;*/

    RecyclerView recycler_prem_images;
    private List<Premium_Lawyer_Model> premium_lawyer_models = new ArrayList<Premium_Lawyer_Model>();
    private Premium_Lawyer_adapter premium_lawyer_adapter;

    RecyclerView recycler_prem_videos;
    private List<Premium_Lawyer_Videos_Model> premium_video_models = new ArrayList<Premium_Lawyer_Videos_Model>();
    private Premium_Lawyer_Video_adapter premium_video_adapter;

    Button btn_more;

    public Premimum_Lawyer_freg() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_premimum_lawyer_freg, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.premimum_lawyer));
        /*URL_HOMEPAGE = "https://elmizan.demoproject.info/api/user_media.php?user_id=" + Login_preference.getuser_id(getContext());*/
        AllocateMemory(view);

        bundle = this.getArguments();

        if (bundle != null) {
            user_id = bundle.getString("user_id");
            Log.e("user_id_57", "" + user_id);
        }

        if(user_id.equalsIgnoreCase(Login_preference.getuser_id(getContext()))==true)
        {
            if (My_Preference.get_premium_lawyer(getActivity()).equals("premium") == true) {


                lv_edit_your_profile.setVisibility(View.VISIBLE);
                iv_add_article.setVisibility(View.VISIBLE);
                iv_add_media.setVisibility(View.VISIBLE);
            }
        }else {
            lv_edit_your_profile.setVisibility(View.GONE);
            iv_add_article.setVisibility(View.GONE);
            iv_add_media.setVisibility(View.GONE);

        }


        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_Article_API();
            CALL_USER_DETAIL_API();
            CALL_GET_MEDIAIMAGES_API();
            CALL_GET_MEDIAVIDEOS_API();

        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

//        mPager.addOnPageChangeListener(this);
        //startAutoScrollViewPager();

        articles_adapter = new Premium_article_adpter(getActivity(), article_models);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_pre_lawyer_article.setLayoutManager(mLayoutManager);
        recycler_pre_lawyer_article.setAdapter(articles_adapter);

        lv_premium_call.setOnClickListener(this);
        lv_edit_your_profile.setOnClickListener(this);
        iv_add_article.setOnClickListener(this);
        iv_add_media.setOnClickListener(this);
        Log.e("user_id_110", "" + Login_preference.getuser_id(getActivity()));




        premium_lawyer_adapter = new Premium_Lawyer_adapter(getActivity(), premium_lawyer_models);
        recycler_prem_images.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recycler_prem_images.setItemAnimator(new DefaultItemAnimator());
        recycler_prem_images.setAdapter(premium_lawyer_adapter);

        premium_video_adapter = new Premium_Lawyer_Video_adapter(getActivity(), premium_video_models);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_prem_videos.setLayoutManager(mLayoutManager1);
        recycler_prem_videos.setAdapter(premium_video_adapter);
        Log.e("sizee", "" + premium_video_models.size());

        Log.e("uidre", "" + user_id);
        Log.e("uiddree", "" + Login_preference.getuser_id(getContext()));

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle b=new Bundle();
                b.putString("useridd",user_id);
                Log.e("usid",""+user_id);
                Fragment myFragment = new Media();
                myFragment.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out).replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();
            }
        });


        return view;

    }

    /*private void startAutoScrollViewPager() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == slidervideo_models.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }*/

    private void CALL_GET_MEDIAIMAGES_API() {
        premium_lawyer_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylist = api.getMedia(user_id);
        Log.e("uid", "" + user_id);
        Log.e("uidd", "" + Login_preference.getuser_id(getContext()));

        categorylist.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                premium_lawyer_models.clear();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_media", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        String msg_en = jsonObject.getString("data");
                        Log.e("data_189", "" + msg_en);

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("media_data", "" + data_obj);

                        String user_media_status = data_obj.getString("user_media_status");
                        Log.e("user_media_status_med", "" + user_media_status);

                        String article_media_url = data_obj.getString("arr_user_media_content_url");
                        Log.e("media_image", "" + article_media_url);

                        JSONObject image_obj = data_obj.getJSONObject("arr_user_media_content_url");
                        Log.e("imgg", "" + image_obj);

                        String image = image_obj.getString("image");
                        Log.e("imageeee", "" + image);

                        JSONArray imag_array = image_obj.getJSONArray("image");

                        if (imag_array != null && imag_array.isNull(0) != true) {
                            Log.e("imag_array", "" + imag_array);

                            int tmp_cnt;

                            if (imag_array.length() >= 5) {
                                tmp_cnt = 5;
                                Log.e("tmp_cnt", "" + tmp_cnt);
                            } else {
                                tmp_cnt = imag_array.length();
                            }
                            for (int j = 0; j < tmp_cnt; j++) {
                                try {
                                    image = imag_array.getString(j);
                                    Log.e("image_prem", "" + image);

                                    if (image == "" || image == null || image == "null" || image.equalsIgnoreCase(null)
                                            || image.equalsIgnoreCase("null")) {
                                        Toast.makeText(getActivity(), "data problem", Toast.LENGTH_SHORT).show();
                                    } else {

                                        premium_lawyer_models.add(new Premium_Lawyer_Model(image));

                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    premium_lawyer_adapter.notifyItemChanged(j);
                                }
                            }
                        }
                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CALL_GET_MEDIAVIDEOS_API() {

        premium_video_models.clear();
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> categorylistt = apii.getMedia(user_id);
        Log.e("uid", "" + user_id);
        Log.e("uidd", "" + Login_preference.getuser_id(getContext()));

        categorylistt.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                premium_video_models.clear();

                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(response.body().string());
                    String status = jsonObject1.getString("status");
                    Log.e("status_media_vids", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        String msg_en = jsonObject1.getString("data");
                        Log.e("data_vids", "" + msg_en);

                        JSONObject data_obj1 = jsonObject1.getJSONObject("data");
                        Log.e("media_data", "" + data_obj1);

                        String user_media_status = data_obj1.getString("user_media_status");
                        Log.e("user_media_status_med", "" + user_media_status);

                        String article_media_url = data_obj1.getString("arr_user_media_content_url");
                        Log.e("media_image", "" + article_media_url);

                        JSONObject vid_obj = data_obj1.getJSONObject("arr_user_media_content_url");
                        Log.e("imgg", "" + vid_obj);

                        String video = vid_obj.getString("video");
                        Log.e("videoooo", "" + video);

                        JSONArray vidss_array = vid_obj.getJSONArray("video");

                        if (vidss_array != null && vidss_array.isNull(0) != true) {
                            Log.e("vid_array", "" + vidss_array);

                            for (int j = 0; j < vidss_array.length(); j++) {
                                try {
                                    video = vidss_array.getString(0);
                                    Log.e("video_prem", "" + video);

                                    if (video == "" || video == null || video == "null" || video.equalsIgnoreCase(null)
                                            || video.equalsIgnoreCase("null")) {

                                        Toast.makeText(getActivity(), "data problem", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Log.e("sizeeee", "" + premium_video_models.size());
                                        premium_video_models.add(new Premium_Lawyer_Videos_Model(video));
                                        /*slidervideo_models.add(new slidervideo_model(video));
                                        mPager.setAdapter(new SlidingVideo_Adapter(getActivity(), slidervideo_models));*/

                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "" + e);
                                } finally {
                                    premium_video_adapter.notifyItemChanged(j);
                                }
                            }
                        }
                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.main_fram_layout, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    private void CALL_USER_DETAIL_API() {
        progressBar_premium.setVisibility(View.VISIBLE);
        lv_primium_click.setVisibility(View.GONE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_detail = api.get_user_detail(user_id);

        user_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                lv_more_btn.setVisibility(View.VISIBLE);
                progressBar_premium.setVisibility(View.GONE);
                lv_primium_click.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("status_data_obj", "" + data_obj);
                        phone_no = data_obj.getString("user_phone");
                        Navigation_activity.Check_String_NULL_Value(tv_premium_name, data_obj.getString("user_name"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_usernm, data_obj.getString("user_name"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_address, data_obj.getString("user_address"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_phone, data_obj.getString("user_phone"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_email, data_obj.getString("user_email"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_site, data_obj.getString("user_website"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_descr, data_obj.getString("user_description"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_appeal, data_obj.getString("user_description"));
                        Navigation_activity.Check_String_NULL_Value(tv_pre_country, data_obj.getString("user_country_name"));
                        tv_pre_country_text.setText(getResources().getString(R.string.country) + " :");
                        /*//tv_name_dircto.setText(data_obj.getString("user_name"));
                        tv_address_dircto.setText(data_obj.getString("user_address"));
                        tv_email_direct.setText(data_obj.getString("user_email"));
                        tv_phonenumber_dircto.setText(data_obj.getString("user_phone"));
                        tv_shortdicription_dircto.setText(data_obj.getString("user_description"));
                        tv_site_dircto.setText(data_obj.getString("user_website"));*/
                        Glide.with(getActivity()).load(data_obj.getString("user_avatar_url")).into(iv_profile_premium);

                    } else if (status.equalsIgnoreCase("error")) {
                        lv_more_btn.setVisibility(View.GONE);
                        progressBar_premium.setVisibility(View.GONE);
                        lv_primium_click.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CALL_Article_API() {
        progressBar_article_lawyer.setVisibility(View.VISIBLE);
        article_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> article_list = api.get_article_list(ApiClient.PAGE, ApiClient.PER_PAGE, ApiClient.user_status, user_id);

        article_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responsearticle", "" + response.body().toString());
                progressBar_article_lawyer.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    article_models.clear();
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_articel", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {
                        tv_article_data_not_found.setVisibility(View.GONE);
                        recycler_pre_lawyer_article.setVisibility(View.VISIBLE);
                        indicater.setVisibility(View.VISIBLE);

                        JSONArray data_array = jsonObject.getJSONArray("data");
                        String image = null, video = null;

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + news_object.getString("article_id"));

                                String article_media_url = news_object.getString("article_media_urls");
                                Log.e("article_media_urls", "" + article_media_url);


                                if (article_media_url.equalsIgnoreCase("false") == true) {
                                    image = "";
                                    video = "";
                                } else {
                                    JSONObject image_obj = news_object.getJSONObject("article_media_urls");
                                    JSONArray imag_array = image_obj.getJSONArray("image");
                                    if (imag_array != null && imag_array.isNull(0) != true) {
                                        Log.e("imag_array", "" + imag_array);

                                        for (int j = 0; j < imag_array.length(); j++) {
                                            image = imag_array.getString(j);
                                            Log.e("image_article", "" + image);

                                        }
                                    } else {
                                        Log.e("imag_array_else", "" + imag_array);
                                        image = "";
                                    }

                                    JSONArray video_array = image_obj.getJSONArray("video");
                                    if (video_array != null && video_array.isNull(0) != true) {
                                        Log.e("video_array", "" + video_array);

                                        for (int j = 0; j < video_array.length(); j++) {
                                            video = video_array.getString(j);
                                            Log.e("video_article", "" + video);
                                        }
                                    } else {
                                        Log.e("videp_array_else", "" + video_array);
                                        video = "";
                                    }

                                }
                                String date = news_object.getString("article_created_at_format_day") + " " +
                                        news_object.getString("article_created_at_format_month") + " " +
                                        news_object.getString("article_created_at_format_year");

                                article_models.add(new Article_model(news_object.getString("article_id"),
                                        news_object.getString("article_title"),
                                        news_object.getString("article_description"),
                                        image,
                                        news_object.getString("article_status"),
                                        date, video, news_object.getString("inserted_by_user")
                                ));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                articles_adapter.notifyItemChanged(i);
                            }

                        }

                        if (data_array.length() > 1) {
                            indicater.attachToRecyclerView(recycler_pre_lawyer_article);
                        } else {
                            indicater.setVisibility(View.GONE);
                        }
                        //   indicater.attachToRecyclerView(recycler_pre_lawyer_article);


                    } else if (status.equalsIgnoreCase("error")) {
                        tv_article_data_not_found.setVisibility(View.VISIBLE);
                        recycler_pre_lawyer_article.setVisibility(View.GONE);
                        indicater.setVisibility(View.GONE);
                        tv_article_data_not_found.setText(message);
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void AllocateMemory(View view) {

        //mPager = (ViewPager) view.findViewById(R.id.pager);
        // indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        btn_more = (Button) view.findViewById(R.id.btn_more);
        recycler_prem_images = (RecyclerView) view.findViewById(R.id.recycler_prem_images);
        tv_pre_country = (TextView) view.findViewById(R.id.tv_pre_country);
        tv_pre_country_text = (TextView) view.findViewById(R.id.tv_pre_country_text);
        tv_edit_your_profile = (TextView) view.findViewById(R.id.tv_edit_your_profile);
        tv_premium_article = (TextView) view.findViewById(R.id.tv_premium_article);
        tv_premium_descr = (TextView) view.findViewById(R.id.tv_premium_descr);
        tv_premium_usernm = (TextView) view.findViewById(R.id.tv_premium_usernm);
        tv_premium_site = (TextView) view.findViewById(R.id.tv_premium_site);
        tv_premium_email = (TextView) view.findViewById(R.id.tv_premium_email);
        tv_premium_address = (TextView) view.findViewById(R.id.tv_premium_address);
        tv_premium_phone = (TextView) view.findViewById(R.id.tv_premium_phone);

        tv_premium_offline = (TextView) view.findViewById(R.id.tv_premium_offline);
        tv_premium_appeal = (TextView) view.findViewById(R.id.tv_premium_appeal);
        tv_premium_name = (TextView) view.findViewById(R.id.tv_premium_name);
        tv_article_data_not_found = (TextView) view.findViewById(R.id.tv_article_data_not_found);
        tv_premium_online = (TextView) view.findViewById(R.id.tv_premium_online);

        iv_add_media = (ImageView) view.findViewById(R.id.iv_add_media);
        iv_add_article = (ImageView) view.findViewById(R.id.iv_add_article);
        iv_premium_twitter = (ImageView) view.findViewById(R.id.iv_premium_twitter);
        iv_premium_insta = (ImageView) view.findViewById(R.id.iv_premium_insta);
        iv_premium_insta = (ImageView) view.findViewById(R.id.iv_premium_insta);
        iv_premium_fb = (ImageView) view.findViewById(R.id.iv_premium_fb);
        iv_profile_premium = (ImageView) view.findViewById(R.id.iv_profile_premium);

        lv_more_btn = (LinearLayout) view.findViewById(R.id.lv_more_btn);
        lv_premium_twitter = (LinearLayout) view.findViewById(R.id.lv_premium_twitter);
        lv_premium_insta = (LinearLayout) view.findViewById(R.id.lv_premium_insta);
        lv_premium_fb = (LinearLayout) view.findViewById(R.id.lv_premium_fb);
        lv_premium_call = (LinearLayout) view.findViewById(R.id.lv_premium_call);
        lv_edit_your_profile = (LinearLayout) view.findViewById(R.id.lv_edit_your_profile);
        lv_pre_online = (LinearLayout) view.findViewById(R.id.lv_pre_online);
        lv_pre_offline = (LinearLayout) view.findViewById(R.id.lv_pre_offline);


        recycler_pre_lawyer_article = (RecyclerView) view.findViewById(R.id.recycler_pre_lawyer_article);
        progressBar_article_lawyer = view.findViewById(R.id.progressBar_article_lawyer);
        lv_primium_click = view.findViewById(R.id.lv_primium_click);
        progressBar_premium = view.findViewById(R.id.progressBar_premium);
        indicater = view.findViewById(R.id.recyclerview_pager_indicator);

        recycler_prem_videos = (RecyclerView) view.findViewById(R.id.recycler_prem_videos);

        tv_premium_article.setTypeface(Navigation_activity.typeface);
        tv_premium_descr.setTypeface(Navigation_activity.typeface);
        tv_premium_usernm.setTypeface(Navigation_activity.typeface);
        tv_premium_site.setTypeface(Navigation_activity.typeface);
        tv_premium_email.setTypeface(Navigation_activity.typeface);
        tv_premium_email.setTypeface(Navigation_activity.typeface);
        tv_premium_address.setTypeface(Navigation_activity.typeface);
        tv_premium_phone.setTypeface(Navigation_activity.typeface);
        //  tv_premium_off_dot.setTypeface(Navigation_activity.typeface);
        tv_premium_offline.setTypeface(Navigation_activity.typeface);
        tv_premium_online.setTypeface(Navigation_activity.typeface);
        tv_premium_appeal.setTypeface(Navigation_activity.typeface);
        tv_premium_name.setTypeface(Navigation_activity.typeface);
        tv_article_data_not_found.setTypeface(Navigation_activity.typeface);

    }


    @Override
    public void onClick(View view) {
        if(view==lv_edit_your_profile)
        {
            pushFragment(new Edit_premium_lawyer_profile());

/*

            if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {
                pushFragment(new Edit_premium_lawyer_profile());
            } else {
                pushFragment(new Login_freg());
            }*/

        }else if(view==iv_add_article)
        {


                    pushFragment(new Add_Article_Freg());



           /* if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {

                if (My_Preference.get_premium_lawyer(getActivity()).equals("premium") == true) {
                    pushFragment(new Add_Article_Freg());
                }
            } else {
                pushFragment(new Login_freg());
            }
*/
        }else if(view==iv_add_media)
        {



                pushFragment(new UPload_Media_freg());




        }
        else if(view==lv_premium_call)
        {
            if (phone_no == "" || phone_no == null || phone_no == "null" || phone_no.equalsIgnoreCase(null)
                    || phone_no.equalsIgnoreCase("null")) {

                Log.e("phone",""+phone_no);
            } else {

                Log.e("phone_104",""+phone_no);
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phone_no));
                startActivity(i);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    YPlayerr.release();
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


}
