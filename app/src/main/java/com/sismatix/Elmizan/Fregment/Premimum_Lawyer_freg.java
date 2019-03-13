package com.sismatix.Elmizan.Fregment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Articles_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Article_model;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Premimum_Lawyer_freg extends Fragment {
    RecyclerView recycler_pre_lawyer_article;
    ProgressBar progressBar_article_lawyer;
    private List<Article_model> article_models = new ArrayList<Article_model>();
    private Articles_Adapter articles_adapter;
    IndefinitePagerIndicator indicater;
    LinearLayout lv_primium_click;
    Bundle bundle;
    String user_id;
    View view;
    ProgressBar progressBar_premium;

    TextView tv_premium_article,tv_premium_descr,tv_premium_usernm,tv_premium_site,tv_premium_email,tv_premium_address,tv_premium_phone,
            tv_premium_off_dot,tv_premium_off_onn,tv_premium_appeal,tv_premium_name,tv_article_data_not_found;
    ImageView iv_premium_twitter,iv_premium_insta,iv_premium_fb,iv_profile_premium;
    LinearLayout lv_premium_twitter,lv_premium_insta,lv_premium_fb,lv_premium_call;
    public Premimum_Lawyer_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_premimum_lawyer_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.premimum_lawyer));
        bundle = this.getArguments();

        if(bundle!=null){
            user_id=bundle.getString("user_id");
            Log.e("user_id_57",""+user_id);
        }
        AllocateMemory(view);

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Article_API();
            CALL_USER_DETAIL_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        articles_adapter = new Articles_Adapter(getActivity(), article_models);
         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        ///new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false)
        recycler_pre_lawyer_article.setLayoutManager(mLayoutManager);
        recycler_pre_lawyer_article.setItemAnimator(new DefaultItemAnimator());
        // recycler_product.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recycler_pre_lawyer_article.setAdapter(articles_adapter);


        return view;
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


                progressBar_premium.setVisibility(View.GONE);
                lv_primium_click.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail",""+status);
                    if (status.equalsIgnoreCase("success")){

                        JSONObject data_obj=jsonObject.getJSONObject("data");
                        Log.e("status_data_obj",""+data_obj);

                       Navigation_activity.Check_String_NULL_Value(tv_premium_name,data_obj.getString("user_name"));
                       Navigation_activity.Check_String_NULL_Value(tv_premium_usernm,data_obj.getString("user_name"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_address,data_obj.getString("user_address"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_phone,data_obj.getString("user_phone"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_email,data_obj.getString("user_email"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_site,data_obj.getString("user_website"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_descr,data_obj.getString("user_description"));
                        Navigation_activity.Check_String_NULL_Value(tv_premium_appeal,data_obj.getString("user_description"));
                        /*//tv_name_dircto.setText(data_obj.getString("user_name"));
                        tv_address_dircto.setText(data_obj.getString("user_address"));
                        tv_email_direct.setText(data_obj.getString("user_email"));
                        tv_phonenumber_dircto.setText(data_obj.getString("user_phone"));
                        tv_shortdicription_dircto.setText(data_obj.getString("user_description"));
                        tv_site_dircto.setText(data_obj.getString("user_website"));*/
                        Glide.with(getActivity()).load(data_obj.getString("user_avatar_url")).into(iv_profile_premium);


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
    private void CALL_Article_API() {
        progressBar_article_lawyer.setVisibility(View.VISIBLE);
        article_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> article_list = api.get_article_list(ApiClient.PAGE,ApiClient.PER_PAGE,ApiClient.user_status,user_id);

        article_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responsearticle", "" + response.body().toString());
                progressBar_article_lawyer.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
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
                        String image = null;
                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + news_object.getString("article_id"));
                                JSONObject image_obj = news_object.getJSONObject("article_media_urls");
                                JSONArray imag_array = image_obj.getJSONArray("image");
                                for (int j = 0; j < imag_array.length(); j++) {

                                    image = imag_array.getString(i);
                                    Log.e("image_article", "" + image);

                                }

                                article_models.add(new Article_model(news_object.getString("article_id"),
                                        news_object.getString("article_title"),
                                        news_object.getString("article_description"),
                                        image,
                                        news_object.getString("article_status"),
                                        news_object.getString("article_created_at")
                                ));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                articles_adapter.notifyItemChanged(i);
                            }

                        }

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
        tv_premium_article = (TextView) view.findViewById(R.id.tv_premium_article);
        tv_premium_descr = (TextView) view.findViewById(R.id.tv_premium_descr);
        tv_premium_usernm = (TextView) view.findViewById(R.id.tv_premium_usernm);
        tv_premium_site = (TextView) view.findViewById(R.id.tv_premium_site);
        tv_premium_email = (TextView) view.findViewById(R.id.tv_premium_email);
        tv_premium_address = (TextView) view.findViewById(R.id.tv_premium_address);
        tv_premium_phone = (TextView) view.findViewById(R.id.tv_premium_phone);
        tv_premium_off_dot = (TextView) view.findViewById(R.id.tv_premium_off_dot);
        tv_premium_off_onn = (TextView) view.findViewById(R.id.tv_premium_off_onn);
        tv_premium_appeal = (TextView) view.findViewById(R.id.tv_premium_appeal);
        tv_premium_name = (TextView) view.findViewById(R.id.tv_premium_name);
        tv_article_data_not_found = (TextView) view.findViewById(R.id.tv_article_data_not_found);

        iv_premium_twitter = (ImageView) view.findViewById(R.id.iv_premium_twitter);
        iv_premium_insta = (ImageView) view.findViewById(R.id.iv_premium_insta);
        iv_premium_insta = (ImageView) view.findViewById(R.id.iv_premium_insta);
        iv_premium_fb = (ImageView) view.findViewById(R.id.iv_premium_fb);
        iv_profile_premium = (ImageView) view.findViewById(R.id.iv_profile_premium);

        lv_premium_twitter = (LinearLayout) view.findViewById(R.id.lv_premium_twitter);
        lv_premium_insta = (LinearLayout) view.findViewById(R.id.lv_premium_insta);
        lv_premium_fb = (LinearLayout) view.findViewById(R.id.lv_premium_fb);
        lv_premium_call = (LinearLayout) view.findViewById(R.id.lv_premium_call);


        recycler_pre_lawyer_article = (RecyclerView) view.findViewById(R.id.recycler_pre_lawyer_article);
        progressBar_article_lawyer = view.findViewById(R.id.progressBar_article_lawyer);
        lv_primium_click = view.findViewById(R.id.lv_primium_click);
        progressBar_premium = view.findViewById(R.id.progressBar_premium);
        indicater =view.findViewById(R.id.recyclerview_pager_indicator);
        indicater.attachToRecyclerView(recycler_pre_lawyer_article);
    }

}
