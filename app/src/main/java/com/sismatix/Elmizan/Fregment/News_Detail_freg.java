package com.sismatix.Elmizan.Fregment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonObject;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class News_Detail_freg extends Fragment implements View.OnClickListener {
    View view;
    String news_id, article_id;
    TextView tv_detail_news_title,tv_send_news, tv_news_detail_date, tv_news_detail_description, tv_detail_news_add_comment, tv_posted_by, tv_posted;
    ImageView iv_news_detail_like, iv_news_detail_bookmark, iv_news_detail_share, iv_news_detail_image;
    EditText edt_news_detail_comment;
    LinearLayout lv_news_detail_send,lv_news_detail;
    ProgressBar progressBar_newsdetail;
    public static String login_flag;
    FloatingActionMenu fab_menu_article;
    FloatingActionButton fab_edit_article;
    View shadowView_article;
    String article_inserted_by;

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
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.single_news_page));

        Allocate_Memory(view);
        Bundle bundle = this.getArguments();
        login_flag = Login_preference.getLogin_flag(getActivity());

        if (bundle != null) {

            news_id = bundle.getString("news_id");
            article_id = bundle.getString("article_id");
            Log.e("news_id_43", "" + news_id);
            Log.e("article_id_79", "" + article_id);
        }



        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            if (news_id == "" || news_id == null || news_id == "null" || news_id.equalsIgnoreCase(null)
                    || news_id.equalsIgnoreCase("null")) {

                CALL_ARTICLE_DETAIL_API();
                fab_btn_click_listner();


            } else {
                CALL_News_Detail_API();
            }

        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        lv_news_detail_send.setOnClickListener(this);
        iv_news_detail_like.setOnClickListener(this);

        return view;

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

        fab_edit_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Login_preference.getLogin_flag(getActivity()).equalsIgnoreCase("1")) {

                    if (My_Preference.get_premium_lawyer(getActivity()).equals("premium") == true) {

                        Bundle b=new Bundle();
                        b.putString("article_id",article_id);
                        Fragment myFragment = new Add_Article_Freg();
                        myFragment.setArguments(b);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                    }
                } else {
                    pushFragment(new Login_freg(), "login");
                }


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
            article_detail = api.get_article_detail(article_id,userid);
        }else{
            article_detail = api.get_article_detail(article_id,"");
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

                        String date=data_obj.getString("article_created_at_format_day")+" "+
                                data_obj.getString("article_created_at_format_month")+" "+
                                data_obj.getString("article_created_at_format_year");
                        article_inserted_by= data_obj.getString("inserted_by");

                        String userid=Login_preference.getuser_id(getActivity());
                        Log.e("userid_120",""+userid);
                        Log.e("article_inserted_by",""+article_inserted_by);

                        if(article_inserted_by.equalsIgnoreCase(userid)==true)
                        {
                            fab_menu_article.setVisibility(View.VISIBLE);
                        }else {
                            fab_menu_article.setVisibility(View.GONE);
                        }

                        Log.e("article_inserted_by", "" + article_inserted_by);
                        Navigation_activity.Check_String_NULL_Value(tv_detail_news_title, data_obj.getString("article_title"));
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_date, date);
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_description, String.valueOf(Html.fromHtml( data_obj.getString("article_description"))));

                        Navigation_activity.Check_String_NULL_Value(tv_posted_by,data_obj.getString("article_created_by"));

                        String check_if_article_liked=data_obj.getString("check_if_article_liked");
                        if (check_if_article_liked.equalsIgnoreCase("true") == true) {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_36dp));
                        } else {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }

                        /*tv_detail_news_title.setText(data_obj.getString("news_title"));
                        tv_news_detail_date.setText(data_obj.getString("news_created_at"));
                        tv_news_detail_description.setText(data_obj.getString("news_description"));
                        tv_posted_by.setText(data_obj.getString("news_created_by"));
*/
                        JSONObject image_obj = data_obj.getJSONObject("article_media_urls");
                        Log.e("img_obj", "" + image_obj);
                        JSONArray jsonArray = image_obj.getJSONArray("image");
                        Log.e("jsonArray_news", "" + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                String image = jsonArray.getString(i);
                                Glide.with(getActivity()).load(image).into(iv_news_detail_image);

                                // JSONObject vac_object = jsonArray.getJSONObject(i);
                                Log.e("image_news", "" + image);
                                // product_model.add(new Product_Category_model(vac_object.getString("name"),vac_object.getString("value")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                //   product_category_adapter.notifyItemChanged(i);
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
            news_detail = api.get_news_detail(news_id,userid);
        }else{
            news_detail = api.get_news_detail(news_id,"");
        }

            news_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_news_detail", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                progressBar_newsdetail.setVisibility(View.GONE);
                lv_news_detail.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("status_data_obj", "" + data_obj);
                        String date=data_obj.getString("news_created_at_format_day")+" "+
                                data_obj.getString("news_created_at_format_month")+" "+
                                data_obj.getString("news_created_at_format_year");

                        Navigation_activity.Check_String_NULL_Value(tv_detail_news_title, data_obj.getString("news_title"));
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_date, date);

                        Log.e("news_description", "" + data_obj.getString("news_description"));
                        Navigation_activity.Check_String_NULL_Value(tv_news_detail_description, String.valueOf(Html.fromHtml(data_obj.getString("news_description"))));
                        Navigation_activity.Check_String_NULL_Value(tv_posted_by, data_obj.getString("news_created_by"));
                        String check_if_news_liked=data_obj.getString("check_if_news_liked");
                        if (check_if_news_liked.equalsIgnoreCase("true") == true) {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_36dp));
                        } else {
                            iv_news_detail_like.setImageDrawable(getResources().getDrawable(R.drawable.like));
                        }

                        /*tv_detail_news_title.setText(data_obj.getString("news_title"));
                        tv_news_detail_date.setText(data_obj.getString("news_created_at"));
                        tv_news_detail_description.setText(data_obj.getString("news_description"));
                        tv_posted_by.setText(data_obj.getString("news_created_by"));
*/
                        JSONObject image_obj = data_obj.getJSONObject("news_media_urls");
                        Log.e("img_obj", "" + image_obj);
                        JSONArray jsonArray = image_obj.getJSONArray("image");
                        Log.e("jsonArray_news", "" + jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {
                                String image = jsonArray.getString(i);
                                Glide.with(getActivity()).load(image).into(iv_news_detail_image);

                                // JSONObject vac_object = jsonArray.getJSONObject(i);
                                Log.e("image_news", "" + image);
                                // product_model.add(new Product_Category_model(vac_object.getString("name"),vac_object.getString("value")));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                //   product_category_adapter.notifyItemChanged(i);
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

    private void Allocate_Memory(View view) {


        shadowView_article=(View)view.findViewById(R.id.shadowView_article);
        fab_menu_article = (FloatingActionMenu) view.findViewById(R.id.fab_menu_article);
        fab_edit_article = (FloatingActionButton) view.findViewById(R.id.fab_edit_article);


        tv_posted = (TextView) view.findViewById(R.id.tv_posted);
        tv_posted_by = (TextView) view.findViewById(R.id.tv_posted_by);
        tv_detail_news_title = (TextView) view.findViewById(R.id.tv_detail_news_title);
        tv_news_detail_date = (TextView) view.findViewById(R.id.tv_news_detail_date);
        tv_news_detail_description = (TextView) view.findViewById(R.id.tv_news_detail_description);
        tv_detail_news_add_comment = (TextView) view.findViewById(R.id.tv_detail_news_add_comment);
        tv_send_news = (TextView) view.findViewById(R.id.tv_send_news);
        iv_news_detail_like = (ImageView) view.findViewById(R.id.iv_news_detail_like);
        iv_news_detail_bookmark = (ImageView) view.findViewById(R.id.iv_news_detail_bookmark);
        iv_news_detail_share = (ImageView) view.findViewById(R.id.iv_news_detail_share);
        iv_news_detail_image = (ImageView) view.findViewById(R.id.iv_news_detail_image);
        edt_news_detail_comment = view.findViewById(R.id.edt_news_detail_comment);
        lv_news_detail_send = view.findViewById(R.id.lv_news_detail_send);
        lv_news_detail = view.findViewById(R.id.lv_news_detail);
        progressBar_newsdetail = view.findViewById(R.id.progressBar_newsdetail);



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
                        }else {
                            CALL_NEWS_ADD_COMMENT();
                        }
                    }else {
                        pushFragment(new Login_freg(),"Cart");
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
                    }else {
                        CALL_News_Like_API();
                    }

                }else {
                    pushFragment(new Login_freg(),"Cart");
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
        Call<ResponseBody> article_like  = api.get_article_like(article_id, userid);

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
        Call<ResponseBody> news_like  = api.get_news_like(news_id, userid);

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
}
