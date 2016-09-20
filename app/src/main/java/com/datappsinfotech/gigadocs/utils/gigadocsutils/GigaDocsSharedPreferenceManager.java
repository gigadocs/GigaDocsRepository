package com.datappsinfotech.gigadocs.utils.gigadocsutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * Created by Thordivya on 19-05-2016.
 */
public class GigaDocsSharedPreferenceManager {


  public static Context preferencesContext;

    public static void setKey(@NonNull Context context, @NonNull String key, @NonNull String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void setKey(@NonNull Context context, @NonNull String key, @NonNull boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setKey(@NonNull Context context, @NonNull String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void setKey(@NonNull Context context, @NonNull String key, float value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.apply();
    }



    public static String getKey(@NonNull Context context, @NonNull String key,String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,value);

    }

    public static long getKey(@NonNull Context context, @NonNull String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static float getKey(@NonNull Context context, @NonNull String key, float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }
    public static boolean getKey(@NonNull Context context, @NonNull String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void clearKeys(@NonNull Context context) {
        context.getSharedPreferences(GigadocsConstants.GIGADOCS, Context.MODE_PRIVATE).edit().clear().commit();
    }
}
