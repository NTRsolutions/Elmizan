package com.sismatix.Elmizan.Fregment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
public class Login_freg extends Fragment implements View.OnClickListener {
    EditText et_Email_login,et_password_login;
    Button btn_create_account_login;
    TextView tv_register;

    public Login_freg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_login_freg, container, false);
        AllocateMemory(v);
        btn_create_account_login.setOnClickListener(this);

        return v;
    }

    private void AllocateMemory(View v) {
        et_Email_login=(EditText)v.findViewById(R.id.et_Email_login);
        et_password_login=(EditText)v.findViewById(R.id.et_password_login);
        btn_create_account_login=(Button)v.findViewById(R.id.btn_create_account_login);
        tv_register=(TextView) v.findViewById(R.id.tv_register);
    }

    @Override
    public void onClick(View view) {
        if (btn_create_account_login==view){
            final String signup_emailid = et_Email_login.getText().toString();
            final String signup_passwordd = et_password_login.getText().toString();

             if (et_Email_login.getText().length() == 0) {
                Toast.makeText(getContext(), "Please enter your Email id", Toast.LENGTH_SHORT).show();
            }else if (isValidEmailAddress(et_Email_login.getText().toString()) == false) {
                Toast.makeText(getContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show();
            } else if (et_password_login.getText().length() == 0) {
                Toast.makeText(getContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
            } else if (et_password_login.getText().toString().length() <= 5) {
                Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            } else {
                 Createaccount_Api(signup_emailid,signup_passwordd);
            }
        }
    }
    private void Createaccount_Api(String login_emailid,String login_password) {
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> signup = apii.login(login_emailid, login_password);

        signup.enqueue(new Callback<ResponseBody>() {
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
                        JSONObject object=new JSONObject(jsonObject.getString("data"));
                        Login_preference.setLogin_flag(getActivity(),"1");
                        Login_preference.setuser_id(getActivity(),object.getString("user_id"));
                        Login_preference.setemail(getActivity(),object.getString("user_email"));
                        Login_preference.setuser_name(getActivity(),object.getString("user_name"));
                        Login_preference.setuser_profile(getActivity(),object.getString("user_avatar_url"));
                        Intent intent=new Intent(getActivity(),Navigation_activity.class);
                        startActivity(intent);
                        getActivity().finish();
                        /*EmailLogin nextFrag = new EmailLogin();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.rootLayout, nextFrag, "login")
                                .addToBackStack(null)
                                .commit();*/
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
