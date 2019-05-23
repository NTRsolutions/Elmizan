package com.sismatix.Elmizan.Fregment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sismatix.Elmizan.Activity.ChangePWpop;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Preference.Login_preference;
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
public class Edit_basic_lawyer_freg extends Fragment implements View.OnClickListener {

    View v;
    ProgressBar progressBar_edit_basic;
    ImageView iv_edit_basic_lawyer;
    LinearLayout lv_edit_basi,lv_basic_name,lv_basic_email,lv_save_data_basic,lv_basic_parent,lv_edit_paasword;
    TextView tv_basic_email,tv_basic_name,tv_basic_change_password,tv_edit_save_basic,tv_dot_basic;
    EditText edt_basic_email,edt_basic_name,edt_change_password_basic;
    boolean flag_name = true;
    boolean flag_email = true;
    boolean flag_password = true;
    String user_id,fullname,email,change_password;

    public Edit_basic_lawyer_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_edit_basic_lawyer_freg, container, false);
        Allocatememory(v);
        hideSoftKeyboard(getActivity());
        setupUI(lv_basic_parent);
        user_id = Login_preference.getuser_id(getContext());

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_USER_DETAIL_API();

        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        lv_basic_email.setOnClickListener(this);
        lv_basic_name.setOnClickListener(this);
        lv_save_data_basic.setOnClickListener(this);
        lv_edit_paasword.setOnClickListener(this);

        return v;
    }

    private void CALL_USER_DETAIL_API() {
        progressBar_edit_basic.setVisibility(View.VISIBLE);
        lv_edit_basi.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> user_detail = api.get_user_detail(user_id);

        user_detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                //  progressBar.setVisibility(View.GONE);
                progressBar_edit_basic.setVisibility(View.GONE);
                lv_edit_basi.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_news_detail", "" + status);
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject data_obj = jsonObject.getJSONObject("data");
                        Log.e("status_data_obj", "" + data_obj);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.my_profile);
                        requestOptions.error(R.drawable.my_profile);

                        Log.e("image_310",""+data_obj.getString("user_avatar_url"));

                        Login_preference.setuser_profile(getActivity(), data_obj.getString("user_avatar_url"));
                        Login_preference.setuser_name(getActivity(), data_obj.getString("user_fullname"));
                        //  My_Preference.setCountry_name(getActivity(), data_obj.getString("user_country_id"));

                        //Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(data_obj.getString("user_avatar_url")).into(iv_edit_lawyer_profile);
                        Glide.with(getActivity()).load(data_obj.getString("user_avatar_url")).into(Navigation_activity.iv_profile_image);

                        Navigation_activity.tv_nav_user_name.setText(data_obj.getString("user_fullname"));

                        fullname = data_obj.getString("user_fullname");
                        email = data_obj.getString("user_email");

                        Navigation_activity.Check_Editext_NULL_Value(edt_basic_name, fullname);
                        Navigation_activity.Check_Editext_NULL_Value(edt_basic_email, email);


                        Log.e("edt_full_name", "" + edt_basic_name.getText().toString());
                        Log.e("edt_full_name", "" + edt_basic_email.getText().toString());

                        Login_preference.setuser_countryid(getActivity(), data_obj.getString("user_country_id"));

                        Check_String_NULL_Value(tv_basic_name, data_obj.getString("user_fullname"), "Enter user Name");



                        if (data_obj.getString("user_email") == "" || data_obj.getString("user_email") == null || data_obj.getString("user_email") == "null" || data_obj.getString("user_email").equalsIgnoreCase(null)
                                || data_obj.getString("user_email").equalsIgnoreCase("null") || data_obj.getString("user_email").equalsIgnoreCase("") == true) {
                            //textview.setHint(hint);
                            //  Log.e("hinteee", "" + hint);
                            tv_basic_email.setText("");
                        } else {
                            tv_basic_email.setText(data_obj.getString("user_email"));
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
    public static void Check_String_NULL_Value(TextView textview, String text, String hint) {
        if (text == "" || text == null || text == "null" || text.equalsIgnoreCase(null)
                || text.equalsIgnoreCase("null") || text.equalsIgnoreCase("") == true) {
            //textview.setHint(hint);
            //  Log.e("hinteee", "" + hint);
            textview.setText("");
        } else {
            textview.setText(Html.fromHtml(Navigation_activity.Convert_String_First_Letter(text)));
        }
    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void Allocatememory(View v) {

        progressBar_edit_basic=(ProgressBar)v.findViewById(R.id.progressBar_edit_basic);
        edt_basic_email=(EditText)v.findViewById(R.id.edt_basic_email);
        edt_basic_name=(EditText)v.findViewById(R.id.edt_basic_name);
        edt_change_password_basic=(EditText)v.findViewById(R.id.edt_change_password_basic);

        tv_dot_basic=(TextView)v.findViewById(R.id.tv_dot_basic);
        tv_basic_email=(TextView)v.findViewById(R.id.tv_basic_email);
        tv_basic_name=(TextView)v.findViewById(R.id.tv_basic_name);
        tv_basic_change_password=(TextView)v.findViewById(R.id.tv_basic_change_password);
        tv_edit_save_basic=(TextView)v.findViewById(R.id.tv_edit_save_basic);


        lv_edit_paasword=(LinearLayout)v.findViewById(R.id.lv_edit_paasword);
        lv_basic_parent=(LinearLayout)v.findViewById(R.id.lv_basic_parent);
        lv_edit_basi=(LinearLayout)v.findViewById(R.id.lv_edit_basi);
        lv_basic_name=(LinearLayout)v.findViewById(R.id.lv_basic_name);
        lv_basic_email=(LinearLayout)v.findViewById(R.id.lv_basic_email);
        lv_save_data_basic=(LinearLayout)v.findViewById(R.id.lv_save_data_basic);
        iv_edit_basic_lawyer=(ImageView)v.findViewById(R.id.iv_edit_basic_lawyer);

        tv_basic_change_password.setTypeface(Navigation_activity.typeface);
        tv_basic_name.setTypeface(Navigation_activity.typeface);
        tv_basic_email.setTypeface(Navigation_activity.typeface);
        tv_edit_save_basic.setTypeface(Navigation_activity.typeface);
        edt_basic_name.setTypeface(Navigation_activity.typeface);
        edt_basic_email.setTypeface(Navigation_activity.typeface);
        edt_change_password_basic.setTypeface(Navigation_activity.typeface);

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
    @Override
    public void onClick(View view) {
        if(view==lv_basic_name)
        {
            if (lv_basic_name.isClickable() == true && flag_name == true) {
                Log.e("fullname_22", "" + tv_basic_name.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_basic_name, tv_basic_name.getText().toString());
                tv_basic_name.setVisibility(View.GONE);
                edt_basic_name.setVisibility(View.VISIBLE);
                Log.e("fullname_34", "" + edt_basic_name.getText().toString());
               /* if(edt_full_name.getText().toString().length()!=0)
                {
                    Log.e("fullname_355", "" + edt_full_name.getText().toString());
                    My_Preference.set_fullname(getActivity(),edt_full_name.getText().toString());
                }
               */ flag_name = false;
            } else if (lv_basic_name.isClickable() == true && flag_name == false) {
                tv_basic_name.setVisibility(View.VISIBLE);
                edt_basic_name.setVisibility(View.GONE);
                flag_name = true;
            }
        }else if(view==lv_basic_email)
        {
            if (lv_basic_email.isClickable() == true && flag_email == true) {
                Log.e("email_22", "" + tv_basic_email.getText().toString());
                Navigation_activity.Check_Editext_NULL_Value(edt_basic_email, tv_basic_email.getText().toString());
                tv_basic_email.setVisibility(View.GONE);
                edt_basic_email.setVisibility(View.VISIBLE);
                Log.e("emaio_34", "" + edt_basic_email.getText().toString());
               /* if(edt_full_name.getText().toString().length()!=0)
                {
                    Log.e("fullname_355", "" + edt_full_name.getText().toString());
                    My_Preference.set_fullname(getActivity(),edt_full_name.getText().toString());
                }
               */ flag_email = false;
            } else if (lv_basic_email.isClickable() == true && flag_email == false) {
                tv_basic_email.setVisibility(View.VISIBLE);
                edt_basic_email.setVisibility(View.GONE);
                flag_email = true;
            }

        }
        else if(view==lv_save_data_basic || view==tv_edit_save_basic)
        {
            Log.e("click_251", "");
            CheckValidation();

        }else if(view==lv_edit_paasword)
        {
            startActivity(new Intent(getContext(), ChangePWpop.class));

            /*if (lv_edit_paasword.isClickable() == true && flag_password == true) {
                edt_change_password_basic.setVisibility(View.VISIBLE);
                tv_dot_basic.setVisibility(View.VISIBLE);
                flag_password = false;
            } else if (lv_edit_paasword.isClickable() == true && flag_password == false) {
                edt_change_password_basic.setVisibility(View.GONE);
                tv_dot_basic.setVisibility(View.GONE);
                flag_password = true;
            }*/
        }
    }

    private void CheckValidation() {
        fullname = edt_basic_name.getText().toString();
        email = edt_basic_email.getText().toString();


        Log.e("click_290", "" + fullname);
        Log.e("click_290", "" + email);
        Boolean validation_ok = true;
        if (edt_basic_name.getVisibility() == View.VISIBLE) {
            Log.e("click_291", "" + fullname);
            if (fullname.length() == 0) {
                Toast.makeText(getContext(), "Please enter your Full name", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }

        if (edt_basic_email.getVisibility() == View.VISIBLE) {
            Log.e("click_293", "" + fullname);
            if (email.length() == 0) {
                Toast.makeText(getContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            } else if (isValidEmailAddress(email) == false) {
                Toast.makeText(getContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show();
                validation_ok = false;
            }
        }
        if (validation_ok == true) {
            Log.e("click_295", "" + fullname);
            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                Log.e("click_366", "" + fullname);

                change_password=edt_change_password_basic.getText().toString();
                CALL_USER_UPDATE_API(user_id,fullname,email,change_password);
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void CALL_USER_UPDATE_API(String user_id, String fullname, String email,String change_password) {

        Log.e("change_password331", "" + change_password);

        progressBar_edit_basic.setVisibility(View.VISIBLE);
        lv_edit_basi.setVisibility(View.GONE);
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> update_profile = api.update_basic_lawyer_profile(user_id,fullname,email,change_password);


        update_profile.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_news", "" + response.body().toString());
                progressBar_edit_basic.setVisibility(View.GONE);
                lv_edit_basi.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_edit",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){

                        Bundle b = new Bundle();
                        b.putString("user_id", Login_preference.getuser_id(getActivity()));
                        Fragment myFragment = new Edit_basic_lawyer_freg();
                        myFragment.setArguments(b);
                        getFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();
                        /*    Intent i=new Intent(getActivity(),Navigation_activity.class);
                            startActivity(i);
*/
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();


                        //  Toast.makeText(getActivity(), "page_loade="+page, Toast.LENGTH_SHORT).show();



                    }else if (status.equalsIgnoreCase("error")){
                        progressBar_edit_basic.setVisibility(View.GONE);

                    }

                }catch (Exception e){
                    Log.e("",""+e);
                    progressBar_edit_basic.setVisibility(View.GONE);

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                progressBar_edit_basic.setVisibility(View.GONE);

            }
        });

    }
}
