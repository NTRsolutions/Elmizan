package com.sismatix.Elmizan.Fregment;


import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.Adapter.Articles_Adapter;
import com.sismatix.Elmizan.Adapter.Comment_adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Configgg;
import com.sismatix.Elmizan.Model.Article_model;
import com.sismatix.Elmizan.Model.Comment_Model;
import com.sismatix.Elmizan.Model.News_Model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class News_Detail_freg extends Fragment implements View.OnClickListener {
    View view;
    String news_id, article_id;
    TextView tv_detail_news_title,tv_tot_likes,tv_comment_nodata, tv_detail_comment,tv_send_news, tv_news_detail_date, tv_news_detail_description, tv_detail_news_add_comment, tv_posted_by, tv_posted;
    ImageView iv_news_detail_like, iv_news_detail_bookmark, iv_news_detail_share, iv_news_detail_image;
    EditText edt_news_detail_comment;
    RelativeLayout lv_news_parent;
    LinearLayout lv_news_detail_send, lv_news_detail,lv_postedby;
    ProgressBar progressBar_newsdetail;
    public static String login_flag;
    FloatingActionMenu fab_menu_article;
    FloatingActionButton fab_edit_article,fab_delete_article;
    View shadowView_article;
    String article_inserted_by;
    JSONArray video_array;
    LinearLayout lv_image_details, lv_video_details;


    RecyclerView recycler_comment;
    Comment_adapter comment_adapter;
    private List<Comment_Model> comment_model = new ArrayList<Comment_Model>();
    public static YouTubePlayer YPlayer;
    String video, image, video_idd, video_id, spl;
    LinearLayoutManager layoutManager;
    int page_no=1,page;

    NestedScrollView scroll_news;
    boolean isLoading = true;
    int pastvisibleitem, visibleitemcount, totalitemcount, previous_total = 0;
    ProgressBar progressBar_bottom_recv_comment;



    public News_Detail_freg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news__detail_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setTypeface(Navigation_activity.typeface);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        lang_arbi();
        Login_freg.hideSoftKeyboard(getActivity());
        Allocate_Memory(view);
        setupUI(lv_news_parent);

        scroll_news.scrollTo(0,0);
        scroll_news.setSmoothScrollingEnabled(true);


        Log.e("videooooo", "" + video);

        Bundle bundle = this.getArguments();
        login_flag = Login_preference.getLogin_flag(getActivity());

        if (bundle != null) {

            news_id = bundle.getString("news_id");
            article_id = bundle.getString("article_id");
            Log.e("news_id_43", "" + news_id);
            Log.e("article_id_79", "" + article_id);
        }


        comment_adapter = new Comment_adapter(getActivity(), comment_model);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        layoutManager=new LinearLayoutManager(getActivity());
        recycler_comment.setLayoutManager(layoutManager);
        recycler_comment.setAdapter(comment_adapter);



        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            if (news_id == "" || news_id == null || news_id == "null" || news_id.equalsIgnoreCase(null)
                    || news_id.equalsIgnoreCase("null")) {

                Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.articles));

                CALL_ARTICLE_DETAIL_API();
                CALL_GET_article_COMMENT_API(article_id, String.valueOf(page_no));

                fab_btn_click_listner();

            } else {
                Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.single_news_page));

                CALL_News_Detail_API();
                CALL_GET_News_COMMENT_API(news_id, String.valueOf(page_no));
            }

        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        lv_news_detail_send.setOnClickListener(this);
        iv_news_detail_like.setOnClickListener(this);



        return view;

    }


    public void CALL_GET_News_COMMENT_API(String id ,String page_no) {

      /*  if(page_no.equals("1"))
        {
          // comment_model.clear();

        }else {
            progressBar_bottom_recv_comment.setSystemUiVisibility(View.GONE);
        }*/

        // progressBar_home.setVisibility(View.VISIBLE);
        comment_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> News_list = api.get_news_comment(id,page_no,ApiClient.PER_PAGE,ApiClient.user_status);

        page= Integer.parseInt(page_no);

        Log.e("pageno_108",""+page);
        Log.e("nmewsid",""+id);
        News_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_article_comm", "" + response.body().toString());
                //       progressBar_home.setVisibility(View.GONE);
                progressBar_bottom_recv_comment.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                        JSONArray data_array=jsonObject.getJSONArray("data");

                        //  Toast.makeText(getActivity(), "page_loade="+page, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                comment_model.add(new Comment_Model(news_object.getString("user_fullname"),
                                        news_object.getString("news_comment_id"),
                                        news_object.getString("time"),
                                        news_object.getString("news_comment_content")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                comment_adapter.notifyItemChanged(i);
                            }

                        }
                        if (comment_model.size() == 0) {
                            tv_comment_nodata.setVisibility(View.VISIBLE);
                            tv_comment_nodata.setTypeface(Navigation_activity.typeface);

                        } else {
                            tv_comment_nodata.setVisibility(View.GONE);
                            progressBar_bottom_recv_comment.setVisibility(View.GONE);

                        }

                    }else if (status.equalsIgnoreCase("error")){
                        tv_comment_nodata.setVisibility(View.VISIBLE);
                        tv_comment_nodata.setTypeface(Navigation_activity.typeface);
                        //             progressBar_home.setVisibility(View.GONE);
                        recycler_comment.setVisibility(View.GONE);
                        progressBar_bottom_recv_comment.setVisibility(View.GONE);

                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressBar_bottom_recv_comment.setVisibility(View.GONE);

            }
        });

        scroll_news.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                progressBar_bottom_recv_comment.setVisibility(View.VISIBLE);

                                PerformPagination_news(news_id, String.valueOf(page));
                                //CALL_GET_News_COMMENT_API(news_id, String.valueOf(page));
                                //  isLoading=true;
