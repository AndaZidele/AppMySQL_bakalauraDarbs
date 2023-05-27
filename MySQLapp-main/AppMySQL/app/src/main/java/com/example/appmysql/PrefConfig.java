package com.example.appmysql;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {

    private static final String UserEmail = "com.example.appmongodb";
    private static final String UserEmailKey = "pre_key";

    public static void saveUserEmail(Context context, String epasts){
        SharedPreferences pref = context.getSharedPreferences(UserEmail, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(UserEmailKey, epasts);
        editor.apply();

    }

    public static String loadEpasts(Context context){
        SharedPreferences pref = context.getSharedPreferences(UserEmail, Context.MODE_PRIVATE);
        return pref.getString(UserEmailKey, "Not Logged In");
    }

//    public static String loadEpastsCart(Class<CartActivity> context){
//        SharedPreferences pref = context.getSharedPreferences(UserEmail, Context.MODE_PRIVATE);
//        return pref.getString(UserEmailKey, "Not Logged In");
//    }

    /*
    private static final String UserIdKey = "pre_key_id";
    public static void saveUserId(Context context, int id){
        SharedPreferences pref = context.getSharedPreferences(UserEmail, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(UserIdKey, id);
        editor.apply();

    }

    public static int loadId(Context context){
        SharedPreferences pref = context.getSharedPreferences(UserEmail, Context.MODE_PRIVATE);
        return pref.getInt(UserIdKey, -1);
    }*/


}
