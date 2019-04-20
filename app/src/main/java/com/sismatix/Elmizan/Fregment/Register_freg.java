package com.sismatix.Elmizan.Fregment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Model.Country_model;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register_freg extends Fragment implements View.OnClickListener {
    EditText editTextname_reg,editTextEmail_reg,editTextphone_reg;
    TextInputEditText editTextpassword_reg;
    Button btn_register;
    CheckBox checkbox;
    String  checked_value_pass,countryid;
    Spinner spinner_country;
    RelativeLayout lv_register_parent;
    TextView sp_text;
    TextInputLayout layout_mobilenuber,etPasswordLayout,layout_password,layout_email;

    public static ArrayList<String> country_name_code = new ArrayList<String>();
    public static ArrayList<String> country_name = new ArrayList<String>();
    private List<Country_model> country_model = new ArrayList<Country_model>();
    public Register_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_register_freg, container, false);

        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.Create_an_account));
        Navigation_activity. tv_nav_title.setTypeface(Navigation_activity.typeface);
        lang_arbi();
        AllocateMemory(v);
        setupUI(lv_register_parent);
        btn_register.setOnClickListener(this);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("checked",""+b);
                if(b==true)
                {
                    checked_value_pass="1";
                }else {
                    checked_value_pass="0";
                }
            }
        });

        if (CheckNetwork.isNetworkAvailable(getActivity())) {

            CALL_COUNTRY_API();
        } else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                int selected_item_position = spinner_country.getSelectedItemPosition();
                countryid = country_name_code.get(selected_item_position);
                // MyAddress_Preference.setCountryId(getActivity(), String.valueOf(selected_item_position));
                Log.e("countryid", "" + countryid);
                String selected_country = String.valueOf(spinner_country.getSelectedItem());

             //    Toast.makeText(getActivity(), selected_item_position + " " + selected_country + " => " + countryid, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {

            }

        });


        return v;
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

                country_name.add(getActivity().getResources().getString(R.string.Select));
                country_name_code.add("0");

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_country",""+status);
                    String message = jsonObject.getString("msg");
                    Log.e("message",""+message);
                    if (status.equalsIgnoreCase("success")){
                        JSONArray data_array=jsonObject.getJSONArray("data");

                        for (int i = 0; i < data_array.length(); i++) {

                            try {
                                JSONObject news_object = data_array.getJSONObject(i);
                                Log.e("Name",""+news_object.getString("country_id"));
                                country_model.add(new Country_model(news_object.getString("country_id"),
                                        news_object.getString("country_name"),
                                        news_object.getString("country_status"),
                                        news_object.getString("country_image_url")
                                ));

                                country_name.add(  news_object.getString("country_name"));
                                country_name_code.add(news_object.getString("country_id"));

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                            }

                        }
                       spinner_country.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, country_name));

                        }else if (status.equalsIgnoreCase("error")){
                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void AllocateMemory(View v) {
        editTextname_reg=(EditText)v.findViewById(R.id.editTextname_reg);
        editTextEmail_reg=(EditText)v.findViewById(R.id.editTextEmail_reg);
        editTextphone_reg=(EditText)v.findViewById(R.id.editTextphone_reg);
        editTextpassword_reg=(TextInputEditText)v.findViewById(R.id.editTextpassword_reg);
        btn_register=(Button)v.findViewById(R.id.btn_register);
        checkbox=(CheckBox) v.findViewById(R.id.checkbox);
        spinner_country=(Spinner) v.findViewById(R.id.spinner_country);
        lv_register_parent=(RelativeLayout) v.findViewById(R.id.lv_register_parent);
        sp_text=(TextView) v.findViewById(R.id.sp_text);


        layout_mobilenuber=(TextInputLayout) v.findViewById(R.id.layout_mobilenuber);
        etPasswordLayout=(TextInputLayout) v.findViewById(R.id.etPasswordLayout);
        layout_password=(TextInputLayout) v.findViewById(R.id.layout_password);
        layout_email=(TextInputLayout) v.findViewById(R.id.layout_email);


        checkbox.setTypeface(Navigation_activity.typeface);



        editTextname_reg.setTypeface(Navigation_activity.typeface);
        editTextEmail_reg.setTypeface(Navigation_activity.typeface);
        editTextpassword_reg.setTypeface(Navigation_activity.typeface);
        editTextphone_reg.setTypeface(Navigation_activity.typeface);

        sp_text.setTypeface(Navigation_activity.typeface);
        checkbox.setTypeface(Navigation_activity.typeface);
        btn_register.setTypeface(Navigation_activity.typeface);

        layout_mobilenuber.setTypeface(Navigation_activity.typeface);
        etPasswordLayout.setTypeface(Navigation_activity.typeface);
        layout_password.setTypeface(Navigation_activity.typeface);
        layout_email.setTypeface(Navigation_activity.typeface);

    }

    @Override
    public void onClick(View view) {
        final String regi_name = editTextname_reg.getText().toString();
        final String regi_emailid = editTextEmail_reg.getText().toString();
        final String regi_phone = editTextphone_reg.getText().toString();
        final String regi_password = editTextpassword_reg.getText().toString();

        if(editTextname_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
        }else if (editTextEmail_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Email id", Toast.LENGTH_SHORT).show();
        }else if (isValidEmailAddress(editTextEmail_reg.getText().toString()) == false) {
            Toast.makeText(getContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show();
        } else if (editTextpassword_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
        } else if (editTextpassword_reg.getText().toString().length() <= 5) {
            Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
        }else if (editTextphone_reg.getText().length() == 0) {
            Toast.makeText(getContext(), "Please enter your Phone number", Toast.LENGTH_SHORT).show();
        }else if (regi_phone.length() < 8 || regi_phone.length() > 13) {//et_shippingphonenumber.getText().length() == 0
            Toast.makeText(getContext(), "Please enter valid Mobile no.", Toast.LENGTH_SHORT).show();
        }else if (spinner_country.getSelectedItem().toString().trim().equals("Select") == true) {
            Toast.makeText(getActivity(), "Please Select Country", Toast.LENGTH_SHORT).show();
        }
        else {
            Register_Api(regi_name,regi_emailid,regi_phone,regi_password,countryid);
        }
    }

    private void Register_Api(String regi_name, String regi_emailid, String regi_phone, String regi_password,String user_country_id) {

        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        String agree_terms="1";
        Call<ResponseBody> register = apii.get_register(regi_name, regi_emailid,regi_phone,
                regi_password,regi_password,agree_terms,checked_value_pass,user_country_id);
        Log.e("checked_value_pass", "" + checked_value_pass);
        Log.e("country_value_pass", "" + countryid);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String meassg = jsonObject.getString("msg");
                    Log.e("message", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                        //JSONObject object=new JSONObject(jsonObject.getString("data"));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Login_freg nextFrag = new Login_freg();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_fram_layout, nextFrag, "login")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        },3000);


                        //getActivity().finish();
                    } else if (status.equalsIgnoreCase("error")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
             //   Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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
