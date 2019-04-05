package com.sismatix.Elmizan.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.sismatix.Elmizan.Fregment.Login_freg;
import com.sismatix.Elmizan.Preference.Login_preference;
import com.sismatix.Elmizan.Preference.My_Preference;
import com.sismatix.Elmizan.R;

import java.util.Locale;

public class Splash_screen extends AppCompatActivity {
    Button btn_create_account,btn_login;
    LinearLayout lv_splash,lv_skip;
    ImageView iv_splash_icon;
    Spinner spinner_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        lang_arbi();
        setContentView(R.layout.activity_splash_screen);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Login_preference.getLogin_flag(this).equalsIgnoreCase("1")){
            Intent i=new Intent(Splash_screen.this,Navigation_activity.class);
            startActivity(i);
        }
        AllocateMemory();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Splash_screen.this,Navigation_activity.class);
                //Bundle b=new Bundle();
                i.putExtra("screen","Login");
                //i.putExtras(b);
                startActivity(i);
            }
        });
        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Splash_screen.this,Navigation_activity.class);
                Bundle b=new Bundle();
                b.putString("screen","register");
                i.putExtras(b);
                startActivity(i);
            }
        });
        lv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Splash_screen.this,Navigation_activity.class);
                startActivity(i);
            }
        });
        String[] countries=getResources().getStringArray(R.array.theme_array);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.text, countries);
        spinner_theme.setAdapter(adapter);
        spinner_theme.setSelection(2,true);
        spinner_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_value= String.valueOf(spinner_theme.getSelectedItem());
                Log.e("selected_item",""+selected_value);
                if(selected_value.equals("Black")==true){
                    My_Preference.settheme(Splash_screen.this,"1");
                }else {
                    My_Preference.settheme(Splash_screen.this,"0");
                }
                if(My_Preference.gettheme(Splash_screen.this).equalsIgnoreCase("1")==true)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btn_create_account.setBackground(ContextCompat.getDrawable(Splash_screen.this, R.drawable.round_btn_corner_white));
                        btn_login.setBackground(ContextCompat.getDrawable(Splash_screen.this, R.drawable.round_btn_corner_white));

                        lv_splash.setBackground(ContextCompat.getDrawable(Splash_screen.this, R.drawable.splash_bg));

                        btn_login.setTextColor(getResources().getColor(R.color.black));
                        btn_create_account.setTextColor(getResources().getColor(R.color.black));
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btn_create_account.setBackground(ContextCompat.getDrawable(Splash_screen.this, R.drawable.round_btn_corner_black));
                        btn_login.setBackground(ContextCompat.getDrawable(Splash_screen.this, R.drawable.round_btn_corner_black));
                        lv_splash.setBackground(ContextCompat.getDrawable(Splash_screen.this, R.drawable.splashdefolt_bg));
                        btn_login.setTextColor(getResources().getColor(R.color.white));
                        btn_create_account.setTextColor(getResources().getColor(R.color.white));

                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
    private void AllocateMemory() {
        btn_create_account=(Button)findViewById(R.id.btn_create_account);
        btn_login=(Button)findViewById(R.id.btn_login);
        lv_splash=(LinearLayout) findViewById(R.id.lv_splash);
        lv_skip=(LinearLayout) findViewById(R.id.lv_skip);
        iv_splash_icon=(ImageView) findViewById(R.id.iv_splash_icon);
        spinner_theme=(Spinner) findViewById(R.id.spinner_theme);
    }
    //-----convert english to arabic----
    public void lang_arbi() {
            String languageToLoad = "ar";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
}
