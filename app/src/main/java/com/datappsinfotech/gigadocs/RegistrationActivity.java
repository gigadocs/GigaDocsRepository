package com.datappsinfotech.gigadocs;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.utils.dtos.RegistrationActivityDTO;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsUtils;
import com.datappsinfotech.gigadocs.utils.communicators.DoctorSpecialisation;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;
import com.datappsinfotech.gigadocs.validations.Validations;
import com.datappsinfotech.gigadocs.widgets.AVUtils;
import com.datappsinfotech.gigadocs.widgets.InternetFailureDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener,DoctorSpecialisation ,Validations{


    @BindView(R.id.edit_textDoctorName)
            EditText editTextDoctorName;
    @BindView(R.id.edit_textDoctorEmail)
            EditText editTextDoctorEmail;
    @BindView(R.id.edit_textDoctorPassword)
            EditText editTextDoctorPassword;
    @BindView(R.id.edit_textMobile)
            EditText editTextDoctorMobile;
    @BindView(R.id.spinnerSpeciality)
            Spinner spinnerSpeciality;
    @BindView(R.id.buttonNext)
            Button buttonNext;



    public static Dialog internetFailure,dailog;

    public  ArrayList<String> specialityList,specialityId;
    String doctorSpecialityList,doctorSpecialityId;
    ArrayAdapter doctorSpecialisationArrayAdapter;
    String getSelectedSpeciality;
    public static RegistrationActivityDTO registrationActivityDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registration);
            ButterKnife.bind(this);
            registrationActivityDTO = new RegistrationActivityDTO();
            internetFailure = InternetFailureDialog.getAnimDialog(this);
            dailog= AVUtils.getAnimDialog(this);
            Button buttonRetry = (Button)internetFailure.findViewById(R.id.buttonRetry);
            if(buttonNext !=null) {
                buttonNext.setOnClickListener(this);
            }if (buttonRetry!=null){
                buttonRetry.setOnClickListener(this);
            }
            try {
                if (CheckInternetConnection.isConnectionAvailable(getApplicationContext())){
                    new RegistrationAsyncTask().execute();
                }else {
                    internetFailure.show();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            doctorSpecialisationItemClickListner();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Override
    public void doctorSpecialisationItemClickListner() {
        try {
            spinnerSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getSelectedSpeciality= specialityId.get(position);
                    Toast.makeText(getApplicationContext(),getSelectedSpeciality,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        try {
        switch (v.getId()) {

            case R.id.buttonNext:
              if (validationSuccess()) {
                  registrationActivityDTO.setName_RegistrationActivity(editTextDoctorName.getText().toString());
                  registrationActivityDTO.setEmail_RegistrationActivity(editTextDoctorEmail.getText().toString());
                  registrationActivityDTO.setPassword_RegistrationActivity(editTextDoctorPassword.getText().toString());
                  registrationActivityDTO.setMobile_RegistrationActivity(editTextDoctorMobile.getText().toString());
                  registrationActivityDTO.setSpeciality_RegistrationActivity(getSelectedSpeciality);
                  startActivity(new Intent(this, RegistrationDetailsActivity.class));
              }
                break;
            case R.id.buttonRetry:
            try {
                if (CheckInternetConnection.isConnectionAvailable(getApplicationContext())) {
                    new RegistrationAsyncTask().execute();
                    internetFailure.dismiss();
                } else{
                    internetFailure.show();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    @Override
    public boolean validationSuccess() {
        try {
            if (GigaDocsUtils.checkEmptyString(editTextDoctorName.getText().toString())) {
                editTextDoctorName.setError("Name should not be Empty");
                editTextDoctorName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        editTextDoctorName.setError(null);
                        return false;
                    }
                });
                return false;
            }
            if (!GigaDocsUtils.isValidEmail(editTextDoctorEmail.getText().toString())) {
                editTextDoctorEmail.setError("Please Enter Valid Email-ID");
                return false;
            }
            if (editTextDoctorPassword.getText().toString().length() < 6){
                editTextDoctorPassword.setError("Password must be grater than 5");
                return false;
            }
            if (!GigaDocsUtils.isValidIndianMobileNumber(editTextDoctorMobile.getText().toString())){
                editTextDoctorMobile.setError("please enter valid Indian Mobile Number");
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;

    }
    public  class RegistrationAsyncTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           dailog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                return WebServiceClass.getSpecialitiesService();
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
                    JSONArray jsonArray = jsonObject.getJSONArray(GigaDocsAPIConstants.MESSAGES);
                    specialityList = new ArrayList<>();
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        doctorSpecialityList = (jsonArray.getJSONObject(i).getString(GigaDocsAPIConstants.SPECIALITY));
                        specialityList.add(doctorSpecialityList);
                    }
                    specialityId = new ArrayList<>();
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        doctorSpecialityId =(jsonArray.getJSONObject(i).getString(GigaDocsAPIConstants.SPECIALITIES_ID));
                        specialityId.add(doctorSpecialityId);
                    }
                    doctorSpecialisationArrayAdapter = new ArrayAdapter<>(RegistrationActivity.this,android.R.layout.simple_spinner_item, specialityList);
                    doctorSpecialisationArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSpeciality.setAdapter(doctorSpecialisationArrayAdapter);
                    LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    spinnerSpeciality.setLayoutParams(params);
                    dailog.dismiss();

                }else{
                    Toast.makeText(  RegistrationDetailsActivity.context, "Validate Credentials", Toast.LENGTH_SHORT).show();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}

