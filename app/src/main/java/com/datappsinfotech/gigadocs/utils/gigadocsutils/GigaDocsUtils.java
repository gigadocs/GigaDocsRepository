package com.datappsinfotech.gigadocs.utils.gigadocsutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sandeep123 on 6/14/2016.
 */
public class GigaDocsUtils {

    public static boolean checkEmptyString(String data) {
        return TextUtils.isEmpty(data);
    }

    public static boolean checkMatchString(@NonNull String src, @NonNull String des) {
        return TextUtils.equals(src, des);
    }

    public static boolean isValidIndianMobileNumber(@NonNull String mobileNumber) {
        return mobileNumber.matches(GigadocsConstants.INDIAN_MOBILE_NUMBER_PATTERN);
    }

    public static boolean isValidEmail(@NonNull String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @NonNull
    public static ProgressDialog getProgressDialog(@NonNull Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(GigadocsConstants.LOADING);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }

}
