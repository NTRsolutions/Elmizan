package com.sismatix.Elmizan.Fregment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sismatix.Elmizan.Activity.Navigation_activity;
import com.sismatix.Elmizan.CheckNetwork;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Login_freg extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    EditText et_Email_login, et_password_login;
    Button btn_create_account_login;
    TextView tv_register;
    LinearLayout gLogin,forgot;

    String screen, aricle_idd;

    Bundle bundle;
    TextView tv_welcome,tv_signin,tv_forgot_pw,googletext;
    private GoogleApiClient googleApiClient;

    public static final int SIGN_IN_CODE = 777;
    private static final int RC_SIGN_IN = 9001;

    String gname, gid, gemail, gphoto;
    String googlefirstname, googlelastname, googlemiddlename, locationid_wish;

    String signup_emailid, signup_passwordd;
    LinearLayout lv_login_parent;

    public static Call<ResponseBody> login = null;

    public Login_freg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_freg, container, false);
        Navigation_activity.iv_nav_logo.setVisibility(View.GONE);
        Navigation_activity.tv_nav_title.setVisibility(View.VISIBLE);
        Navigation_activity.tv_nav_title.setText(getResources().getString(R.string.log_in));
        Navigation_activity. tv_nav_title.setTypeface(Navigation_activity.typeface);
        lang_arbi();
        AllocateMemory(v);

        setupUI(lv_login_parent);
        bundle = this.getArguments();
        //get_CheckScreen();

        btn_create_account_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        //gLogin.setOnClickListener(this);

        GOOGLE_LOGIN(v);

        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        *//*if(googleApiClient == null || !googleApiClient.isConnected()){
            try {
                googleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity(), this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*//*

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .enableAutoManage(getActivity(), 0 *//* clientId *//*, this)
                .build();*/

        if (bundle != null) {
            screen = bundle.getString("screen");
            aricle_idd = bundle.getString("article_id");

            Log.e("CheckScreen", "" + screen);
            Log.e("article_idd", "" + aricle_idd);
        } else {
            Log.e("CheckScreen", "" + screen);
            Log.e("article_idd", "" + aricle_idd);
        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://elmizan.demoproject.info/admin/index.php?p=forgot-password"));
                startActivity(browserIntent);
            }
        });
        return v;
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    private void GOOGLE_LOGIN(View v) {

        gLogin=(LinearLayout)v.findViewById(R.id.google_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

    }

    private void AllocateMemory(View v) {
        lv_login_parent = (LinearLayout) v.findViewById(R.id.lv_login_parent);
        et_Email_login = (EditText) v.findViewById(R.id.et_Email_login);
        et_password_login = (EditText) v.findViewById(R.id.et_password_login);
        btn_create_account_login = (Button) v.findViewById(R.id.btn_create_account_login);
        tv_register = (TextView) v.findViewById(R.id.tv_register);
        forgot = (LinearLayout) v.findViewById(R.id.forgot);
        //gLogin = (LinearLayout) v.findViewById(R.id.google);
        tv_welcome = (TextView)v.findViewById(R.id.tv_welcome);
        tv_signin = (TextView)v.findViewById(R.id.tv_signin);
        googletext = (TextView)v.findViewById(R.id.googletext);
        tv_forgot_pw = (TextView)v.findViewById(R.id.tv_forgot_pw);



        tv_welcome.setTypeface(Navigation_activity.typeface);
        tv_signin.setTypeface(Navigation_activity.typeface);
        googletext.setTypeface(Navigation_activity.typeface);
        tv_forgot_pw.setTypeface(Navigation_activity.typeface);

        et_Email_login.setTypeface(Navigation_activity.typeface);
        et_password_login.setTypeface(Navigation_activity.typeface);

        btn_create_account_login.setTypeface(Navigation_activity.typeface);

        tv_register.setTypeface(Navigation_activity.typeface);

    }





    @Override
    public void onClick(View view) {
        if (btn_create_account_login == view) {
            signup_emailid = et_Email_login.getText().toString();
            signup_passwordd = et_password_login.getText().toString();

            if (et_Email_login.getText().length() == 0) {
                Toast.makeText(getContext(), "Please enter your Email id", Toast.LENGTH_SHORT).show();
            } else if (isValidEmailAddress(et_Email_login.getText().toString()) == false) {
                Toast.makeText(getContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show();
            } else if (et_password_login.getText().length() == 0) {
                Toast.makeText(getContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
            } else if (et_password_login.getText().toString().length() <= 5) {
                Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            } else {
                Login_Api(signup_emailid, signup_passwordd);
            }
        } else if (view == tv_register) {
            pushFragment(new Register_freg(), "register");
        }
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

    private void Login_Api(String login_emailid, String login_password) {


       /* ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> Login;*/

        if (gid == "" || gid == null || gid == "null" || gid.equalsIgnoreCase(null) || gid.equalsIgnoreCase("null")) {
            /*Login = apii.login(login_emailid, login_password);
            Log.e("giiddd", "" + gid);*/
            Log.e("with_login", "");
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            login = api.login(login_emailid, login_password);
        } else {
            Log.e("with_glogin", "");
            Log.e("giddd", "" + gid);
            Log.e("gemailll", "" + gemail);
            Log.e("gnameee", "" + gname);
            Log.e("gphotoo", "" + gphoto);
            ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
            login = api.google_login(gid, gemail, gname, gphoto);
        }

        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status_login", "" + status);
                    String meassg = jsonObject.getString("msg");
                    Log.e("message", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), "" + meassg, Toast.LENGTH_SHORT).show();
                        JSONObject object = new JSONObject(jsonObject.getString("data"));

                        Log.e("dataaaa_login", "" + object);

                        Login_preference.setLogin_flag(getActivity(), "1");
                        Login_preference.setuser_id(getActivity(), object.getString("user_id"));
                        Login_preference.setemail(getActivity(), object.getString("user_email"));
                        Login_preference.setuser_name(getActivity(), object.getString("user_name"));
                        Login_preference.setuser_short_desc(getActivity(), object.getString("user_description"));


                        Login_preference.setuser_profile(getActivity(), object.getString("user_avatar_url"));
                        My_Preference.set_premium_lawyer(getActivity(), object.getString("basic_premium"));

                        /*Intent intent=new Intent(getActivity(),Navigation_activity.class);
                        startActivity(intent);
                        getActivity().finish();*/

                        if (bundle != null) {
                            String loginflag = Login_preference.getLogin_flag(getActivity());
                            if (loginflag.equalsIgnoreCase("1") || loginflag == "1") {
                                lv_withlogin_header.setVisibility(View.VISIBLE);
                                lv_withoutlogin_header.setVisibility(View.GONE);
                                //hide if user not login
                                nav_register.setVisible(false);
                                nav_contactus.setVisible(false);
                                nav_signin.setVisible(false);
                                nav_logout.setVisible(true);
                                withoutloginicon.setVisibility(View.VISIBLE);
                               // Navigation_activity.tv_nav_appal.setText( object.getString("user_description"));

                                tv_nav_user_name.setText(Login_preference.getuser_name(getActivity()));
                            } else {
                                lv_withlogin_header.setVisibility(View.GONE);
                                lv_withoutlogin_header.setVisibility(View.VISIBLE);
                                withoutloginicon.setVisibility(View.INVISIBLE);
                                nav_register.setVisible(true);
                                nav_contactus.setVisible(true);
                                nav_signin.setVisible(true);
                                nav_myaccount.setVisible(false);
                                nav_myarticle.setVisible(false);

                                nav_logout.setVisible(false);
                            }
                            if (screen.equalsIgnoreCase("news_details")) {

                                Bundle b = new Bundle();
                                b.putString("article_id", aricle_idd);
                                Fragment myFragment = new News_Detail_freg();
                                myFragment.setArguments(b);
                                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("Newsdetail")
                                        .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                        .replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                            /*EmailLogin nextFrag = new EmailLogin();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.rootLayout, nextFrag, "login")
                                    .addToBackStack(null)
                                    .commit();*/

                            } else if (screen.equalsIgnoreCase("Navigation")) {

                                Fragment myFragment = new Edit_premium_lawyer_profile();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                        .replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                            } else if (screen.equalsIgnoreCase("Article")) {

                                Fragment myFragment = new Add_Article_Freg();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                        .replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                            }else if (screen.equalsIgnoreCase("Add_Prem_Article")) {

                                Fragment myFragment = new Add_Article_Freg();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                        .replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                            }else if (screen.equalsIgnoreCase("Add_Media")) {

                                Fragment myFragment = new UPload_Media_freg();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.fade_in, 0, 0, R.anim.fade_out)
                                        .replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();

                            }

                        } else {
                            Intent intent = new Intent(getActivity(), Navigation_activity.class);
                            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            startActivity(intent);
                            getActivity().finish();
                        }

                         /*else {
                            Intent intent = new Intent(getActivity(), Navigation_activity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }*/

                        Intent intent = new Intent(getActivity(), Navigation_activity.class);
                        startActivity(intent);
                        getActivity().finish();

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.e("resultofgoogle", "" + result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        /*if (result.isSuccess()) {
            goMainScreen();
        } else {
            Toast.makeText(getContext(), R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }*/

        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();

            gname = account.getDisplayName();
            gemail = account.getEmail();
            gid = account.getId();
            gphoto = String.valueOf(account.getPhotoUrl());

            Log.e("gimgl", "" + gphoto);
            Log.e("gnamel", "" + gname);
            Log.e("gemaill", "" + gemail);
            Log.e("gidl", "" + gid);
            Log.e("gtokenidl", "" + account.getIdToken());
            Log.d("gname", "" + gname);

            if (gphoto.equals("null") == true || gphoto.equals(null) == true) {
                Log.e("gimgnull", "" + gphoto);
                gphoto = "";
            } else {
                Log.e("gim", "" + gphoto);
            }

            Log.d("gnameLength-->", "" + gname);

            if (gname != null) {
                Log.e("gnameLength_283", "" + gname);

                String[] parts = gname.split("\\s+");

                Log.d("Length-->", "" + parts.length);
                if (parts.length == 2) {
                    googlefirstname = parts[0];
                    googlelastname = parts[1];
                    Log.d("First-->", "" + googlefirstname);
                    Log.d("Last-->", "" + googlelastname);
                } else if (parts.length == 3) {
                    googlefirstname = parts[0];
                    googlemiddlename = parts[1];
                    googlelastname = parts[2];
                    Log.d("First-->", "" + googlefirstname);
                    Log.d("Last-->", "" + googlelastname);
                }

            } else {
                Log.e("gnameLength_283_null", "" + gname);
                googlefirstname = "";
                googlelastname = "";
                googlemiddlename = "";
            }

            if (CheckNetwork.isNetworkAvailable(getActivity())) {
                Login_preference.setLogin_flag(getActivity(), "1");
                Login_Api(signup_emailid, signup_passwordd);
            } else {
                Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

            //Login_Api(signup_emailid,signup_passwordd);

        } else {
            Toast.makeText(getContext(), "Not Login", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

   /* @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
*/
    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage((FragmentActivity) getContext());
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void goMainScreen() {

       /* Fragment myFragment = new GoogleDetails();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack(null).commit();*/

        Intent intent = new Intent(getContext(), Navigation_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    /*@Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }*/
}
