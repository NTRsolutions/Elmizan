package com.sismatix.Elmizan.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Login_preference {
    public static SharedPreferences mPrefs;
    public static SharedPreferences.Editor prefsEditor;



    public static void setLogin_flag(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("login_flag", value);
        prefsEditor.commit();
    }
    public static String getLogin_flag(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("login_flag", "");
    }

    public static void setuser_id(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("user_id", value);
        prefsEditor.commit();
    }
    public static String getuser_id(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("user_id", "");
    }
    public static void setemail(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("user_email", value);
        prefsEditor.commit();
    }
    public static String getemail(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("user_email", "");
    }
    public static void setuser_name(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("user_name", value);
        prefsEditor.commit();
    }
    public static String getuser_name(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("user_name", "");
    }
    public static void setuser_short_desc(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("user_desc", value);
        prefsEditor.commit();
    }
    public static String getuser_short_desc(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("user_desc", "");
    }
    public static void setuser_profile(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("user_avatar_url", value);
        prefsEditor.commit();
    }
    public static String getuser_profile(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("user_avatar_url", "");
    }
    public static void setuser_countryid(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("countryid", value);
        prefsEditor.commit();
    }
    public static String getuser_countryid(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("countryid", "");
    }
    public static void setdevicetoken(Context context, String value)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("devicetoken", value);
        prefsEditor.commit();
    }
    public static String getdevicetoken(Context context)
    {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString("devicetoken", "");
    }
}
