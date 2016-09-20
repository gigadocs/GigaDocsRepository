package com.datappsinfotech.gigadocs.utils.gigadocsutils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.R;


public class GigaDocsMessages {

    public static void messageShort(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void messageShort(@NonNull Context context, @NonNull ViewGroup viewGroup, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(viewGroup,message, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        snackbar.setActionTextColor(ContextCompat.getColor(context,R.color.white));
        snackbar.show();
    }


    public static void messageShort(@NonNull Context context,@NonNull View view, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        snackbar.setActionTextColor(ContextCompat.getColor(context,R.color.white));
        snackbar.show();
    }
    public static void messageLong(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void messageLong(@NonNull Context context,@NonNull ViewGroup viewGroup, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(viewGroup,message, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        snackbar.setActionTextColor(ContextCompat.getColor(context,R.color.white));
        snackbar.show();
    }
    public static void messageLong(@NonNull Context context,@NonNull View view, @NonNull String message) {
        Snackbar snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        snackbar.setActionTextColor(ContextCompat.getColor(context,R.color.white));
        snackbar.show();
    }


    public static void messageAlert(@NonNull Context context, @NonNull String buttonName, @NonNull String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(buttonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    public static void messageAlertWithTitle(@NonNull Context context, String buttonName, @NonNull String title, @NonNull String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView titleTextView = new TextView(context);
        titleTextView.setBackgroundResource(R.color.colorPrimary);
        titleTextView.setLayoutParams(layoutParams);
        titleTextView.setText(title);
        titleTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        titleTextView.setGravity(Gravity.CENTER);
        builder.setCustomTitle(titleTextView);
        builder.setMessage(message);
        builder.setPositiveButton(buttonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

//    public static void messageInternetDialogWithTile(@NonNull final Context context, @NonNull final InternetFailedCommunicator internetFailedCommunicators, @NonNull String title, @NonNull final String message, final int methodFlag) {
//        try {
//            final Dialog dialog = new Dialog(context);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.layout_internet_failed);
//            dialog.show();
//            dialog.setCancelable(false);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            TextView interFailedTitleTextView = (TextView) dialog.findViewById(R.id.text_viewInternetFailedTitle);
//            TextView interFailedMessageTextView = (TextView) dialog.findViewById(R.id.text_viewInternetFailedMessage);
//
//            interFailedTitleTextView.setText(title);
//            interFailedMessageTextView.setText(message);
//            Button retryButton = (Button) dialog.findViewById(R.id.buttonRetry);
//            Button cancelButton = (Button) dialog.findViewById(R.id.buttonCancel);
//            retryButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    internetFailedCommunicators.checkInternetConnection(CheckInternetConnection.isConnectionAvailable(context), dialog, message,methodFlag, false);
//                }
//            });
//            cancelButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    internetFailedCommunicators.checkInternetConnection(false, dialog, message,methodFlag, true);
//                }
//            });
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

}
