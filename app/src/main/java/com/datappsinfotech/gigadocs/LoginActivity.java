package com.datappsinfotech.gigadocs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.database.AppointmentDataBaseHelper;
import com.datappsinfotech.gigadocs.database.PatientDataBaseHelper;
import com.datappsinfotech.gigadocs.utils.dtos.LoginActivityDTO;
import com.datappsinfotech.gigadocs.utils.dtos.PatientDTO;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsSharedPreferenceManager;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsUtils;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;
import com.datappsinfotech.gigadocs.validations.Validations;
import com.datappsinfotech.gigadocs.widgets.AVUtils;
import com.datappsinfotech.gigadocs.widgets.InternetFailureDialog;
import com.datappsinfotech.gigadocs.widgets.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener,Validations{

    @BindView(R.id.buttonSignUp)
    Button buttonSignUp;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.text_viewForgotPassword)
    TextView forgotPassword;
    @BindView(R.id.edit_textLoginEmail)
    TextInputEditText editTextEmail;
    @BindView(R.id.edit_textLoginPassword)
    TextInputEditText editTextPassword;
    @BindView(R.id.check_boxRememberMe)
    CheckBox saveLoginCheckBox;
    private static final int REQUEST_WRITE_STORAGE = 111;
    public static Context context;
    public static LoginActivityDTO loginActivityDTO;
    public static String loginToken;
    PatientDataBaseHelper patientDataBaseHelper;
    AppointmentDataBaseHelper appointmentDataBaseHelper;
    private String username,password;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    public static List<String> al1;
    UserSessionManager session;
    Button buttonRetry,buttonDismiss;
    public static Dialog dialog,internetFailureDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            try {
                setContentView(R.layout.activity_login);
                boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE },
                            REQUEST_WRITE_STORAGE);
                }

                ButterKnife.bind(this);
                dialog = AVUtils.getAnimDialog(this);
                internetFailureDialog = InternetFailureDialog.getAnimDialog(this);
                 buttonRetry = (Button)internetFailureDialog.findViewById(R.id.buttonRetry);
                 buttonDismiss = (Button)internetFailureDialog.findViewById(R.id.buttonDismiss);
                session = new UserSessionManager(getApplicationContext());
                context = getBaseContext();
                loginActivityDTO = new LoginActivityDTO();
                buttonLogin.setClickable(false);
                buttonSignUp.setClickable(false);
                if (buttonSignUp!=null) {
                    buttonSignUp.setOnClickListener(this);
                }
                if(buttonLogin!=null){
                    buttonLogin.setOnClickListener(this);
                }
                if (forgotPassword!=null){
                    forgotPassword.setOnClickListener(this);
                }
                if (buttonRetry!=null){
                    buttonRetry.setOnClickListener(this);
                }
                if (buttonDismiss!=null){
                    buttonDismiss.setOnClickListener(this);
                }
                loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                loginPrefsEditor = loginPreferences.edit();

                saveLogin = loginPreferences.getBoolean("saveLogin", false);
                if (saveLogin == true) {
                    editTextEmail.setText(loginPreferences.getString("username", ""));
                    editTextPassword.setText(loginPreferences.getString("password", ""));
                    saveLoginCheckBox.setChecked(true);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("Request Code", String.valueOf(requestCode));

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode)
            {
                case REQUEST_WRITE_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        //reload my activity with permission granted or use the features what required the permission
                    } else
                    {
                        Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.buttonLogin:
                    if (validationSuccess()) {
                        loginActivityDTO.setEmail_LoginActivity(editTextEmail.getText().toString());
                        loginActivityDTO.setPassword_LoginActivity(editTextPassword.getText().toString());
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);

                        username = editTextEmail.getText().toString();
                        password = editTextPassword.getText().toString();

                        if (saveLoginCheckBox.isChecked()) {
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", username);
                            loginPrefsEditor.putString("password", password);
                            loginPrefsEditor.commit();
                        } else {
                            loginPrefsEditor.clear();
                            loginPrefsEditor.commit();
                        }
                        buttonLogin.setClickable(true);

                        session.createUserLoginSession();
                        try {
                            if (CheckInternetConnection.isConnectionAvailable(getApplicationContext())) {
                                new LoginAsyncTask().execute();
                            } else{
                                internetFailureDialog.show();
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(LoginActivity.this, "Wrong Credential", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.buttonSignUp:
                    try {
                        buttonSignUp.setClickable(true);
                        startActivity(new Intent(this,RegistrationActivity.class));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.text_viewForgotPassword:
                    try {
                        startActivity(new Intent(this, ForgotPasswordActivity.class));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.buttonRetry:
                    try {
                        if (CheckInternetConnection.isConnectionAvailable(getApplicationContext())) {
                            new LoginAsyncTask().execute();
                        } else{
                            internetFailureDialog.show();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.buttonDismiss:
                    internetFailureDialog.dismiss();
                    break;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validationSuccess() {

        try {
            if (!GigaDocsUtils.isValidEmail(editTextEmail.getText().toString())){
                editTextEmail.setError("Invalid Email");
                return false;
            }else if (editTextPassword.getText().toString().length()<1){
                editTextPassword.setError("password must be greater than 6");
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
    public  class LoginAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            try {
                super.onPreExecute();
                dialog.show();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.postLoginService();
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
                    dialog.dismiss();
                    String name= jsonObject.getString(GigaDocsAPIConstants.SHIFT_TIMINGS);
                    Log.i("Nameeee", name);
                     al1 =  Arrays.asList(name.split(","));
                    Log.i("Apppppppp", al1.toString());
                    JSONArray jsonArray ;
                    jsonArray= jsonObject.getJSONArray(GigaDocsAPIConstants.ALL_DETAILS);
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        GigaDocsSharedPreferenceManager.setKey(context,GigaDocsAPIConstants.DOCTOR_NAME,jsonArray.getJSONObject(i).getString(GigaDocsAPIConstants.DOCTOR_NAME));
                        GigaDocsSharedPreferenceManager.setKey(context,GigaDocsAPIConstants.DOCTOR_MOBILE,jsonArray.getJSONObject(i).getString(GigaDocsAPIConstants.DOCTOR_MOBILE));
                        GigaDocsSharedPreferenceManager.setKey(context,GigaDocsAPIConstants.DOCTOR_SPECIALITY,jsonArray.getJSONObject(i).getString(GigaDocsAPIConstants.DOCTOR_SPECIALITY));
                        GigaDocsSharedPreferenceManager.setKey(context,GigaDocsAPIConstants.DOCTOR_ADDRESS,jsonArray.getJSONObject(i).getString(GigaDocsAPIConstants.DOCTOR_ADDRESS));
                        GigaDocsSharedPreferenceManager.setKey(context,GigaDocsAPIConstants.DOCTOR_TIME,jsonArray.getJSONObject(i).getString(GigaDocsAPIConstants.DOCTOR_TIME));
                    }
                    GigaDocsSharedPreferenceManager.setKey(context,GigaDocsAPIConstants.SHIFT_TIMINGS,jsonObject.getString(GigaDocsAPIConstants.SHIFT_TIMINGS));
                    GigaDocsSharedPreferenceManager.setKey(getApplicationContext(),GigaDocsAPIConstants.TOKEN,jsonObject.getString(GigaDocsAPIConstants.TOKEN));
                    loginToken= jsonObject.getString(GigaDocsAPIConstants.TOKEN);
                    Log.i("Test Login Token",loginToken );
                    getPatientList();
                    getAppointmentList();

                    Intent intent = new Intent(LoginActivity.context,HomeScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginActivity.context.startActivity(intent);
                    Toast.makeText(LoginActivity.context, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.context, "Validate Credentials", Toast.LENGTH_SHORT).show();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }finally {
                try {
                    dialog.dismiss();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class GetPatientListAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.getPatientListForDB(getApplicationContext());
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
                            String mobile=obj.getString("mobile");
                            String name=obj.getString("name");
                            String email=obj.getString("email");
                            String description=obj.getString("description");
//                            String apo_id=obj.getString("apo_id");
                            String pid=obj.getString("pid");
                            String server_id=obj.getString("server_id");
                            patientDataBaseHelper = new PatientDataBaseHelper(getApplicationContext());

                            boolean ifInserted= false;
                            try {
                                ifInserted = patientDataBaseHelper.insertData(mobile, name, description, email,pid, server_id);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
//                            if(ifInserted){
//                                Toast.makeText(getApplicationContext(), "Data Synked Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                                Toast.makeText(getApplicationContext(), "Data Not Synked", Toast.LENGTH_SHORT).show();
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
    public class GetAppointmentListAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.getAppointmentListForDB(getApplicationContext());
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
                            appointmentDataBaseHelper = new AppointmentDataBaseHelper(getApplicationContext());
                            boolean ifInserted= appointmentDataBaseHelper.insertData(mobile, name, date, slot, apo_id, server_id, patient_id,prescription_status );
//                            if(ifInserted){
//                                Toast.makeText(getApplicationContext(), "Data Synked Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                                Toast.makeText(getApplicationContext(), "Data Not Synked", Toast.LENGTH_SHORT).show();
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
    public void getPatientList(){
        try {
            new GetPatientListAsyncTask().execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void getAppointmentList(){
        try {
            new GetAppointmentListAsyncTask().execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
