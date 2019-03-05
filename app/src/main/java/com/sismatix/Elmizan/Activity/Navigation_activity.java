package com.sismatix.Elmizan.Activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sismatix.Elmizan.Fregment.Article_freg;
import com.sismatix.Elmizan.Fregment.Directory_freg;
import com.sismatix.Elmizan.Fregment.Home_freg;
import com.sismatix.Elmizan.Fregment.Library_freg;
import com.sismatix.Elmizan.Fregment.Login_freg;
import com.sismatix.Elmizan.Fregment.Register_freg;
import com.sismatix.Elmizan.Fregment.Video_freg;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.R;

import java.util.Locale;

public class Navigation_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public  BottomNavigationView bottom_navigation;
    DrawerLayout drawer;
    public static Toolbar toolbar;
    NavigationView navigationView;
    public  static ImageView iv_nav_currency_image,iv_nav_logo;
    public  static TextView tv_nav_title,tv_nav_user_name;
    Bundle b;
    String Screen,register;
    public static AssetManager am ;
    public static Typeface typeface,tf,medium;
    LinearLayout lv_withlogin_header,lv_withoutlogin_header,withoutloginicon;
    MenuItem nav_register,nav_contactus,nav_signin,nav_myaccount,nav_myarticle,nav_notification,nav_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        AllocateMemory();
        setSupportActionBar(toolbar);
        SET_FONT_STYLE();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

        }else{
            lv_withlogin_header.setVisibility(View.GONE);
            lv_withoutlogin_header.setVisibility(View.VISIBLE);
            withoutloginicon.setVisibility(View.INVISIBLE);
            nav_register.setVisible(true);
            nav_contactus.setVisible(true);
            nav_signin.setVisible(true);
            nav_myaccount.setVisible(false);
            nav_myarticle.setVisible(false);
            nav_notification.setVisible(false);
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
        if(b!=null)
        {
            Screen=getIntent().getExtras().getString("screen");

            Log.e("lofin",""+Screen);
            if(Screen.equalsIgnoreCase("Login")==true){
                pushFragment(new Login_freg(),"Login");
            }else if(Screen.equalsIgnoreCase("register")==true){
                pushFragment(new Register_freg(),"register");
            }
        }
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
        iv_nav_currency_image = findViewById(R.id.iv_nav_currency_image);
        iv_nav_logo = findViewById(R.id.iv_nav_logo);
        tv_nav_title = findViewById(R.id.tv_nav_title);
        View header = navigationView.getHeaderView(0);
        lv_withlogin_header=(LinearLayout) header.findViewById(R.id.lv_withlogin_header);
        lv_withoutlogin_header=(LinearLayout) header.findViewById(R.id.lv_withoutlogin_header);
        tv_nav_user_name=(TextView) header.findViewById(R.id.tv_nav_user_name);
        withoutloginicon=(LinearLayout) findViewById(R.id.withoutloginicon);
        Menu menu =navigationView.getMenu();
        nav_register = menu.findItem(R.id.nav_register);
        nav_contactus = menu.findItem(R.id.nav_contactus);
        nav_signin = menu.findItem(R.id.nav_signin);
        nav_myaccount = menu.findItem(R.id.nav_my_account);
        nav_myarticle = menu.findItem(R.id.nav_my_article);
        nav_notification = menu.findItem(R.id.nav_notification);
        nav_logout = menu.findItem(R.id.nav_logout);

    }

    public DrawerLayout getmDrawerLayout() {
        return drawer;
    }
    private void Bootom_Navigation_view() {

        /*TextView textView = (TextView) bottom_navigation.findViewById(R.id.bottom_navigation).findViewById(R.id.largeLabel);
        textView.setTextSize(8);
*/
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
                pushFragment(new Directory_freg(),"Home");
                break;
            case R.id.bottom_nav_article:
                pushFragment(new Article_freg(),"Home");
                break;
              /*  if (loginflagmain.equalsIgnoreCase("1") || loginflagmain == "1") {
                    pushFragment(new Wishlist_fragment(),"Wishlist_fragment");
                    viewPager.setCurrentItem(2);
                    break;
                } else {
                    Toast.makeText(this, "Please try to login.", Toast.LENGTH_SHORT).show();
                    break;
                }*/
            case R.id.bottom_nav_library:
                pushFragment(new Library_freg(),"Cart");

                break;
            case R.id.bottom_nav_myaccount:
                pushFragment(new Video_freg(),"Cart");

            /*    if (loginflagmain.equalsIgnoreCase("1") || loginflagmain == "1") {
                    pushFragment(new AccountTabs(),"My Account");
                    viewPager.setCurrentItem(4);
                    break;

                } else {
                    pushFragment(new Account(),"Login_myaccount");
                    viewPager.setCurrentItem(4);
                    break;
                }*/
        }
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
        } else if (id == R.id.nav_contactus) {

        } else if (id == R.id.nav_logout) {
            String lo="0";
            Login_preference.setLogin_flag(this,lo);
            Intent intent=new Intent(this,Navigation_activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_my_account) {

        } else if (id == R.id.nav_my_article) {

        } else if (id == R.id.nav_notification) {

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
}