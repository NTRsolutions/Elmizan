package com.sismatix.Elmizan.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.sismatix.Elmizan.Adapter.Country_Adapter;
import com.sismatix.Elmizan.CheckNetwork;
import com.sismatix.Elmizan.Fregment.About_us;
import com.sismatix.Elmizan.Fregment.Article_freg;
import com.sismatix.Elmizan.Fregment.Contact_us;
import com.sismatix.Elmizan.Fregment.Directory_freg;
import com.sismatix.Elmizan.Fregment.Edit_premium_lawyer_profile;
import com.sismatix.Elmizan.Fregment.Final_Library_fregment;
import com.sismatix.Elmizan.Fregment.Home_freg;
import com.sismatix.Elmizan.Fregment.Login_freg;
import com.sismatix.Elmizan.Fregment.New_Library_freg;
import com.sismatix.Elmizan.Fregment.Register_freg;
import com.sismatix.Elmizan.Model.Country_model;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Navigation_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    public  BottomNavigationView bottom_navigation;
    DrawerLayout drawer;
    public static Toolbar toolbar;
    NavigationView navigationView;
    public  static ImageView iv_nav_country_image,iv_nav_logo;
    public  static TextView tv_nav_title,tv_nav_user_name,tv_nav_appal;
    Bundle b;
    String Screen,register;
    public static AssetManager am ;
    boolean doubleBackToExitPressedOnce = false;
    public static Typeface typeface,tf,medium;
    public static LinearLayout lv_withlogin_header,lv_withoutlogin_header,withoutloginicon;
    public static MenuItem nav_register, nav_contactus, nav_signin, nav_myaccount, nav_myarticle, nav_logout;
    public static ImageView iv_profile_image;
    Point p,c;
    ProgressDialog PD;
    RecyclerView recycler_country;
    public static PopupWindow popup;
    ProgressBar progressBar_country;
    public  static ImageView iv_nav_premium_logo,iv_nav_twitter;
    private String android_deviceid;

    private List<Country_model> country_model = new ArrayList<Country_model>();
    private Country_Adapter country_adapter;
    GoogleApiClient googleApiClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        lang_arbi();
        AllocateMemory();
        setSupportActionBar(toolbar);
        SET_FONT_STYLE();
       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
