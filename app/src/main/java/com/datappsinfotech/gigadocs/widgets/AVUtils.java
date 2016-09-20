package com.datappsinfotech.gigadocs.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.datappsinfotech.gigadocs.R;

/**
 * Created by sandeep123 on 6/30/2016.
 */
public class AVUtils {
   static Dialog dialog = null;


    @NonNull
    public static Dialog getAnimDialog(@NonNull Context context){
        try {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_anim_indicator);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return dialog;
    }
}
