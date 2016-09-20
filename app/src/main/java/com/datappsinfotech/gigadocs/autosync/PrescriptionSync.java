package com.datappsinfotech.gigadocs.autosync;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.database.PrescriptionDataBaseHelper;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsDataUnavailableException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsTimeOutException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsUnAutorisedException;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsSharedPreferenceManager;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PrescriptionSync {
    Context context;
    public static PrescriptionDataBaseHelper prescriptionDataBaseHelper;



    public PrescriptionSync(Context context) {
        this.context = context;
    }

     public void prescriptionSync (){
         try {
             new PrescriptionAsyncTask().execute();
         } catch (Throwable e) {
             e.printStackTrace();
         }
     }

    public class PrescriptionAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        public PrescriptionAsyncTask() {
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.PostPrescriptionSync(context);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                super.onPostExecute(jsonObject);
                prescriptionDataBaseHelper = new PrescriptionDataBaseHelper(context);
                prescriptionDataBaseHelper.deleteTable();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
