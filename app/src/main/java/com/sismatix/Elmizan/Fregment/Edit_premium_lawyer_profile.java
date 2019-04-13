package com.sismatix.Elmizan.Fregment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Country_model;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class Edit_premium_lawyer_profile extends Fragment implements View.OnClickListener {
    TextView tv_edit_your_img, tv_edit_user_name, tv_edit_appeal, tv_edit_personal_data, tv_edit_phone_text, tv_edit_title_text,
            tv_edit_phone, tv_edit_site, tv_edit_site_text, tv_edit_user_detail, tv_edit_email, tv_edit_email_text, tv_edit_address, tv_edit_country_text,
            tv_edit_about_lawyer, tv_edit_twitter, tv_edit_instagram, tv_edit_facebook, tv_edit_socialmedia, tv_edit_save, tv_edit_country;
    EditText edt_full_name, edt_appeal, edt_edit_user_detail, edt_phone, edt_address, edt_email, edt_site, edt_fb_url, edt_twitter_url, edt_insta_url;
    LinearLayout lv_edit_title, lv_edit_appeal, lv_edit_lawyer_info, lv_user_lawyer_detail, lv_edit_detils, lv_save_data,
            lv_edit_social_media, lv_edit_personal_detail, lv_edit_fb, lv_edit_twitter, lv_edit_insta, lv_edit_pre_profile_click, lv_edit_parent;
    View v;
    CircleImageView iv_edit_lawyer_profile;
    ImageView iv_edit_camera,iv_edit_image;
    ProgressBar progressBar_edit__premium;
    Spinner spinner_edit_country;

    public static ArrayList<String> country_name_code = new ArrayList<String>();
    public static ArrayList<String> country_name = new ArrayList<String>();
    private List<Country_model> country_model = new ArrayList<Country_model>();

    Bitmap bitmap = null;
    String path, filename, encodedImage, user_id;
    Bundle bundle;
    public static final int RequestPermissionCode = 7;
    String article_id, oldurl_pass, old_img;
    LinearLayout lv_add_article_click;
    boolean Choose_img_clicked = false;

    boolean flag_personal = true;
    boolean flag_social = true;
    boolean flag_name = true;
    boolean flag_appeal = true;
    boolean flag_detail = true;

    String fullname, short_desc, phone, email, address, site, country_id_pass, fb_url, twitter_url, inst_url, description;

    public Edit_premium_lawyer_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit_premium_lawyer_profile, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.Edit_your_profile));
        Navigation_activity.tv_nav_title.setTypeface(Navigation_activity.typeface);
        lang_arbi();
        AllocateMemory(v);
        setupUI(lv_edit_parent);
        user_id = Login_preference.getuser_id(getContext());
        Log.e("userid_95", "" + user_id);
        country_model.clear();
        country_name.clear();
        country_name_code.clear();
        old_img = "test";

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_USER_DETAIL_API();
            CALL_COUNTRY_API();

        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        lv_edit_title.setOnClickListener(this);
        lv_edit_lawyer_info.setOnClickListener(this);
        lv_edit_appeal.setOnClickListener(this);
        iv_edit_camera.setOnClickListener(this);
        lv_save_data.setOnClickListener(this);
        lv_edit_personal_detail.setOnClickListener(this);
        lv_edit_social_media.setOnClickListener(this);
        tv_edit_save.setOnClickListener(this);

        spinner_edit_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                int selected_item_position = spinner_edit_country.getSelectedItemPosition();
                country_id_pass = country_name_code.get(selected_item_position);
                // MyAddress_Preference.setCountryId(getActivity(), String.valueOf(selected_item_position));
                Log.e("countryid", "" + country_id_pass);
                String selected_country = String.valueOf(spinner_edit_country.getSelectedItem());

                // Toast.makeText(getActivity(), selected_item_position + " " + selected_country + " => " + country_id_pass, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {

            }

        });



        if (My_Preference.get_premium_lawyer(getActivity()).equalsIgnoreCase("premium") == true) {

            //iv_nav_premium_logo.setImageDrawable(Navigation_activity.this.getDrawable(R.drawable.menu_img));
            iv_edit_image.setImageResource(R.drawable.menu_img);
        } else {

            //iv_nav_premium_logo.setImageDrawable(Navigation_activity.this.getDrawable(R.drawable.grey_perimimum));
            iv_edit_image.setImageResource(R.drawable.grey_perimimum);
        }


        return v;
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }

  /*  public boolean isValid_facebook_url(String fb) {
        String ePattern = "(?:(?:http|https):\\/\\/)?(?:www.)?facebook.com\\/(?:(?:\\w)*#!\\/)?(?:pages\\/)?(?:[?\\w\\-]*\\/)?(?:profile.php\\?id=(?=\\d.*))?([\\w\\-]*)?";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(fb);
        Log.e("facebook validation == ",""+m.matches());
        return m.matches();
    }*/
  public boolean isValid_facebook_url(String fb) {
      String ePattern = "https?:\\/\\/(www\\.)?facebook\\.com\\/([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?)";
      java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
      java.util.regex.Matcher m = p.matcher(fb);
      Log.e("facebook validation == ",""+m.matches());
      return m.matches();
  }

    public boolean isValid_twitter_url(String twitter) {
        //String ePattern = "http(?:s)?:\\/\\/(?:www\\.)?twitter\\.com\\/([a-zA-Z0-9_]+)";
        String ePattern = "https?:\\/\\/(www\\.)?twitter\\.com\\/([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(twitter);
        Log.e("twitter_validation == ",""+m.matches());

        return m.matches();
    }

    public boolean isValid_instagram_url(String instagram) {
        String ePattern = "https?:\\/\\/(www\\.)?instagram\\.com\\/([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(instagram);
        Log.e("m_array == ",""+m);
        Log.e("insta_validation == ",""+m.matches());

        return m.matches();
    }

    private void CALL_USER_DETAIL_API() {
        Log.e("userid_108", "" + user_id);
        progressBar_edit__premium.setVisibility(View.VISIBLE);
        lv_edit_pre_profile_click.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_detail = api.get_user_detail(user_id);

        user_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);

                progressBar_edit__premium.setVisibility(View.GONE);
                lv_edit_pre_profile_click.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("status_data_obj", "" + data_obj);

                        fullname = data_obj.getString("user_fullname");
                        short_desc = data_obj.getString("user_description");
                        email = data_obj.getString("user_email");
                        address = data_obj.getString("user_address");
                        site = data_obj.getString("user_website");
                        country_id_pass = data_obj.getString("user_country_id");
                        fb_url = data_obj.getString("user_facebook");
                        twitter_url = data_obj.getString("user_twitter");
                        inst_url = data_obj.getString("user_instagram");
                        description = data_obj.getString("user_about");
                        phone = data_obj.getString("user_phone");

                        old_img = data_obj.getString("user_avatar");
                        Log.e("olddd_img_202", "" + old_img);
                        Log.e("country_id_199", "" + data_obj.getString("user_country_id"));

                        Login_preference.setuser_countryid(getActivity(), data_obj.getString("user_country_id"));

                        Check_String_NULL_Value(tv_edit_user_name, data_obj.getString("user_fullname"), "Enter user Name");
                        Check_String_NULL_Value(tv_edit_address, data_obj.getString("user_address"), "Enter Address");
                        Check_String_NULL_Value(tv_edit_phone, data_obj.getString("user_phone"), "Enter Phone no");
                        Check_String_NULL_Value(tv_edit_email, data_obj.getString("user_email"), "Enter Email");
                        Check_String_NULL_Value(tv_edit_site, data_obj.getString("user_website"), "Enter Website");
                        Check_String_NULL_Value(tv_edit_appeal, data_obj.getString("user_description"), "Enter description");
                        Check_String_NULL_Value(tv_edit_user_detail, data_obj.getString("user_about"), "Enter user Detail");
                       // Check_String_NULL_Value(tv_edit_facebook, data_obj.getString("user_facebook"), "Enter facebook link");
                        //Check_String_NULL_Value(tv_edit_twitter, data_obj.getString("user_twitter"), "Enter twitter link");
                        //Check_String_NULL_Value(tv_edit_instagram, data_obj.getString("user_instagram"), "Enter instagram link");

                        tv_edit_facebook.setText(data_obj.getString("user_facebook"));
                        tv_edit_twitter.setText(data_obj.getString("user_twitter"));
                        tv_edit_instagram.setText(data_obj.getString("user_instagram"));
                        Check_String_NULL_Value(  Navigation_activity.tv_nav_appal, data_obj.getString("user_description"), "");

                       // Login_preference.setuser_short_desc(getActivity(), data_obj.getString("user_description"));
                       /// Navigation_activity.tv_nav_appal.setText( data_obj.getString("user_description"));
                        Navigation_activity.Check_Editext_NULL_Value(edt_edit_user_detail, description);
                        Navigation_activity.Check_Editext_NULL_Value(edt_edit_user_detail, description);
                       /* Navigation_activity.Check_Editext_NULL_Value(edt_fb_url, fb_url);
                        Navigation_activity.Check_Editext_NULL_Value(edt_twitter_url, twitter_url);
                        Navigation_activity.Check_Editext_NULL_Value(edt_insta_url, inst_url);
*/
                        edt_fb_url.setText(fb_url);
                        edt_twitter_url.setText(twitter_url);
                        edt_insta_url.setText(inst_url);

                        Navigation_activity.Check_Editext_NULL_Value(edt_full_name, fullname);
                        Navigation_activity.Check_Editext_NULL_Value(edt_appeal, short_desc);
                        Navigation_activity.Check_Editext_NULL_Value(edt_phone, phone);
                        Navigation_activity.Check_Editext_NULL_Value(edt_email, email);
                        Navigation_activity.Check_Editext_NULL_Value(edt_site, site);
                        Navigation_activity.Check_Editext_NULL_Value(edt_address, address);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.app_icon);
                        requestOptions.error(R.drawable.app_icon);


                        Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(data_obj.getString("user_avatar_url")).into(iv_edit_lawyer_profile);


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

    public static void Check_String_NULL_Value(TextView textview, String text, String hint) {


        if (text == "" || text == null || text == "null" || text.equalsIgnoreCase(null)
                || text.equalsIgnoreCase("null") || text.equalsIgnoreCase("") == true) {
            //textview.setHint(hint);
            Log.e("hinteee", "" + hint);
            textview.setText(hint);
        } else {

            textview.setText(Html.fromHtml(Navigation_activity.Convert_String_First_Letter(text)));
        }

    }

    @Override
    public void onClick(View view) {
        if (view == lv_edit_title) {


            if (lv_edit_title.isClickable() == true && flag_name == true) {

                Log.e("fullname_22", "" + tv_edit_user_name.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_full_name, tv_edit_user_name.getText().toString());
                tv_edit_user_name.setVisibility(View.GONE);
                edt_full_name.setVisibility(View.VISIBLE);

                flag_name = false;
            } else if (lv_edit_title.isClickable() == true && flag_name == false) {
                tv_edit_user_name.setVisibility(View.VISIBLE);
                edt_full_name.setVisibility(View.GONE);
                flag_name = true;

            }


        } else if (view == lv_edit_appeal) {

            if (lv_edit_appeal.isClickable() == true && flag_appeal == true) {

                edt_appeal.setVisibility(View.VISIBLE);
                tv_edit_appeal.setVisibility(View.GONE);

                Log.e("short_desc_22", "" + tv_edit_appeal.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_appeal, tv_edit_appeal.getText().toString());

                flag_appeal = false;

            } else if (lv_edit_appeal.isClickable() == true && flag_appeal == false) {
                edt_appeal.setVisibility(View.GONE);
                tv_edit_appeal.setVisibility(View.VISIBLE);

                flag_appeal=true;
            }


        } else if (view == lv_edit_lawyer_info)

        {
            if (lv_edit_lawyer_info.isClickable() == true && flag_detail == true) {
                lv_user_lawyer_detail.setVisibility(View.GONE);
                lv_edit_detils.setVisibility(View.VISIBLE);

                Log.e("description_22", "" + tv_edit_user_detail.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_edit_user_detail, tv_edit_user_detail.getText().toString());

                flag_detail = false;

            } else if (lv_edit_lawyer_info.isClickable() == true && flag_detail == false) {
                lv_user_lawyer_detail.setVisibility(View.VISIBLE);
                lv_edit_detils.setVisibility(View.GONE);

                flag_detail=true;
            }


        } else if (view == iv_edit_camera)

        {
            if (CheckingPermissionIsEnabledOrNot()) {
                //  Toast.makeText(getActivity(), "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
            } else {
                //Calling method to enable permission.
                RequestMultiplePermission();
            }
            selectImage();
        } else if (view == lv_edit_personal_detail)

        {
            if (lv_edit_personal_detail.isClickable() == true && flag_personal == true) {

                tv_edit_site.setVisibility(View.GONE);
                tv_edit_phone.setVisibility(View.GONE);
                tv_edit_address.setVisibility(View.GONE);
                tv_edit_country.setVisibility(View.GONE);
                tv_edit_email.setVisibility(View.GONE);

                edt_site.setVisibility(View.VISIBLE);
                edt_phone.setVisibility(View.VISIBLE);
                edt_address.setVisibility(View.VISIBLE);
                spinner_edit_country.setVisibility(View.VISIBLE);
                edt_email.setVisibility(View.VISIBLE);

                Log.e("email_22", "" + tv_edit_email.getText().toString());
                Log.e("address_22", "" + tv_edit_address.getText().toString());
                Log.e("phone_22", "" + tv_edit_phone.getText().toString());
                Log.e("site_22", "" + tv_edit_site.getText().toString());


                Navigation_activity.Check_Editext_NULL_Value(edt_email, tv_edit_email.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_address, tv_edit_address.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_phone, tv_edit_phone.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_site, tv_edit_site.getText().toString());

                flag_personal = false;

            } else if (lv_edit_personal_detail.isClickable() == true && flag_personal == false) {

                tv_edit_site.setVisibility(View.VISIBLE);
                tv_edit_phone.setVisibility(View.VISIBLE);
                tv_edit_address.setVisibility(View.VISIBLE);
                tv_edit_country.setVisibility(View.VISIBLE);
                tv_edit_email.setVisibility(View.VISIBLE);

                edt_site.setVisibility(View.GONE);
                edt_phone.setVisibility(View.GONE);
                edt_address.setVisibility(View.GONE);
                spinner_edit_country.setVisibility(View.VISIBLE);
                edt_email.setVisibility(View.GONE);

                flag_personal=true;
            }


        } else if (view == lv_edit_social_media)

        {

            if (lv_edit_social_media.isClickable() == true && flag_social == true) {
                tv_edit_facebook.setVisibility(View.GONE);
                tv_edit_instagram.setVisibility(View.GONE);
                tv_edit_twitter.setVisibility(View.GONE);

                lv_edit_fb.setVisibility(View.VISIBLE);
                lv_edit_insta.setVisibility(View.VISIBLE);
                lv_edit_twitter.setVisibility(View.VISIBLE);

                Log.e("fb_url_22", "" + tv_edit_facebook.getText().toString());
                Log.e("twitter_url_22", "" + tv_edit_twitter.getText().toString());
                Log.e("inst_url_22", "" + tv_edit_instagram.getText().toString());

                edt_fb_url.setText(tv_edit_facebook.getText().toString());
                edt_twitter_url.setText(tv_edit_twitter.getText().toString());
                edt_insta_url.setText(tv_edit_instagram.getText().toString());

               /* Navigation_activity.Check_Editext_NULL_Value(edt_fb_url, tv_edit_facebook.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_twitter_url, tv_edit_twitter.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_insta_url, tv_edit_instagram.getText().toString());
*/
                flag_social = false;

            }else if (lv_edit_social_media.isClickable() == true && flag_social == false) {

                tv_edit_facebook.setVisibility(View.VISIBLE);
                tv_edit_instagram.setVisibility(View.VISIBLE);
                tv_edit_twitter.setVisibility(View.VISIBLE);

                lv_edit_fb.setVisibility(View.GONE);
                lv_edit_insta.setVisibility(View.GONE);
                lv_edit_twitter.setVisibility(View.GONE);

                flag_social=true;

            }


        } else if (view == tv_edit_save || view==lv_save_data)

        {
            Log.e("click_251", "");
            CheckValidation();

        }


    }

    private void CALL_COUNTRY_API() {
        //  progressBar_home.setVisibility(View.VISIBLE);
        country_model.clear();
        country_name.clear();
        country_name_code.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> country_list = api.get_country_list(ApiClient.user_status);

        country_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_country", "" + response.body().toString());
                //   progressBar_home.setVisibility(View.GONE);

                country_name.add(getResources().getString(R.string.Select));
                country_name_code.add("0");

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_country", "" + status);
                    String message = jsonObject.getString("msg");
                    Log.e("message", "" + message);
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray data_array = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name", "" + news_object.getString("country_id"));
                                country_model.add(new Country_model(news_object.getString("country_id"),
                                        news_object.getString("country_name"),
                                        news_object.getString("country_status"),
                                        news_object.getString("country_image_url")
                                ));

                                country_name.add(news_object.getString("country_name"));
                                country_name_code.add(news_object.getString("country_id"));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                            }

                        }
                        spinner_edit_country.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, country_name));
                        Log.e("shipping_167", "" + Login_preference.getuser_countryid(getActivity()));
                        int selected_spinner_pos = country_name_code.indexOf(Login_preference.getuser_countryid(getActivity()));
                        Log.e("pos_344", "" + selected_spinner_pos);
                        spinner_edit_country.setSelection(selected_spinner_pos, true);

                    } else if (status.equalsIgnoreCase("error")) {
                    }

                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void CheckValidation() {

        fullname = edt_full_name.getText().toString();
        short_desc = edt_appeal.getText().toString();
        phone = edt_phone.getText().toString();
        email = edt_email.getText().toString();
        address = edt_address.getText().toString();
        site = edt_site.getText().toString();
        // country_id_pass = country_id_pass;
        fb_url = edt_fb_url.getText().toString();
        twitter_url = edt_twitter_url.getText().toString();
        inst_url = edt_insta_url.getText().toString();
        description = edt_edit_user_detail.getText().toString();

        Log.e("click_290", "" + fullname);
        Log.e("click_290", "" + short_desc);
        Log.e("click_290", "" + phone);
        Log.e("click_290", "" + address);
        Log.e("click_290", "" + site);
        Log.e("click_290", "" + fb_url);
        Log.e("click_290", "" + inst_url);
        Log.e("click_290", "" + twitter_url);
        Log.e("click_290", "" + description);

        Boolean validation_ok = true;
        if (edt_full_name.getVisibility() == View.VISIBLE) {
            Log.e("click_291", "" + fullname);
            if (fullname.length() == 0) {
                Toast.makeText(getContext(), "Please enter your Full name", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }

        if (edt_phone.getVisibility() == View.VISIBLE) {
            Log.e("click_292", "" + fullname);
            if (phone.length() == 0) {
                Toast.makeText(getContext(), "Please enter your Mobile No", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            } else if (phone.length() < 9 || phone.length() > 13) {
                Toast.makeText(getContext(), "Please enter valid Mobile no.", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }

        if (edt_email.getVisibility() == View.VISIBLE) {
            Log.e("click_293", "" + fullname);
            if (email.length() == 0) {
                Toast.makeText(getContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            } else if (isValidEmailAddress(email) == false) {
                Toast.makeText(getContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }

        if (spinner_edit_country.getSelectedItem().toString().trim().equals("Select") == true) {
            Log.e("click_294", "" + fullname);
            Toast.makeText(getActivity(), "Please Select Country", Toast.LENGTH_SHORT).show();
            validation_ok = false;
        }

        if (lv_edit_fb.getVisibility() == View.VISIBLE && edt_fb_url.getText().length() > 0) {
            if (isValid_facebook_url(edt_fb_url.getText().toString()) == false) {
                Log.e("fb_url_598", "" + edt_fb_url.getText().toString());
                Toast.makeText(getContext(), "Please enter valid Facebook Url", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }


        if (lv_edit_twitter.getVisibility() == View.VISIBLE && edt_twitter_url.getText().length() > 0) {
            if (isValid_twitter_url(edt_twitter_url.getText().toString()) == false) {
                Log.e("twitterurl_598", "" + edt_twitter_url.getText().toString());
                Toast.makeText(getContext(), "Please enter valid Twitter Url", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }
        if (lv_edit_insta.getVisibility() == View.VISIBLE && edt_insta_url.getText().length() > 0) {
            if (isValid_instagram_url(edt_insta_url.getText().toString()) == false) {
                Log.e("instagram_598", "" + edt_insta_url.getText().toString());
                Toast.makeText(getContext(), "Please enter valid Instagram Url", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }

        if (iv_edit_lawyer_profile.getDrawable() == null) {
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            validation_ok = false;
        } else if (path == "" || path == null || path == "null" || path.equalsIgnoreCase(null)
                || path.equalsIgnoreCase("null")) {

            if (old_img.equalsIgnoreCase("test") == true) {
                Log.e("old_image", "" + old_img);
                Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
                //   Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }

        if (validation_ok == true) {
            Log.e("click_295", "" + fullname);
            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                Log.e("click_366", "" + description);
                CALL_USER_UPDATE_API(fullname, short_desc, phone, email, address, country_id_pass, site, fb_url, inst_url, twitter_url, description, user_id, path);
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

       /* edt_email.setText(email);
        //edt_address.setText(address);
        edt_phone.setText(phone);
        edt_appeal.setText(short_desc);
        edt_site.setText(site);
        edt_edit_user_detail.setText(description);
        edt_fb_url.setText(fb_url);
        edt_twitter_url.setText(twitter_url);
        edt_insta_url.setText(inst_url);
        edt_full_name.setText(fullname);*/

    }

    private void CALL_USER_UPDATE_API(String fullname, String short_desc, String phone, String email, String address, String country_id_pass, String site, String fb_url, String inst_url, String twitter_url, String about_lawyer, String user_id, String path) {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Log.e("user_id", "" + user_id);
        Log.e("fullname", "" + fullname);
        Log.e("short_desc", "" + short_desc);
        Log.e("phone", "" + phone);
        Log.e("email", "" + email);
        Log.e("address", "" + address);
        Log.e("country_id_pass", "" + country_id_pass);
        Log.e("site", "" + site);
        Log.e("fb_url", "" + fb_url);
        Log.e("inst_url", "" + inst_url);
        Log.e("twitter_url", "" + twitter_url);
        Log.e("about_lawyer", "" + about_lawyer);
        Log.e("about_lawyer", "" + about_lawyer);
        Log.e("path_584", "" + path);

        RequestBody fullname_pass = RequestBody.create(MediaType.parse("text/plain"), fullname);
        RequestBody short_desc_pass = RequestBody.create(MediaType.parse("text/plain"), short_desc);
        RequestBody phone_pass = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody address_pass = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody email_pass = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody country_id_passs = RequestBody.create(MediaType.parse("text/plain"), country_id_pass);
        RequestBody site_pass = RequestBody.create(MediaType.parse("text/plain"), site);
        RequestBody fb_url_pass = RequestBody.create(MediaType.parse("text/plain"), fb_url);
        RequestBody inst_url_pass = RequestBody.create(MediaType.parse("text/plain"), inst_url);
        RequestBody twitter_url_pass = RequestBody.create(MediaType.parse("text/plain"), twitter_url);
        RequestBody about_lawyer_pass = RequestBody.create(MediaType.parse("text/plain"), about_lawyer);
        RequestBody user_id_pass = RequestBody.create(MediaType.parse("text/plain"), user_id);

        Call<ResponseBody> Update_User_detail = null;

        if (path == "" || path == null || path == "null" || path.equalsIgnoreCase(null)
                || path.equalsIgnoreCase("null")) {
            Log.e("EDit_old_image", "" + path);
            RequestBody old_img_url = RequestBody.create(MediaType.parse("text/plain"), old_img);

            RequestBody blank = RequestBody.create(MediaType.parse("text/plain"), "");
            Update_User_detail = api.CALL_User_update_img_blank
                    (user_id_pass, fullname_pass,
                            short_desc_pass, about_lawyer_pass,
                            email_pass, phone_pass,
                            address_pass, site_pass,
                            country_id_passs, inst_url_pass,
                            twitter_url_pass, fb_url_pass,
                            old_img_url, blank);
        } else {
            RequestBody old_img_url = RequestBody.create(MediaType.parse("text/plain"), "");
            File file = new File(path);
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("user_avatar", file.getName(), fileReqBody);
            Log.e("new_part_624", "" + part);

            Update_User_detail = api.CALL_User_update
                    (user_id_pass,
                            fullname_pass, short_desc_pass, about_lawyer_pass,
                            email_pass, phone_pass,
                            address_pass, site_pass,
                            country_id_passs, inst_url_pass,
                            twitter_url_pass, fb_url_pass,
                            old_img_url, part);

        }

        Update_User_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    Log.e("response_333", "" + response);
                    String status = jsonObject.getString("status");
                    Log.e("User_update_status", "" + status);
                    String message = jsonObject.getString("msg");

                    if (status.equalsIgnoreCase("success")) {
                        // // Log.e("msg_article", "" + message);
                        Bundle b = new Bundle();
                        b.putString("user_id", Login_preference.getuser_id(getActivity()));
                        Fragment myFragment = new Edit_premium_lawyer_profile();
                        myFragment.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        String media_upload_error = jsonObject.getString("media_upload_error");
                        Toast.makeText(getActivity(), "" + media_upload_error, Toast.LENGTH_SHORT).show();

                        Log.e("add_article_status_else", "" + status);
                        Log.e("media_upload_error", "" + media_upload_error);
                    }

                } catch (Exception e) {
                    Log.e("exception", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void AllocateMemory(View v) {

        iv_edit_image = (ImageView) v.findViewById(R.id.iv_edit_image);
        iv_edit_camera = (ImageView) v.findViewById(R.id.iv_edit_camera);
        spinner_edit_country = (Spinner) v.findViewById(R.id.spinner_edit_country);

        tv_edit_your_img = (TextView) v.findViewById(R.id.tv_edit_your_img);
        tv_edit_user_name = (TextView) v.findViewById(R.id.tv_edit_user_name);
        tv_edit_appeal = (TextView) v.findViewById(R.id.tv_edit_appeal);
        tv_edit_personal_data = (TextView) v.findViewById(R.id.tv_edit_personal_data);
        tv_edit_phone_text = (TextView) v.findViewById(R.id.tv_edit_phone_text);
        tv_edit_title_text = (TextView) v.findViewById(R.id.tv_edit_title_text);
        tv_edit_phone = (TextView) v.findViewById(R.id.tv_edit_phone);
        tv_edit_site = (TextView) v.findViewById(R.id.tv_edit_site);
        tv_edit_site_text = (TextView) v.findViewById(R.id.tv_edit_site_text);
        tv_edit_user_detail = (TextView) v.findViewById(R.id.tv_edit_user_detail);
        tv_edit_email = (TextView) v.findViewById(R.id.tv_edit_email);
        tv_edit_email_text = (TextView) v.findViewById(R.id.tv_edit_email_text);
        tv_edit_about_lawyer = (TextView) v.findViewById(R.id.tv_edit_about_lawyer);
        tv_edit_twitter = (TextView) v.findViewById(R.id.tv_edit_twitter);
        tv_edit_instagram = (TextView) v.findViewById(R.id.tv_edit_instagram);
        tv_edit_facebook = (TextView) v.findViewById(R.id.tv_edit_facebook);
        tv_edit_socialmedia = (TextView) v.findViewById(R.id.tv_edit_socialmedia);
        tv_edit_country = (TextView) v.findViewById(R.id.tv_edit_pre_country);
        tv_edit_save = (TextView) v.findViewById(R.id.tv_edit_save);
        tv_edit_country_text = (TextView) v.findViewById(R.id.tv_edit_countryyyy_text);
        tv_edit_address = (TextView) v.findViewById(R.id.tv_edit_address);

        tv_edit_country_text.setText(getResources().getString(R.string.country) + " :");


        progressBar_edit__premium = (ProgressBar) v.findViewById(R.id.progressBar_edit__premium);
        edt_full_name = (EditText) v.findViewById(R.id.edt_title_name);
        edt_appeal = (EditText) v.findViewById(R.id.edt_appeal);
        edt_edit_user_detail = (EditText) v.findViewById(R.id.edt_edit_user_detail);
        edt_phone = (EditText) v.findViewById(R.id.edt_phone);
        edt_address = (EditText) v.findViewById(R.id.edt_address);
        edt_email = (EditText) v.findViewById(R.id.edt_email);
        edt_site = (EditText) v.findViewById(R.id.edt_site);
        edt_fb_url = (EditText) v.findViewById(R.id.edt_fb_url);
        edt_twitter_url = (EditText) v.findViewById(R.id.edt_twitter_url);
        edt_insta_url = (EditText) v.findViewById(R.id.edt_insta_url);

        lv_edit_parent = (LinearLayout) v.findViewById(R.id.lv_edit_parent);
        lv_edit_lawyer_info = (LinearLayout) v.findViewById(R.id.lv_edit_lawyer_info);
        lv_edit_title = (LinearLayout) v.findViewById(R.id.lv_edit_title);
        lv_edit_appeal = (LinearLayout) v.findViewById(R.id.lv_edit_appeal);
        lv_user_lawyer_detail = (LinearLayout) v.findViewById(R.id.lv_user_lawyer_detail);
        lv_edit_detils = (LinearLayout) v.findViewById(R.id.lv_edit_detils);
        lv_save_data = (LinearLayout) v.findViewById(R.id.lv_save_data);
        lv_edit_social_media = (LinearLayout) v.findViewById(R.id.lv_edit_social_media);
        lv_edit_personal_detail = (LinearLayout) v.findViewById(R.id.lv_edit_personal_detail);
        lv_edit_fb = (LinearLayout) v.findViewById(R.id.lv_edit_fb);
        lv_edit_twitter = (LinearLayout) v.findViewById(R.id.lv_edit_twitter);
        lv_edit_insta = (LinearLayout) v.findViewById(R.id.lv_edit_insta);
        lv_edit_pre_profile_click = (LinearLayout) v.findViewById(R.id.lv_edit_pre_profile_click);

        iv_edit_lawyer_profile = (CircleImageView) v.findViewById(R.id.iv_edit_lawyer_profile);


        tv_edit_country.setTypeface(Navigation_activity.typeface);
        tv_edit_country_text.setTypeface(Navigation_activity.typeface);
        tv_edit_address.setTypeface(Navigation_activity.typeface);

        tv_edit_user_name.setTypeface(Navigation_activity.typeface);
        tv_edit_personal_data.setTypeface(Navigation_activity.typeface);
        tv_edit_phone_text.setTypeface(Navigation_activity.typeface);
        tv_edit_title_text.setTypeface(Navigation_activity.typeface);
        tv_edit_phone.setTypeface(Navigation_activity.typeface);
        tv_edit_site.setTypeface(Navigation_activity.typeface);
        tv_edit_site_text.setTypeface(Navigation_activity.typeface);
        tv_edit_user_detail.setTypeface(Navigation_activity.typeface);
        tv_edit_email.setTypeface(Navigation_activity.typeface);
        tv_edit_email_text.setTypeface(Navigation_activity.typeface);
        tv_edit_about_lawyer.setTypeface(Navigation_activity.typeface);
        tv_edit_instagram.setTypeface(Navigation_activity.typeface);
        tv_edit_facebook.setTypeface(Navigation_activity.typeface);
        tv_edit_socialmedia.setTypeface(Navigation_activity.typeface);
        edt_full_name.setTypeface(Navigation_activity.typeface);
        edt_appeal.setTypeface(Navigation_activity.typeface);
         tv_edit_your_img.setTypeface(Navigation_activity.Cairo_Regular);
        tv_edit_appeal.setTypeface(Navigation_activity.Cairo_Regular);
        tv_edit_twitter.setTypeface(Navigation_activity.typeface);
        tv_edit_save.setTypeface(Navigation_activity.typeface);
  }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
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

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                //onCaptureImageResult(data);
                Log.e("DATATATATAT===========", "==" + data.getExtras().get("data"));
                bitmap = (Bitmap) data.getExtras().get("data");
                // encodedImage = imgBitMapToString(bitmap);
                Log.e("camera_imagess", "" + bitmap);

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                circularBitmapDrawable.setCircular(true);
                iv_edit_lawyer_profile.setImageBitmap(bitmap);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getActivity(), bitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                path = String.valueOf(finalFile);
                filename = path.substring(path.lastIndexOf("/") + 1);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(imagePath));
                Log.e("path of image from gallery..*************...", imagePath);

                File imagefile = new File(imagePath);
                path = String.valueOf(imagefile);
                Log.e("pathhhhhhh_profilepic", "" + path);
                filename = path.substring(path.lastIndexOf("/") + 1);
                Log.e("pat_gallery_filenm", "" + filename);

                BitmapDrawable d = new BitmapDrawable(getResources(), imagefile.getAbsolutePath());
                iv_edit_lawyer_profile.setImageDrawable(d);

              /*  RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), imagefile.getAbsolutePath());
                circularBitmapDrawable.setCircular(true);
*/
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("PathURLLLLLLLLLLLL", "" + Uri.parse(path));
        return Uri.parse(path);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Remove Photo",
                "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (items[item].equals("Remove Photo")) {

                    bitmap = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.drawable.my_profile);
                    Log.e("bitmap_355", "" + bitmap);
                    iv_edit_lawyer_profile.setImageBitmap(bitmap);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    path = String.valueOf(finalFile);
                    Log.e("remove_pic_path", "" + path);

                    filename = path.substring(path.lastIndexOf("/") + 1);


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //Permission function starts from here
    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,

                }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalstoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalstoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && ReadExternalstoragePermission && WriteExternalstoragePermission) {

                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    // Checking permission is enabled or not using function starts from here.
    public boolean CheckingPermissionIsEnabledOrNot() {

        int CAMERA_PermissionResult = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        int READ_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);


        return CAMERA_PermissionResult == PackageManager.PERMISSION_GRANTED &&
                READ_EXTERNAL_STORAGE_PermissionResult == PackageManager.PERMISSION_GRANTED &&
                WRITE_EXTERNAL_STORAGE_PermissionResult == PackageManager.PERMISSION_GRANTED;

    }




}