*/
        android_deviceid = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("deviceid_117", "" + android_deviceid);

        My_Preference.setCountry_name(Navigation_activity.this,"1");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        PD = new ProgressDialog(Navigation_activity.this);
        PD.setCancelable(false);

        navigationView.setNavigationItemSelectedListener(this);

        ///navigation menu icon change

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);

        if(Login_preference.getLogin_flag(Navigation_activity.this).equalsIgnoreCase("1")){
            lv_withlogin_header.setVisibility(View.VISIBLE);
            lv_withoutlogin_header.setVisibility(View.GONE);
            //hide if user not login
            nav_register.setVisible(false);
            nav_contactus.setVisible(false);
            nav_signin.setVisible(false);
            nav_logout.setVisible(true);
            withoutloginicon.setVisibility(View.VISIBLE);
            tv_nav_user_name.setText(Login_preference.getuser_name(this));

            String desc=Login_preference.getuser_short_desc(this);
            if (desc.equalsIgnoreCase("null") == true) {
                tv_nav_appal.setHint("");
                //textview.setText("Enter Value here");
            }else {

                tv_nav_appal.setText(desc);
            }

            //tv_nav_appal.setText(Login_preference.getuser_short_desc(this));

            //   Check_String_NULL_Value(tv_nav_appal,desc, "a");

            Glide.with(this).load(Login_preference.getuser_profile(this)).into(iv_profile_image);

            if (My_Preference.get_premium_lawyer(Navigation_activity.this).equalsIgnoreCase("premium") == true) {
                lv_withlogin_header.setVisibility(View.VISIBLE);
                //iv_nav_premium_logo.setImageDrawable(Navigation_activity.this.getDrawable(R.drawable.menu_img));
                iv_nav_premium_logo.setImageResource(R.drawable.menu_img);
            } else {
                lv_withlogin_header.setVisibility(View.VISIBLE);
                //iv_nav_premium_logo.setImageDrawable(Navigation_activity.this.getDrawable(R.drawable.grey_perimimum));
                iv_nav_premium_logo.setImageResource(R.drawable.grey_perimimum);
            }

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


        ////////-------bottom navigation view---------///
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Navigation_activity.this) ).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        //--------------bottom navigation view----------//
        Bootom_Navigation_view();

        b = getIntent().getExtras();
        if (b != null) {
            Screen=getIntent().getExtras().getString("screen");

            Log.e("lofin",""+Screen);
            if(Screen.equalsIgnoreCase("Login")==true){
                pushFragment(new Login_freg(),"Login");
            }else if(Screen.equalsIgnoreCase("register")==true){
                pushFragment(new Register_freg(),"register");
            }
        }


        //---------get country//
        iv_nav_country_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c != null)

                    // showPopupCurrency(Navigation_activity.this, c);
                    showPopup(Navigation_activity.this, c);


            }
        });


        if (CheckNetwork.isNetworkAvailable(Navigation_activity.this)) {
            CALL_DEVICE_TOKEN_API();
        } else {
            Toast.makeText(Navigation_activity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        iv_nav_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("almizakwt@"));
                startActivity(browserIntent);
            }
        });

    }

    public  void lang_arbi() {
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
    private void CALL_DEVICE_TOKEN_API() {

        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> GetToken = apii.Get_Device_Token(Login_preference.getdevicetoken(Navigation_activity.this), android_deviceid);
        Log.e("refreshedToken_pass", "" + Login_preference.getdevicetoken(Navigation_activity.this));
        Log.e("android_deviceid_pass", "" + android_deviceid);
        GetToken.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String meassg = jsonObject.getString("msg");
                    Log.e("message_token", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        //Toast.makeText(Navigation_activity.this, "" + meassg, Toast.LENGTH_SHORT).show();


                    } else if (status.equalsIgnoreCase("error")) {
                        // Toast.makeText(Navigation_activity.this, "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Navigation_activity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        iv_nav_country_image.getLocationOnScreen(location);
        c = new Point();
        c.x = location[0];
        c.y = location[1];

    }
    private void showPopup(final Activity context, Point c) {
        int popupWidth = 500;
        int popupHeight = 650;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
      //  Toast.makeText(Navigation_activity.this, width + " = " + height, Toast.LENGTH_SHORT).show();



        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup1);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.country_popup_row, viewGroup);
     ///   lv_english = (LinearLayout) layout.findViewById(R.id.lv_english);
     //   lv_arbi = (LinearLayout) layout.findViewById(R.id.lv_arbi);


        progressBar_country = (ProgressBar) layout.findViewById(R.id.progressBar_country);
        recycler_country = (RecyclerView) layout.findViewById(R.id.recycler_country);
        LinearLayout lv_show = (LinearLayout) layout.findViewById(R.id.lv_country_show);
        country_adapter = new Country_Adapter(Navigation_activity.this, country_model);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Navigation_activity.this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(false);
        recycler_country.setLayoutManager(layoutManager);
        recycler_country.setAdapter(country_adapter);
        //lv_show.setVisibility(View.VISIBLE);

        CALL_COUNTRY_API(lv_show);
        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        int OFFSET_X;
        int OFFSET_Y;
        popup.setBackgroundDrawable(new BitmapDrawable());

       /* if (width > 1300) {
            popupHeight = 950;
            popup.setHeight(popupHeight);
            OFFSET_X = -538;
            OFFSET_Y = 80;
            Log.e("abc","abc8");

            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 1200 && width <= 1300) {
            popupWidth = 900;
            popupHeight = 1100;
            popup.setHeight(popupHeight);
            popup.setWidth(popupWidth);
            OFFSET_X = -560;
            OFFSET_Y = 90;
            Log.e("abc","abc1");

            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 1100 && width <= 1200) {
            popupWidth = 900;
            popupHeight = 1100;
            Log.e("abc","abc");
            popup.setHeight(popupHeight);
            popup.setWidth(popupWidth);
            OFFSET_X = -975;
            OFFSET_Y = 80;
            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 1020 && width <= 1100) {
            popupHeight = 750;
            popup.setHeight(popupHeight);
            OFFSET_X = -550;
            OFFSET_Y = 100;
            Log.e("abc","abc2");

            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 950 && width <= 1020) {
            OFFSET_X = -550;
            OFFSET_Y = 80;            Log.e("abc","abc3");


            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 850 && width <= 950) {
            OFFSET_X = -495;
            OFFSET_Y = 40;
            Log.e("abc","abc4");

            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 750 && width <= 800) {
            OFFSET_X = -420;
            OFFSET_Y = 80;
            Log.e("abc","abc5");

            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 650 && width <= 750) {
            OFFSET_X = -450;
            OFFSET_Y = 80;
            Log.e("abc","abc6");

            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);
        } else if (width > 550 && width <= 650) {
            popup.setWidth(popupWidth);
            OFFSET_X = -430;
            OFFSET_Y = 40;
            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);

        } else if (width >= 500 && width <= 560) {
            popup.setWidth(popupWidth);
            OFFSET_X = -350;
            OFFSET_Y = 80;
            popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);

        }
*/


       // popup.setBackgroundDrawable(new BitmapDrawable());
     //   popup.showAtLocation(layout, Gravity.NO_GRAVITY, c.x + OFFSET_X, c.y + OFFSET_Y);


        popup.showAtLocation(layout, Gravity.CENTER,0,0);
    }


    private void CALL_COUNTRY_API(final LinearLayout lv_show) {
        progressBar_country.setVisibility(View.VISIBLE);
        recycler_country.setVisibility(View.GONE);
        country_model.clear();
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> country_list = api.get_country_list(ApiClient.user_status);

        country_list.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_counry", "" + response.body().toString());
                progressBar_country.setVisibility(View.GONE);

              //  PD.dismiss();
                recycler_country.setVisibility(View.VISIBLE);
                //        lv_show.setVisibility(View.VISIBLE);

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

                            } catch (Exception e) {
                                Log.e("Exception", "" + e);
                            } finally {
                                country_adapter.notifyItemChanged(i);
                            }

                        }

                    }else if (status.equalsIgnoreCase("error")){
                    }

                }catch (Exception e){
                    Log.e("",""+e);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Navigation_activity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SET_FONT_STYLE() {
        am = getApplicationContext().getAssets();
        typeface = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "Cairo-SemiBold.ttf"));
        tf = Typeface.createFromAsset(am,
                String.format(Locale.getDefault(), "Cairo-SemiBold.ttf"));
    }

    private void AllocateMemory() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        iv_nav_country_image = findViewById(R.id.iv_nav_country_image);
        iv_nav_twitter = findViewById(R.id.iv_nav_twitter);
        iv_nav_logo = findViewById(R.id.iv_nav_logo);
        tv_nav_title = findViewById(R.id.tv_nav_title);


        View header = navigationView.getHeaderView(0);
        lv_withlogin_header=(LinearLayout) header.findViewById(R.id.lv_withlogin_header);
        lv_withoutlogin_header=(LinearLayout) header.findViewById(R.id.lv_withoutlogin_header);
        tv_nav_user_name=(TextView) header.findViewById(R.id.tv_nav_user_name);
        tv_nav_appal=(TextView) header.findViewById(R.id.tv_nav_appal);
        iv_profile_image = (ImageView) header.findViewById(R.id.iv_profile_image);
        withoutloginicon=(LinearLayout) findViewById(R.id.withoutloginicon);
        iv_nav_premium_logo = (ImageView)header. findViewById(R.id.iv_navigation_premium_logo);

        Menu menu =navigationView.getMenu();
        nav_register = menu.findItem(R.id.nav_register);
        nav_contactus = menu.findItem(R.id.nav_contactus);
        nav_signin = menu.findItem(R.id.nav_signin);
        nav_myaccount = menu.findItem(R.id.nav_my_account);
        nav_myarticle = menu.findItem(R.id.nav_my_article);
        nav_logout = menu.findItem(R.id.nav_logout);






    }

    public DrawerLayout getmDrawerLayout() {
        return drawer;
    }
    private void Bootom_Navigation_view() {


        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        BottomNavigationViewHelper.disableShiftMode(bottom_navigation);

        Menu menu = bottom_navigation.getMenu();
        selectFragment(menu.getItem(0));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return false;
        }
    };

    private void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.bottom_nav_home:
                pushFragment(new Home_freg(),"Home");
                break;
            case R.id.bottom_nav_directory:
                pushFragment(new Directory_freg(),"directory");
                break;
            case R.id.bottom_nav_article:
                pushFragment(new Article_freg(),"article");
                break;

            case R.id.bottom_nav_library:
                pushFragment(new Final_Library_fregment(),"library");

                break;
            case R.id.bottom_nav_myaccount:

                if(Login_preference.getLogin_flag(Navigation_activity.this).equalsIgnoreCase("1")) {
                    pushFragment(new Edit_premium_lawyer_profile(),"Edit profile");

                }else {
                    pushFragment(new Login_freg(),"login");
                }


        }
    }
    public static void Check_String_NULL_Value( TextView textview, String text) {


        if(text.equalsIgnoreCase("null")==true)
        {
            textview.setHint("");
            //textview.setText("Enter Value here");
        }else {

            textview.setText(Html.fromHtml(Navigation_activity.Convert_String_First_Letter(text)));
        }

    }
    public static void Check_Editext_NULL_Value(EditText textview, String text) {


        if(text.equalsIgnoreCase("null")==true)
        {
         //   textview.setText("Enter Value here");
           textview.setHint("");
        }else {

            textview.setText(Html.fromHtml(Navigation_activity.Convert_String_First_Letter(text)));
        }

    }
    public static String  Convert_String_First_Letter(String convert_string)
    {
        String upperString ;

        if(convert_string.length() > 0)
        {
            upperString = convert_string.substring(0,1).toUpperCase() + convert_string.substring(1);
        }else {
            upperString=" ";
        }
        return upperString;
    }
    private void pushFragment(Fragment fragment, String add_to_backstack) {
        if (fragment == null)
            return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.main_fram_layout, fragment);
                ft.addToBackStack(add_to_backstack);
                ft.commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        Log.e("count_621",""+count);

        if (count == 1) {
            if (doubleBackToExitPressedOnce) {
                Log.e("count_628",""+count);
                super.onBackPressed();
                super.finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {

            getSupportFragmentManager().popBackStack();
            Log.e("count_629",""+count);

           /* String title = fragmentManager.getBackStackEntryAt(count - 2).getName();
            Log.e("count_629",""+count);
            super.onBackPressed();
         */ //  super.onBackPressed();
           // Log.e("onBackPressetitle", "" + title);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.navigation_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_us) {
            // Handle the camera action
            pushFragment(new About_us(),"aboutus");

        } else if (id == R.id.nav_contactus) {

            pushFragment(new Contact_us(),"aboutus");
        } else if (id == R.id.nav_logout) {

            if (CheckNetwork.isNetworkAvailable(Navigation_activity.this)) {
                Call_LOGOUT_API();
            } else {
                Toast.makeText(Navigation_activity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }



        } else if (id == R.id.nav_my_account) {

            if(Login_preference.getLogin_flag(Navigation_activity.this).equalsIgnoreCase("1")) {

                    Bundle b=new Bundle();
                    b.putString("user_id",Login_preference.getuser_id(Navigation_activity.this));
                    Fragment myFragment = new Edit_premium_lawyer_profile();
                    myFragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack("").commit();


            }else {
                pushFragment(new Login_freg(),"login");
            }

        } else if (id == R.id.nav_my_article) {
            if(Login_preference.getLogin_flag(Navigation_activity.this).equalsIgnoreCase("1")) {
                if(My_Preference.get_premium_lawyer(Navigation_activity.this).equalsIgnoreCase("premium")){
                    Bundle b=new Bundle();
                    b.putString("user_id",Login_preference.getuser_id(Navigation_activity.this));
                    Fragment myFragment = new Article_freg();
                    myFragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fram_layout, myFragment).addToBackStack("").commit();

                }
            }else {
                pushFragment(new Login_freg(),"login");
            }

        }
        else if (id == R.id.nav_signin) {
            pushFragment(new Login_freg(),"register");
        }

        else if (id == R.id.nav_register) {
            pushFragment(new Register_freg(),"register");
        }
        else if (id == R.id.nav_view) { }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Call_LOGOUT_API() {

        Log.e("login_userid", "" + Login_preference.getuser_id(this));
        ApiInterface apii = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> Logout = apii.Logout(Login_preference.getuser_id(this));

        Logout.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response_logout", "" + response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String status = jsonObject.getString("status");
                    Log.e("status", "" + status);
                    String meassg = jsonObject.getString("msg");
                    Log.e("message_token", "" + meassg);
                    if (status.equalsIgnoreCase("success")) {
                        Login_preference.mPrefs = PreferenceManager.getDefaultSharedPreferences(Navigation_activity.this);
                        Login_preference.prefsEditor = Login_preference.mPrefs.edit();

                        Login_preference.prefsEditor.remove("user_id").apply();
                        Login_preference.prefsEditor.remove("premiunm_lawyer").apply();
                        Login_preference.prefsEditor.remove("countryid").apply();
                        Login_preference.prefsEditor.remove("Countryname").apply();
                        Login_preference.prefsEditor.apply();
                        Login_preference.prefsEditor.commit();

                        String lo = "0";
                        Login_preference.setLogin_flag(Navigation_activity.this, lo);

                       /* Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {

                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent intent = new Intent(Navigation_activity.this, Navigation_activity.class);
                                            startActivity(intent);

                                        }
                                    }, 50);
                                }else {
                                    Toast.makeText(Navigation_activity.this, "There are some problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
*/
                        Intent intent = new Intent(Navigation_activity.this, Navigation_activity.class);
                        startActivity(intent);


                    } else if (status.equalsIgnoreCase("error")) {
                        // Toast.makeText(Navigation_activity.this, "" + meassg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("", "" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Navigation_activity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}