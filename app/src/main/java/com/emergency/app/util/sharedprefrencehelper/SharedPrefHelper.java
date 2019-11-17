package com.emergency.app.util.sharedprefrencehelper;


import android.content.Context;
import android.content.SharedPreferences;

import com.emergency.app.models.User;
import com.google.gson.Gson;
public class SharedPrefHelper {
    public static final String SHARED_PREFERENCE_LANGUAGE_KEY = "language";
   public static final String SHARED_PREFERENCE_HIDE_GUIDE = "showGuide";
    public static final String SHARED_PREFERENCE_CLOSE_PAYMENT_ACTIVITY_KEY = "closePayment";

    public static final String SHARED_PREFERENCE_USER_DATA = "userData";

    public static String getSharedString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");

    }

    public static int getSharedInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static float getSharedFloat(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    public static boolean getSharedBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void  setSharedString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void setSharedInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static void setSharedFloat(Context context, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public static void setSharedBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void setSharedOBJECT(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }
    public static Object getSharedOBJECT(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(key, "");
        User objData = new Gson().fromJson(json, User.class);
        return objData;
    }
}
