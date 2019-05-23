package com.sismatix.Elmizan.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Fregment.Add_Article_Freg;
import com.sismatix.Elmizan.Fregment.Edit_premium_lawyer_profile;
import com.sismatix.Elmizan.Fregment.Home_freg;
import com.sismatix.Elmizan.Fregment.News_Detail_freg;
import com.sismatix.Elmizan.Fregment.Register_freg;
import com.sismatix.Elmizan.Fregment.UPload_Media_freg;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;
import com.sismatix.Elmizan.Retrofit.ApiClient;
import com.sismatix.Elmizan.Retrofit.ApiInterface;

import org.json.JSONObject;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sismatix.Elmizan.Activity.Navigation_activity.lv_withlogin_header;
import static com.sismatix.Elmizan.Activity.Navigation_activity.lv_withoutlogin_header;
import static com.sismatix.Elmizan.Activity.Navigation_activity.nav_contactus;
import static com.sismatix.Elmizan.Activity.Navigation_activity.nav_logout;
import static com.sismatix.Elmizan.Activity.Navigation_activity.nav_myaccount;
import static com.sismatix.Elmizan.Activity.Navigation_activity.nav_myarticle;
import static com.sismatix.Elmizan.Activity.Navigation_activity.nav_register;
import static com.sismatix.Elmizan.Activity.Navigation_activity.nav_signin;
import static com.sismatix.Elmizan.Activity.Navigation_activity.tv_nav_user_name;
import static com.sismatix.Elmizan.Activity.Navigation_activity.withoutloginicon;

public class ChangePWpop extends AppCompatActivity implements View.OnClickListener {

    EditText edt_old_pw,edt_new_pw,edt_confirem_pw;
    LinearLayout lv_send_email;
    String user_id,old_pw,new_pw,confirm_pw;
    public static Call<ResponseBody> changepw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwpop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.4));

        lang_arbi();
        AllocateMemory();
        callChangepwApi();

        old_pw = edt_old_pw.getText().toString();
        new_pw = edt_new_pw.getText().toString();
        confirm_pw = edt_confirem_pw.getText().toString();

        user_id = Login_preference.getuser_id(getApplicationContext());
        Log.e("user_id_changepw",""+user_id);

        lv_send_email.setOnClickListener(this);

    }

    private void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void callChangepwApi() {

    }

    private void AllocateMemory() {
        edt_old_pw = (EditText)findViewById(R.id.edt_old_pw);
        edt_new_pw = (EditText)findViewById(R.id.edt_new_pw);
        edt_confirem_pw = (EditText)findViewById(R.id.edt_confirem_pw);
        lv_send_email = (LinearLayout)findViewById(R.id.lv_send_email);
    }

    @Override
    public void onClick(View view) {
        if (lv_send_email == view) {
            if (edt_old_pw.getText().length() == 0) {
                Toast.makeText(this, "Please enter your old password", Toast.LENGTH_SHORT).show();
            } else if (edt_new_pw.getText().length() == 0) {
                Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show();
            } else if (edt_new_pw.getText().toString().length() <= 5) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            } else if (edt_confirem_pw.getText().toString().length() == 0) {
                Toast.makeText(this, "Please enter your confirm password", Toast.LENGTH_SHORT).show();
            } else if(!edt_new_pw.getText().toString().equals(edt_confirem_pw.getText().toString())){
                Toast.makeText(this, "New password and confirm password should be same", Toast.LENGTH_SHORT).show();

            }else {

                if (CheckNetwork.isNetworkAvailable(ChangePWpop.this)) {
                    ChangePW(user_id, edt_old_pw.getText().toString(),edt_new_pw.getText().toString(),edt_confirem_pw.getText().toString());


                } else {
                    Toast.makeText(ChangePWpop.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }



            }
        }
    }

    private void ChangePW(String user_id, String oldpw, String newpw, String confpw) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        changepw = api.ChangePW(user_id, oldpw,newpw,confpw);
        changepw.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_changepw", "" + status);
                    String meassg = jsonObject.getString("msg");
                    Log.e("message_changepw", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        onBackPressed();
                        Toast.makeText(ChangePWpop.this, meassg, Toast.LENGTH_SHORT).show();
                    } else if (status.equalsIgnoreCase("error")) {
                        //Toast.makeText(getApplicationContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
