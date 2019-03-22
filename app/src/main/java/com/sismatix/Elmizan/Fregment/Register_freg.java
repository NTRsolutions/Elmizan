package com.sismatix.Elmizan.Fregment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.sismatix.Elmizan.Activity.Navigation_activity;
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
public class Register_freg extends Fragment implements View.OnClickListener {
    EditText editTextname_reg,editTextEmail_reg,editTextphone_reg;
    TextInputEditText editTextpassword_reg;
    Button btn_register;
    CheckBox checkbox;
    String  checked_value_pass;

    public Register_freg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_register_freg, container, false);
        AllocateMemory(v);

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

        return v;
    }

    private void AllocateMemory(View v) {
        editTextname_reg=(EditText)v.findViewById(R.id.editTextname_reg);
        editTextEmail_reg=(EditText)v.findViewById(R.id.editTextEmail_reg);
        editTextphone_reg=(EditText)v.findViewById(R.id.editTextphone_reg);
        editTextpassword_reg=(TextInputEditText)v.findViewById(R.id.editTextpassword_reg);
        btn_register=(Button)v.findViewById(R.id.btn_register);
        checkbox=(CheckBox) v.findViewById(R.id.checkbox);

        btn_register.setTypeface(Navigation_activity.typeface);
        editTextname_reg.setTypeface(Navigation_activity.typeface);
        editTextEmail_reg.setTypeface(Navigation_activity.typeface);
        editTextphone_reg.setTypeface(Navigation_activity.typeface);
        editTextpassword_reg.setTypeface(Navigation_activity.typeface);
        checkbox.setTypeface(Navigation_activity.typeface);

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
        }else if (regi_phone.length() < 9 || regi_phone.length() > 13) {//et_shippingphonenumber.getText().length() == 0
            Toast.makeText(getContext(), "Please enter valid Mobile no.", Toast.LENGTH_SHORT).show();
        }else {
            Register_Api(regi_name,regi_emailid,regi_phone,regi_password);
        }
    }

    private void Register_Api(String regi_name, String regi_emailid, String regi_phone, String regi_password) {
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        String agree_terms="1";
        Call<ResponseBody> register = apii.get_register(regi_name, regi_emailid,regi_phone,
                regi_password,regi_password,agree_terms,checked_value_pass);
        Log.e("checked_value_pass", "" + checked_value_pass);
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

                        Login_freg nextFrag = new Login_freg();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_fram_layout, nextFrag, "login")
                                .addToBackStack(null)
                                .commit();
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
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