//                        Load Your Data
                            }
                        }
                    }
                }
            }
        });

    }

    private void PerformPagination_news(String news_id,String page_no) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> News_list = api.get_news_comment(news_id,page_no,ApiClient.PER_PAGE,ApiClient.user_status);

        page= Integer.parseInt(page_no);
        progressBar_bottom_recv_comment.setVisibility(View.VISIBLE);

        Log.e("pageno_108",""+page);
        Log.e("nmewsid",""+news_id);
        News_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_article_comm", "" + response.body().toString());
                //       progressBar_home.setVisibility(View.GONE);
                progressBar_bottom_recv_comment.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                        JSONArray data_array=jsonObject.getJSONArray("data");

                        //  Toast.makeText(getActivity(), "page_loade="+page, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                comment_model.add(new Comment_Model(news_object.getString("user_fullname"),
                                        news_object.getString("news_comment_id"),
                                        news_object.getString("time"),
                                        news_object.getString("news_comment_content")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                comment_adapter.notifyItemChanged(i);
                            }

                        }
                        if (comment_model.size() == 0) {
                            tv_comment_nodata.setTypeface(Navigation_activity.typeface);

                            tv_comment_nodata.setVisibility(View.VISIBLE);
                        } else {
                            tv_comment_nodata.setVisibility(View.GONE);
                            progressBar_bottom_recv_comment.setVisibility(View.GONE);

                        }

                    }else if (status.equalsIgnoreCase("error")){
                       // tv_comment_nodata.setVisibility(View.VISIBLE);
                        //             progressBar_home.setVisibility(View.GONE);
                       // recycler_comment.setVisibility(View.GONE);
                        progressBar_bottom_recv_comment.setVisibility(View.GONE);

                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressBar_bottom_recv_comment.setVisibility(View.GONE);

            }
        });
    }


    public void CALL_GET_article_COMMENT_API(String id ,String page_no) {

           comment_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> News_list = api.get_article_comment(id,page_no,ApiClient.PER_PAGE,ApiClient.user_status);

        page= Integer.parseInt(page_no);

        Log.e("pageno_108",""+page);
        Log.e("articleid",""+id);
        News_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_article_comm", "" + response.body().toString());
         //       progressBar_home.setVisibility(View.GONE);
                progressBar_bottom_recv_comment.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                        JSONArray data_array=jsonObject.getJSONArray("data");

                        //  Toast.makeText(getActivity(), "page_loade="+page, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                comment_model.add(new Comment_Model(news_object.getString("user_fullname"),
                                        news_object.getString("article_comment_id"),
                                        news_object.getString("time"),
                                        news_object.getString("article_comment_content")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                comment_adapter.notifyItemChanged(i);
                            }

                        }
                        if (comment_model.size() == 0) {
                            tv_comment_nodata.setVisibility(View.VISIBLE);
                            tv_comment_nodata.setTypeface(Navigation_activity.typeface);

                        } else {
                            tv_comment_nodata.setVisibility(View.GONE);
                        }

                    }else if (status.equalsIgnoreCase("error")){
                        tv_comment_nodata.setVisibility(View.VISIBLE);
                        tv_comment_nodata.setTypeface(Navigation_activity.typeface);

                        //             progressBar_home.setVisibility(View.GONE);
                        recycler_comment.setVisibility(View.GONE);

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

        scroll_news.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                              //  progressBar_bottom_recv_comment.setVisibility(View.VISIBLE);

                                PerformPagination_article(article_id, String.valueOf(page));
                              //  CALL_GET_COMMENT_API(article_id, String.valueOf(page));
                                //  isLoading=true;
//                        Load Your Data
                            }
                        }
                    }
                }
            }
        });

    }

    private void PerformPagination_article(String article_id, String page_no) {
        progressBar_bottom_recv_comment.setVisibility(View.VISIBLE);

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> News_list = api.get_article_comment(article_id,page_no,ApiClient.PER_PAGE,ApiClient.user_status);

        page= Integer.parseInt(page_no);

        Log.e("pageno_444",""+page);
        Log.e("articleid",""+article_id);
        News_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_article_comm", "" + response.body().toString());
                //       progressBar_home.setVisibility(View.GONE);
                progressBar_bottom_recv_comment.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_prod_cat",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                        JSONArray data_array=jsonObject.getJSONArray("data");

                        //  Toast.makeText(getActivity(), "page_loade="+page, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                comment_model.add(new Comment_Model(news_object.getString("user_fullname"),
                                        news_object.getString("article_comment_id"),
                                        news_object.getString("time"),
                                        news_object.getString("article_comment_content")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                comment_adapter.notifyItemChanged(i);
                            }

                        }
                        if (comment_model.size() == 0) {
                            tv_comment_nodata.setVisibility(View.VISIBLE);
                            tv_comment_nodata.setTypeface(Navigation_activity.typeface);

                            progressBar_bottom_recv_comment.setVisibility(View.GONE);

                        } else {
                            tv_comment_nodata.setVisibility(View.GONE);
                            progressBar_bottom_recv_comment.setVisibility(View.GONE);

                        }

                    }else if (status.equalsIgnoreCase("error")){
                        progressBar_bottom_recv_comment.setVisibility(View.GONE);

                        // tv_comment_nodata.setVisibility(View.VISIBLE);
                        //             progressBar_home.setVisibility(View.GONE);
                      //  recycler_comment.setVisibility(View.GONE);

                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressBar_bottom_recv_comment.setVisibility(View.GONE);

            }
        });

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Login_freg.hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    private void Youtube(final String video) {

        Log.e("vidssssss", "" + video);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(Configgg.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {

                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(false);
                    YPlayer.setShowFullscreenButton(false);
                    YPlayer.loadVideo(video_id);
                    YPlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            YPlayer.pause();
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                    YPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                    YPlayer.getCurrentTimeMillis();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    private String getYoutubeVideoID(String video) {
        if (TextUtils.isEmpty(video)) {
            return "";
        }
        video_id = "";

        String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        CharSequence input = video;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(7);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                video_id = groupIndex1;
        }
        if (TextUtils.isEmpty(video_id)) {
            if (video.contains("youtu.be/")) {
                spl = video.split("youtu.be/")[1];
                if (spl.contains("\\?")) {
                    video_id = spl.split("\\?")[0];
                } else {
                    video_id = spl;
                }
            }
        }
        Log.e("vidid_details", "" + video_id);
        return video_id;
    }


    private void fab_btn_click_listner() {


        fab_menu_article.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    // showToast("Menu is opened");
                    shadowView_article.setVisibility(View.VISIBLE);
                } else {
                    // showToast("Menu is closed");
                    shadowView_article.setVisibility(View.GONE);
                }
            }
        });
        shadowView_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab_menu_article.isOpened()) {
                    fab_menu_article.close(true);
                }
            }
        });

        fab_delete_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {

                    if (My_Preference.get_premium_lawyer(getActivity()).equals("premium") == true) {
                        String user_id=Login_preference.getuser_id(getActivity());

                      //  Toast.makeText(getActivity(), article_id+"delete article"+user_id, Toast.LENGTH_SHORT).show();

                        Call_Delete_article_api(article_id,user_id);
                    }
                } else {

                    /*String screen = "news_details";
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("screen", "" + screen);
                    bundle1.putString("article_id", "" + article_id);
                    Fragment myFragment = new Login_freg();
                    myFragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();*/

                    pushFragment(new Login_freg(), "login");
                }




            }
        });

        fab_edit_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {

                    if (My_Preference.get_premium_lawyer(getActivity()).equals("premium") == true) {

                        Bundle b = new Bundle();
                        b.putString("article_id", article_id);
                        Fragment myFragment = new Add_Article_Freg();
                        myFragment.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                    }
                } else {

                    /*String screen = "news_details";
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("screen", "" + screen);
                    bundle1.putString("article_id", "" + article_id);
                    Fragment myFragment = new Login_freg();
                    myFragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();*/

                    pushFragment(new Login_freg(), "login");
                }


            }
        });
    }

    private void Call_Delete_article_api(String article_id, String user_id) {
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> register = apii.article_delete(article_id, user_id);
        Log.e("article_id", "" + article_id);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {

                        //JSONObject object=new JSONObject(jsonObject.getString("data"));

                        Bundle b = new Bundle();
                        b.putString("user_id", Login_preference.getuser_id(getActivity()));
                        Fragment myFragment = new Article_freg();
                        myFragment.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        //getActivity().finish();
                    } else if (status.equalsIgnoreCase("error")) {
                       // Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              //  Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CALL_ARTICLE_DETAIL_API() {
        progressBar_newsdetail.setVisibility(View.VISIBLE);
        lv_news_detail.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        String userid = Login_preference.getuser_id(getActivity());

        Call<ResponseBody> article_detail;
        if (login_flag.equalsIgnoreCase("1") || login_flag == "1") {
            article_detail = api.get_article_detail(article_id, userid);
        } else {
            article_detail = api.get_article_detail(article_id, "");
        }

        article_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_article_detail", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {

                    progressBar_newsdetail.setVisibility(View.GONE);
                    lv_news_detail.setVisibility(View.VISIBLE);
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("status_data_obj", "" + data_obj);

                        String date = data_obj.getString("article_created_at_format_day") + " " +
                                data_obj.getString("article_created_at_format_month") + " " +
                                data_obj.getString("article_created_at_format_year");
                        article_inserted_by = data_obj.getString("inserted_by");
                        Log.e("article_date_33",""+date);

                        String userid = Login_preference.getuser_id(getActivity());
                        Log.e("userid_120", "" + userid);
                        Log.e("article_inserted_by", "" + article_inserted_by);

                        if (article_inserted_by.equalsIgnoreCase(userid) == true) {
                            fab_menu_article.setVisibility(View.VISIBLE);
                        } else {
                            fab_menu_article.setVisibility(View.GONE);
                        }

                        Log.e("article_inserted_by", "" + article_inserted_by);
                        Navigation_activity.Check_String_NULL_Value(tv_detail_news_title, data_obj.getString("article_title"));
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_date, date);
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_description, String.valueOf(Html.fromHtml(data_obj.getString("article_description"))));

                        Navigation_activity.Check_String_NULL_Value(tv_posted_by, data_obj.getString("article_created_by"));


                        if (data_obj.getString("article_created_by") != null && !data_obj.getString("article_created_by").isEmpty()
                                && !data_obj.getString("article_created_by").equals("null")) {

                            lv_postedby.setVisibility(View.VISIBLE);
                            Navigation_activity.Check_String_NULL_Value(tv_posted_by, data_obj.getString("article_created_by"));

                        } else {
                            Log.e("nulllll", "");
                            lv_postedby.setVisibility(View.GONE);
                        }

                        String check_if_article_liked = data_obj.getString("check_if_article_liked");
                        if (check_if_article_liked.equalsIgnoreCase("true") == true) {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_36dp));
                        } else {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }

                        /*tv_detail_news_title.setText(data_obj.getString("news_title"));
                        tv_news_detail_date.setText(data_obj.getString("news_created_at"));
                        tv_news_detail_description.setText(data_obj.getString("news_description"));
                        tv_posted_by.setText(data_obj.getString("news_created_by"));*/

                        String article_media_url = data_obj.getString("article_media_urls");
                        Log.e("article_media_urls", "" + article_media_url);

                        if (article_media_url.equalsIgnoreCase("false") == true) {
                            image = "";
                            video = "";
                        } else {
                            JSONObject image_obj = data_obj.getJSONObject("article_media_urls");
                            JSONArray imag_array = image_obj.getJSONArray("image");
                            if (imag_array != null && imag_array.isNull(0) != true) {
                                Log.e("imag_array", "" + imag_array);
                                lv_image_details.setVisibility(View.VISIBLE);
                                lv_video_details.setVisibility(View.GONE);

                                for (int j = 0; j < imag_array.length(); j++) {
                                    image = imag_array.getString(j);
                                    Log.e("image_article", "" + image);

                                    if (image == "" || image == null || image == "null" || image.equalsIgnoreCase(null)
                                            || image.equalsIgnoreCase("null")) {
                                        lv_image_details.setVisibility(View.GONE);
                                    } else {

                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.placeholder(R.drawable.placeholder);
                                        requestOptions.error(R.drawable.placeholder);


                                        Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                                                .load(image).into(iv_news_detail_image);
                                    }

                                }

                            } else {
                                Log.e("imag_array_else", "" + imag_array);
                                image = "";
                                lv_video_details.setVisibility(View.GONE);
                                lv_image_details.setVisibility(View.GONE);
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.placeholder);
                                requestOptions.error(R.drawable.placeholder);

                                Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                                        .load(image).into(iv_news_detail_image);
                            }
                             video_array = image_obj.getJSONArray("video");
                            if (video_array != null && video_array.isNull(0) != true) {
                                Log.e("video_array", "" + video_array);
                                lv_video_details.setVisibility(View.VISIBLE);

                                for (int j = 0; j < video_array.length(); j++) {
                                    video = video_array.getString(j);
                                    Log.e("video_arrayvvvv", "" + video);

                                    if (video == "" || video == null || video == "null" || video.equalsIgnoreCase(null)
                                            || video.equalsIgnoreCase("null")) {
                                        Log.e("video_blanknull", "" + video);
                                        lv_video_details.setVisibility(View.GONE);
                                    } else {

                                        Youtube(video);
                                        getYoutubeVideoID(video);
                                        Log.e("video_article", "" + video);

                                    }
                                }

                            } else {
                                lv_video_details.setVisibility(View.GONE);
                                Log.e("video_array_else", "" + video_array);
                                video = "";
                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.placeholder);
                                requestOptions.error(R.drawable.placeholder);


                                Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                                        .load(image).into(iv_news_detail_image);

                            }

                        }
                        String article_likes = data_obj.getString("article_likes");
                        Log.e("article_likes",""+article_likes);
                        tv_tot_likes.setText(article_likes);

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

    private void CALL_NEWS_ADD_COMMENT() {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("edittext_value", "" + edt_news_detail_comment.getText().toString());
        String userid = Login_preference.getuser_id(getActivity());
        Log.e("user_id", "" + userid);
        Log.e("News", "" + news_id + " userid " + userid + " comment" + edt_news_detail_comment.getText().toString());
        Log.e("news_id_comment", "" + news_id);
        Call<ResponseBody> add_comment = api.get_news_add_comment(news_id, userid, edt_news_detail_comment.getText().toString());

        add_comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_comment", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        edt_news_detail_comment.getText().clear();

                        String message = jsonObject.getString("msg");
                        Log.e("msg", "" + message);
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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

    private void CALL_News_Detail_API() {
        //progressBar.setVisibility(View.VISIBLE);
        String userid = Login_preference.getuser_id(getActivity());

        progressBar_newsdetail.setVisibility(View.VISIBLE);
        lv_news_detail.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> news_detail;
        if (login_flag.equalsIgnoreCase("1") || login_flag == "1") {
            news_detail = api.get_news_detail(news_id, userid);
        } else {
            news_detail = api.get_news_detail(news_id, "");
        }

        news_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_news_detail", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                progressBar_newsdetail.setVisibility(View.GONE);
                lv_news_detail.setVisibility(View.VISIBLE);
                tv_posted_by.setVisibility(View.GONE);
                tv_posted.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("status_data_obj", "" + data_obj);
                        String date = data_obj.getString("news_created_at_format_day") + " " +
                                data_obj.getString("news_created_at_format_month") + " " +
                                data_obj.getString("news_created_at_format_year");

                        Navigation_activity.Check_String_NULL_Value(tv_detail_news_title, data_obj.getString("news_title"));
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_date, date);

                        Log.e("news_description", "" + data_obj.getString("news_description"));
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_description, String.valueOf(Html.fromHtml(data_obj.getString("news_description"))));
                       // Navigation_activity.Check_String_NULL_Value(tv_posted_by, data_obj.getString("news_created_by"));

                        if (data_obj.getString("news_created_by") != null && !data_obj.getString("news_created_by").isEmpty()
                                && !data_obj.getString("news_created_by").equals("null")) {

                            lv_postedby.setVisibility(View.VISIBLE);
                            Navigation_activity.Check_String_NULL_Value(tv_posted_by, data_obj.getString("news_created_by"));

                        } else {
                            Log.e("nulllll", "");
                            lv_postedby.setVisibility(View.GONE);
                        }
                        String check_if_news_liked = data_obj.getString("check_if_news_liked");
                        if (check_if_news_liked.equalsIgnoreCase("true") == true) {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_36dp));
                        } else {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }

                        /*tv_detail_news_title.setText(data_obj.getString("news_title"));
                        tv_news_detail_date.setText(data_obj.getString("news_created_at"));
                        tv_news_detail_description.setText(data_obj.getString("news_description"));
                        tv_posted_by.setText(data_obj.getString("news_created_by"));*/


                        JSONObject image_obj = data_obj.getJSONObject("news_media_urls");
                        Log.e("img_obj", "" + image_obj);
                        JSONArray imag_array = image_obj.getJSONArray("image");
                        if (imag_array != null && imag_array.isNull(0) != true) {
                            Log.e("imag_array", "" + imag_array);
                            lv_image_details.setVisibility(View.VISIBLE);
                            lv_video_details.setVisibility(View.GONE);

                            for (int j = 0; j < imag_array.length(); j++) {
                                image = imag_array.getString(j);
                                Log.e("image_news_495", "" + image);

                                if (image == "" || image == null || image == "null" || image.equalsIgnoreCase(null)
                                        || image.equalsIgnoreCase("null")) {
                                    lv_image_details.setVisibility(View.GONE);
                                } else {
                                    Log.e("image_news_main", "" + image);
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.placeholder(R.drawable.placeholder);
                                    requestOptions.error(R.drawable.placeholder);


                                    Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                                            .load(image).into(iv_news_detail_image);

                                }

                            }

                        } else {
                            Log.e("imag_array_else", "" + imag_array);
                            image = "";
                            lv_video_details.setVisibility(View.GONE);
                            lv_image_details.setVisibility(View.GONE);
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.placeholder(R.drawable.placeholder);
                            requestOptions.error(R.drawable.placeholder);


                            Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                                    .load(image).into(iv_news_detail_image);
                                                }

                        JSONArray video_array = image_obj.getJSONArray("video");
                        if (video_array != null && video_array.isNull(0) != true) {
                            Log.e("video_array_nb", "" + video_array);


                            lv_video_details.setVisibility(View.VISIBLE);
                            for (int j = 0; j < video_array.length(); j++) {
                                video = video_array.getString(j);
                                Log.e("video_arrayvvvv", "" + video);

                                if (video == "" || video == null || video == "null" || video.equalsIgnoreCase(null)
                                        || video.equalsIgnoreCase("null")) {
                                    Log.e("video_blanknull", "" + video);
                                    lv_video_details.setVisibility(View.GONE);
                                } else {

                                    Youtube(video);
                                    getYoutubeVideoID(video);
                                    Log.e("video_article", "" + video);

                                }
                            }

                        } else {
                            lv_video_details.setVisibility(View.GONE);
                            Log.e("video_array_else", "" + video_array);
                            video = "";
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.placeholder(R.drawable.placeholder);
                            requestOptions.error(R.drawable.placeholder);


                            Glide.with(getActivity()).setDefaultRequestOptions(requestOptions)
                                    .load(image).into(iv_news_detail_image);
                                                }

                        String news_likes = data_obj.getString("news_likes");
                        Log.e("news_likes",""+news_likes);
                        tv_tot_likes.setText(news_likes);

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

    private void Allocate_Memory(View view) {

        scroll_news = (NestedScrollView) view.findViewById(R.id.scroll_news);
        lv_news_parent = (RelativeLayout) view.findViewById(R.id.lv_news_parent);
        lv_image_details = (LinearLayout) view.findViewById(R.id.lv_image_details);
        lv_video_details = (LinearLayout) view.findViewById(R.id.lv_video_details);
        lv_postedby = (LinearLayout) view.findViewById(R.id.lv_postedby);
        iv_news_detail_image = (ImageView) view.findViewById(R.id.iv_news_detail_image);

        shadowView_article = (View) view.findViewById(R.id.shadowView_article);
        fab_menu_article = (FloatingActionMenu) view.findViewById(R.id.fab_menu_article);
        fab_edit_article = (FloatingActionButton) view.findViewById(R.id.fab_edit_article);
        fab_delete_article = (FloatingActionButton) view.findViewById(R.id.fab_delete_article);
        recycler_comment = (RecyclerView) view.findViewById(R.id.recycler_comment);

        tv_tot_likes = (TextView) view.findViewById(R.id.tv_tot_likes);
        tv_comment_nodata = (TextView) view.findViewById(R.id.tv_comment_nodata);
        tv_detail_comment = (TextView) view.findViewById(R.id.tv_detail_comment);
        tv_posted = (TextView) view.findViewById(R.id.tv_posted);
        tv_posted_by = (TextView) view.findViewById(R.id.tv_posted_by);
        tv_detail_news_title = (TextView) view.findViewById(R.id.tv_detail_news_title);
        tv_news_detail_date = (TextView) view.findViewById(R.id.tv_news_detail_date);
        tv_news_detail_description = (TextView) view.findViewById(R.id.tv_news_detail_description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_news_detail_description.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        tv_detail_news_add_comment = (TextView) view.findViewById(R.id.tv_detail_news_add_comment);
        tv_send_news = (TextView) view.findViewById(R.id.tv_send_news);
        iv_news_detail_like = (ImageView) view.findViewById(R.id.iv_news_detail_like);
        iv_news_detail_bookmark = (ImageView) view.findViewById(R.id.iv_news_detail_bookmark);
        iv_news_detail_share = (ImageView) view.findViewById(R.id.iv_news_detail_share);
        //iv_news_detail_image = (ImageView) view.findViewById(R.id.iv_news_detail_image);
        edt_news_detail_comment = view.findViewById(R.id.edt_news_detail_comment);
        lv_news_detail_send = view.findViewById(R.id.lv_news_detail_send);
        lv_news_detail = view.findViewById(R.id.lv_news_detail);
        progressBar_newsdetail = view.findViewById(R.id.progressBar_newsdetail);
        progressBar_bottom_recv_comment = view.findViewById(R.id.progressBar_bottom_recv_comment);


        tv_detail_comment.setTypeface(Navigation_activity.typeface);
        tv_posted.setTypeface(Navigation_activity.typeface);
        tv_posted_by.setTypeface(Navigation_activity.typeface);
        tv_detail_news_title.setTypeface(Navigation_activity.typeface);
        tv_news_detail_date.setTypeface(Navigation_activity.typeface);
        tv_news_detail_description.setTypeface(Navigation_activity.typeface);
        tv_detail_news_add_comment.setTypeface(Navigation_activity.typeface);
        edt_news_detail_comment.setTypeface(Navigation_activity.typeface);
        tv_detail_news_add_comment.setTypeface(Navigation_activity.typeface);
        tv_send_news.setTypeface(Navigation_activity.typeface);


    }

    @Override
    public void onClick(View view) {
        if (view == lv_news_detail_send) {
            if (edt_news_detail_comment.getText().length() == 0) {
                Toast.makeText(getActivity(), "Enter Comment", Toast.LENGTH_SHORT).show();
            } else {

                if (CheckNetwork.isNetworkAvailable(getActivity())) {
                    if (login_flag.equalsIgnoreCase("1") || login_flag == "1") {

                        if (news_id == "" || news_id == null || news_id == "null" || news_id.equalsIgnoreCase(null)
                                || news_id.equalsIgnoreCase("null")) {
                            CALL_ARTICLE_ADD_COMMENT_API();
                        } else {
                            CALL_NEWS_ADD_COMMENT();
                        }
                    } else {
                        pushFragment(new Login_freg(), "Cart");
                    }

                } else {
                    Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (view == iv_news_detail_like) {
            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                if (login_flag.equalsIgnoreCase("1") || login_flag == "1") {

                    if (news_id == "" || news_id == null || news_id == "null" || news_id.equalsIgnoreCase(null)
                            || news_id.equalsIgnoreCase("null")) {
                        CALL_ARTICLE_LIKE_API();
                    } else {
                        CALL_News_Like_API();
                    }

                } else {
                    String screen = "news_details";
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("screen", "" + screen);
                    bundle1.putString("article_id", article_id);
                    Fragment myFragment = new Login_freg();
                    myFragment.setArguments(bundle1);
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                            0, 0, R.anim.fade_out).replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();
                    //pushFragment(new Login_freg(), "Cart");
                }
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void CALL_ARTICLE_ADD_COMMENT_API() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("edittext_value", "" + edt_news_detail_comment.getText().toString());
        String userid = Login_preference.getuser_id(getActivity());
        Log.e("user_id", "" + userid);
        Log.e("article", "" + article_id + " userid " + userid + " comment" + edt_news_detail_comment.getText().toString());
        Log.e("article_id_comment", "" + article_id);
        Call<ResponseBody> add_comment = api.get_article_add_comment(article_id, userid, edt_news_detail_comment.getText().toString());

        add_comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_article_comment", "" + status);
                    if (status.equalsIgnoreCase("success")) {
                        String message = jsonObject.getString("msg");
                        edt_news_detail_comment.getText().clear();
                        Log.e("msg", "" + message);
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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

    private void CALL_ARTICLE_LIKE_API() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        String userid = Login_preference.getuser_id(getActivity());

        Log.e("user_id_article", "" + userid);
        Log.e("article_id_like", "" + article_id);
        Call<ResponseBody> article_like = api.get_article_like(article_id, userid);

        article_like.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_article_like", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        String checked_value = jsonObject.getString("check_if_article_liked");
                        Log.e("checked_value_article", "" + checked_value);


                        if (checked_value.equalsIgnoreCase("true") == true) {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_36dp));
                        } else {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }


                        Bundle b = new Bundle();
                        b.putString("article_id", article_id);
                        b.putString("news_id",news_id);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out);
                        Fragment newFragment = new News_Detail_freg();
                        newFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack("Newsdetail").commit();
// Start the animated transition.
                        ft.commit();

                        String message = jsonObject.getString("msg");
                        Log.e("msg", "" + message);
                        //  Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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

    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.setCustomAnimations(R.anim.fade_in,
                        0, 0, R.anim.fade_out);
                ft.replace(R.id.main_fram_layout, fragment);
                ft.addToBackStack(add_to_backstack);
                ft.commit();
            }
        }
    }

    private void CALL_News_Like_API() {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        String userid = Login_preference.getuser_id(getActivity());

        Log.e("user_id", "" + userid);
        Call<ResponseBody> news_like = api.get_news_like(news_id, userid);

        news_like.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_new_like", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        String checked_value = jsonObject.getString("check_if_news_liked");
                        Log.e("checked_value", "" + checked_value);
                        if (checked_value.equalsIgnoreCase("true") == true) {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_36dp));
                        } else {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }
                        String message = jsonObject.getString("msg");
                        Log.e("msg", "" + message);

                        Bundle b = new Bundle();
                        b.putString("article_id", article_id);
                        b.putString("news_id",news_id);
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.setCustomAnimations(R.anim.fade_in,
                                0, 0, R.anim.fade_out);
                        Fragment newFragment = new News_Detail_freg();
                        newFragment.setArguments(b);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, newFragment).addToBackStack("Newsdetail").commit();
// Start the animated transition.
                        ft.commit();
                        //  Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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

    /*@Override
    public void onPause() {
        super.onPause();
        if(YPlayer!=null) {
            YPlayer.pause();
        }
    }*/

  /*  @Override
    public void onStop() {
        super.onStop();
       if(YPlayer!=null) {
           YPlayer.pause();
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (video_array != null && video_array.isNull(0) != true) {
            if(YPlayer!=null) {
                YPlayer.pause();
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (video_array != null && video_array.isNull(0) != true) {
            if(YPlayer!=null) {
                YPlayer.pause();
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

}
