package com.datappsinfotech.gigadocs.fragments;
import android.app.Dialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.LoginActivity;
import com.datappsinfotech.gigadocs.PrescriptionActivity;
import com.datappsinfotech.gigadocs.R;
import com.datappsinfotech.gigadocs.database.AppointmentDataBaseHelper;
import com.datappsinfotech.gigadocs.database.PatientDataBaseHelper;
import com.datappsinfotech.gigadocs.database.PrescriptionDataBaseHelper;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;
import com.datappsinfotech.gigadocs.widgets.AVUtils;
import com.datappsinfotech.gigadocs.widgets.InternetFailureDialog;
import com.datappsinfotech.gigadocs.widgets.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("ALL")
public class LogoutFragment extends Fragment {
    Dialog dialog,internetFailureDialog;
    public static HashMap<String,String> patientSyncMap;
    public static HashMap<String,String> appointmentSyncMap;
    UserSessionManager session;
    public static PatientDataBaseHelper patientDataBaseHelper;
    public static AppointmentDataBaseHelper appointmentDataBaseHelper;
    public static PrescriptionDataBaseHelper prescriptionDataBaseHelper;
    public  String apo_id;
    Button buttonRetry,buttonDismiss;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_logout, container, false);
        try {
            dialog = AVUtils.getAnimDialog(getContext());
            internetFailureDialog = InternetFailureDialog.getAnimDialog(getContext());
             buttonRetry = (Button)internetFailureDialog.findViewById(R.id.buttonRetry);
             buttonDismiss = (Button)internetFailureDialog.findViewById(R.id.buttonDismiss);

            if (CheckInternetConnection.isConnectionAvailable(getContext())){
                try {
                    dbInfo();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }else{
                internetFailureDialog.show();
            }
            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (CheckInternetConnection.isConnectionAvailable(getContext())){
                            try {
                                dbInfo();
                            }catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }else{
                            internetFailureDialog.show();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
            });
            buttonDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    internetFailureDialog.dismiss();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return  view;
    }
    public static void getId(String apo_id){
     apo_id= apo_id;

    }
    public void dbInfo(){
        try {
            patientDataBaseHelper = new PatientDataBaseHelper(getContext());
            appointmentDataBaseHelper = new AppointmentDataBaseHelper(getContext());
            prescriptionDataBaseHelper=new PrescriptionDataBaseHelper(getContext());
            try {
                    new PateintSyncAsyncTask().execute();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                session = new UserSessionManager(getContext());
                session.logoutUser();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public class PateintSyncAsyncTask extends AsyncTask<Void,Void,JSONObject>{
    @Override
    protected void onPreExecute() {
        try {
            super.onPreExecute();
            dialog.show();
            dialog.setCancelable(false);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
           return WebServiceClass.postPatientSync(getContext(),patientSyncMap);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        try {
            if (jsonObject.getBoolean(GigaDocsAPIConstants.STATUS)) {
                new AppointmentSyncAsyncTask().execute();
            }else{
                Toast.makeText(getContext(), "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
    public class AppointmentSyncAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {

            Log.i("APPOINTMENT LOGOUT FRAGMENT", "LOGOUT APPOINTMENT SYNC");
            try {
                   return WebServiceClass.postAppointmentSYNC(getContext(),appointmentSyncMap);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if(jsonObject.getBoolean(GigaDocsAPIConstants.STATUS)) {

                    if (PrescriptionActivity.flag){
                        new PrescriptionAsyncTask().execute();
                    }else{
                        appointmentDataBaseHelper.deleteTable();
                        patientDataBaseHelper.deleteTable();
                    }
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Everything is Synced to Server", Toast.LENGTH_LONG).show();
                        System.out.println("everything is synced successfully");
                    }


            }catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    public class PrescriptionAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.PostPrescriptionSync(getContext());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                super.onPostExecute(jsonObject);
                appointmentDataBaseHelper.deleteTable();
                patientDataBaseHelper.deleteTable();
                prescriptionDataBaseHelper.deleteTable();
                Toast.makeText(getContext(), "LoggedOut successfully ", Toast.LENGTH_LONG).show();
                System.out.println("everything is synced successfully");
                dialog.dismiss();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
