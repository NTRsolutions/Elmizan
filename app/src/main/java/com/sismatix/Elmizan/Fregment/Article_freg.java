package com.sismatix.Elmizan.Fregment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonArray;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Articles_Adapter;
import com.sismatix.Elmizan.Adapter.Video_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.DotsIndicatorDecoration;
import com.sismatix.Elmizan.Model.Article_model;
import com.sismatix.Elmizan.Model.News_Model;
import com.sismatix.Elmizan.Model.Video_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Article_freg extends Fragment {
    View view, shadowView;
    RecyclerView recycler_article;
    private List<Article_model> article_models = new ArrayList<Article_model>();
    private Articles_Adapter articles_adapter;

    ProgressBar progressBar_article,progressBar_bottom_article;
    String user_id;
    TextView tv_article;
    Bundle bundle;
    FloatingActionMenu fab_menu;
    FloatingActionButton fab_add_article;
    NestedScrollView nested_scroll_article;
    GridLayoutManager layoutManager;
    boolean isLoading = true;
    int page_no=1,page;

    int pastvisibleitem, visibleitemcount, totalitemcount, previous_total = 0;

    public Article_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_article_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setTypeface(Navigation_activity.typeface);
        lang_arbi();
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.articles));
        String youtubeUrl = "https://www.youtube.com/watch?v=O3aemJ9eAAA";
        Log.e("yid",""+youtubeUrl);

        getYoutubeID(youtubeUrl);
        AllocateMemory(view);
        fab_btn_click_listner();

         bundle = this.getArguments();
         if (bundle != null) {

            user_id = bundle.getString("user_id");
             Log.e("user_id_80",""+user_id);
        }else {
             Log.e("user_id_89",""+user_id);
         }

        if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {

            if (My_Preference.get_premium_lawyer(getActivity()).equals("premium") == true) {
                fab_menu.setVisibility(View.VISIBLE);

            } else {
                fab_menu.setVisibility(View.GONE);
            }
        } else {
            fab_menu.setVisibility(View.GONE);
        }

        if (CheckNetwork.isNetworkAvailable(getActivity())) {
            CALL_Article_API(user_id,page_no);
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        articles_adapter = new Articles_Adapter(getActivity(), article_models);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        layoutManager=new GridLayoutManager(getActivity(), 2);
        recycler_article.setLayoutManager(layoutManager);
        recycler_article.setAdapter(articles_adapter);

        return view;
    }
    private String getYoutubeID(String youtubeUrl) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return "";
        }
        String video_id = "";

        String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = youtubeUrl;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(7);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                video_id = groupIndex1;
        }
        if (TextUtils.isEmpty(video_id)) {
            if (youtubeUrl.contains("youtu.be/")) {
                String spl = youtubeUrl.split("youtu.be/")[1];
                if (spl.contains("\\?")) {
                    video_id = spl.split("\\?")[0];
                } else {
                    video_id = spl;
                }
            }
        }
        Log.e("vidid", "" + video_id);
        return video_id;
    }
    private void fab_btn_click_listner() {


        fab_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    // showToast("Menu is opened");
                    shadowView.setVisibility(View.VISIBLE);
                } else {
                    // showToast("Menu is closed");
                    shadowView.setVisibility(View.GONE);
                }
            }
        });
        shadowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab_menu.isOpened()) {
                    fab_menu.close(true);
                }
            }
        });

        fab_add_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {

                    if (My_Preference.get_premium_lawyer(getActivity()).equals("premium") == true) {
                        pushFragment(new Add_Article_Freg(), "login");
                    }
                } else {

                    String screen = "Article";
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("screen",screen);
                    Fragment myFragment = new Login_freg();
                    myFragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

//pushFragment(new Login_freg(), "login");
                }


            }
        });
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

    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.main_fram_layout, fragment);
                ft.addToBackStack(add_to_backstack);
                ft.commit();
            }
        }
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    private void CALL_Article_API(final String user_idd, int page_no) {
        progressBar_article.setVisibility(View.VISIBLE);
        article_models.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> article_list;
        if (bundle==null) {
            Log.e("user_id_97", "" +user_idd);
            article_list = api.get_article_list(String.valueOf(page_no), ApiClient.PER_PAGE, ApiClient.user_status, "");
        } else {
            Log.e("user_id_101", "" + user_idd);
            article_list = api.get_article_list(String.valueOf(page_no), ApiClient.PER_PAGE, ApiClient.user_status, user_idd);
        }
        page=page_no;

        article_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responsearticle", "" + response.body().toString());
                progressBar_article.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_articel", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {
                        tv_article.setVisibility(View.GONE);
                        recycler_article.setVisibility(View.VISIBLE);

                        JSONArray data_array = jsonObject.getJSONArray("data");
                        String image = null,video=null;

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + news_object.getString("article_id"));


                                String article_media_url = news_object.getString("article_media_urls");
                                Log.e("article_media_urls", "" + article_media_url);


                                if (article_media_url.equalsIgnoreCase("false") == true) {
                                    image = "";
                                    video="";
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
                                        date,video,news_object.getString("inserted_by_user")
                                ));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                articles_adapter.notifyItemChanged(i);
                            }

                        }

                    } else if (status.equalsIgnoreCase("error")) {
                        tv_article.setTypeface(Navigation_activity.typeface);

                        tv_article.setVisibility(View.VISIBLE);
                        tv_article.setText(message);
                        recycler_article.setVisibility(View.GONE);
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


        ///pagination
        nested_scroll_article.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleitemcount = layoutManager.getChildCount();
                        totalitemcount = layoutManager.getItemCount();
                        pastvisibleitem = layoutManager.findFirstVisibleItemPosition();

                        if (isLoading) {

                            if ((visibleitemcount + pastvisibleitem) >= totalitemcount) {
                                page++;
                                Log.e("isloading", "loadscroll" + page);
                                progressBar_bottom_article.setVisibility(View.VISIBLE);

                                PerformPagination(page,user_idd);
                                //  isLoading=true;
//                        Load Your Data
                            }
                        }
                    }
                }
            }
        });

    }

    private void PerformPagination(int page, String user_idd) {
        Log.e("pagination_390", "" +page);
        progressBar_bottom_article.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        final Call<ResponseBody> article_list;
        if (bundle==null) {
            Log.e("user_id_97", "" +user_idd);
            article_list = api.get_article_list(String.valueOf(page), ApiClient.PER_PAGE, ApiClient.user_status, "");
        } else {
            Log.e("user_id_101", "" + user_idd);
            article_list = api.get_article_list(String.valueOf(page), ApiClient.PER_PAGE, ApiClient.user_status, user_idd);
        }
        page=page_no;

        article_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("responsearticle", "" + response.body().toString());
                progressBar_bottom_article.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_articel", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {
                        tv_article.setVisibility(View.GONE);
                        recycler_article.setVisibility(View.VISIBLE);

                        JSONArray data_array = jsonObject.getJSONArray("data");
                        String image = null,video=null;

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + news_object.getString("article_id"));


                                String article_media_url = news_object.getString("article_media_urls");
                                Log.e("article_media_urls", "" + article_media_url);


                                if (article_media_url.equalsIgnoreCase("false") == true) {
                                    image = "";
                                    video="";
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
                                        date,video,news_object.getString("inserted_by_user")
                                ));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                articles_adapter.notifyItemChanged(i);
                            }

                        }

                    } else if (status.equalsIgnoreCase("error")) {
                        tv_article.setTypeface(Navigation_activity.typeface);
                        progressBar_bottom_article.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                        tv_article.setVisibility(View.GONE);
                        //tv_article.setText(message);
                     //   recycler_article.setVisibility(View.GONE);
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
        recycler_article = (RecyclerView) view.findViewById(R.id.recycler_article);
        progressBar_article = view.findViewById(R.id.progressBar_article);
        progressBar_bottom_article = view.findViewById(R.id.progressBar_bottom_article);
        tv_article = view.findViewById(R.id.tv_article);
        fab_menu = (FloatingActionMenu) view.findViewById(R.id.fab_menu);
        fab_add_article = (FloatingActionButton) view.findViewById(R.id.fab_add_article);
        shadowView = (View) view.findViewById(R.id.shadowView);
        nested_scroll_article = (NestedScrollView) view.findViewById(R.id.nested_scroll_article);

    }


}
