package com.datappsinfotech.gigadocs.utils.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

/**
 * Created by Guru on 15-Jun-16.
 */
public class CheckInternetConnection {

    public static boolean isConnectionAvailable(@NonNull Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


}
