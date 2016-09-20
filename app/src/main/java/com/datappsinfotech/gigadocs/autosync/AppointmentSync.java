package com.datappsinfotech.gigadocs.autosync;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.LoginActivity;
import com.datappsinfotech.gigadocs.database.AppointmentDataBaseHelper;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsDataUnavailableException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsTimeOutException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsUnAutorisedException;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hemanthkaipa on 8/24/2016.
 */
@SuppressWarnings("ALL")
public class AppointmentSync {
    Context context;
    AppointmentDataBaseHelper appointmentDataBaseHelper;
    HashMap<String,String> appointmentSyncMap;
    public AppointmentSync(Context context) {
        this.context = context;
    }

    public void appointmentSync(){
        try {
            new AppointmentSyncAsyncTask().execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public class AppointmentSyncAsyncTask extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... params) {
            appointmentSyncMap = new HashMap<>();
            try {
                return WebServiceClass.postAppointmentSYNC(context,appointmentSyncMap);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if(jsonObject.getBoolean(GigaDocsAPIConstants.STATUS)) {
                    Toast.makeText(context, "Appointment is Successfully saved in server", Toast.LENGTH_SHORT).show();
                    appointmentDataBaseHelper = new AppointmentDataBaseHelper(context);
                    Log.d("APPOINTMENT TABLE","DELETED");
                    appointmentDataBaseHelper.deleteTable();
                    new GetAppointmentListAsyncTask().execute();
                }
                }catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    // APPOINTMENT LIST
    public class GetAppointmentListAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.getAppointmentListSyncForDB(context);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                super.onPostExecute(jsonObject);
                try {
                    if (jsonObject.getBoolean(GigaDocsAPIConstants.STATUS)){
                        JSONArray message= jsonObject.getJSONArray("message");
                        for(int i=0; i< message.length();i++){
                            JSONObject obj= message.getJSONObject(i);
                            String mobile=obj.getString("patient_mobile");
                            String name=obj.getString("patient_name");
                            String date=obj.getString("date_appoinment");
                            String slot=obj.getString("slot_time");
                            String apo_id=obj.getString("apo_id");
                            String server_id=obj.getString("server_id");
                            String patient_id =obj.getString("patient_id");
                            String prescription_status =obj.getString("prescription_status");
                            Log.i("Test0987", obj.toString());
                            boolean ifInserted= appointmentDataBaseHelper.insertData(mobile, name, date, slot, apo_id, server_id, patient_id,prescription_status );
                            if(ifInserted){
                                Toast.makeText(context, "Data Synked Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(context, "Data Not Synked", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Throwable e) {
                    e.printStackTrace();
                }
            }catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
