package com.datappsinfotech.gigadocs.autosync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.database.PatientDataBaseHelper;
import com.datappsinfotech.gigadocs.fragments.PatientFragment;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hemanthkaipa on 8/24/2016.
 */
public class PatientSync {
    Context context;


    public static HashMap<String,String> patientSyncHashMap;
    public PatientSync(Context context) {
        this.context = context;
    }
    public void patientSync(){
        try{

            new PatientSyncAsyncTask().execute();
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public class PatientSyncAsyncTask extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
               return WebServiceClass.postPatientSync(context,patientSyncHashMap);
            }catch (Throwable e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject.getBoolean(GigaDocsAPIConstants.STATUS)) {
                    Toast.makeText(context, "Patient is Successfully saved in server", Toast.LENGTH_SHORT).show();
                    PatientFragment.patientDataBaseHelper.deleteTable();
                    Log.d("TABLE DELETED","TABLE DELETED");
                    new GetPatientListAsyncTask().execute();
                }
            }catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    public class GetPatientListAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.getPatientListSyncForDB(context);
            }catch (Throwable e) {
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
                            String mobile=obj.getString("mobile");
                            String name=obj.getString("name");
                            String email=obj.getString("email");
                            String description=obj.getString("description");
                            String pid=obj.getString("pid");
                            String server_id=obj.getString("server_id");

                           PatientFragment.patientDataBaseHelper.insertData(mobile, name, description, email,pid, server_id);
                        }
                    }
                }catch (Throwable e) {
                    e.printStackTrace();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
