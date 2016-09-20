package com.datappsinfotech.gigadocs.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;


import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.HomeScreenActivity;
import com.datappsinfotech.gigadocs.R;
import com.datappsinfotech.gigadocs.adapters.CustomAdapter;
import com.datappsinfotech.gigadocs.autosync.PatientSync;
import com.datappsinfotech.gigadocs.database.PatientDataBaseHelper;
import com.datappsinfotech.gigadocs.database.TestDb;
import com.datappsinfotech.gigadocs.utils.dtos.PatientDTO;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsUtils;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigadocsConstants;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;
import com.datappsinfotech.gigadocs.utils.services.WebServiceURL;
import com.datappsinfotech.gigadocs.validations.ViewStatus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PatientFragment extends Fragment implements View.OnClickListener,ViewStatus{

    public static PatientDataBaseHelper patientDataBaseHelper;

//    @BindView(R.id.buttonViewData)
//    Button buttonViewData;
    @BindView(R.id.buttonAdd)
    Button buttonAdd;
    @BindView(R.id.edit_textPatientMobile)
    EditText patientMobile;
    @BindView(R.id.edit_textPatientName)
    EditText patientName;
    @BindView(R.id.edit_textPatientAddress)
    EditText patientAddress;
    @BindView(R.id.edit_textPatientEmailID)
    EditText patientEmail;
    @BindView(R.id.edit_textPatientSearch)
    EditText patientSearchTextInputEditText;
    @BindView(R.id.buttonSearch)
    ImageButton buttonSearch;
    @BindView(R.id.buttonUpdate)
    Button updateButton;
    public static Map<String,String> temp;
    static ArrayList<String> array;
    static ArrayList<String> array1;
    ListView listView;
    ListView listViewOne;
    ListAdapter listAdapter;
    static String test1;
    public static TestDb myTDb;
    public static String apo_id;
    public static String pid;
    public static String getPid;
    public static String responsePatientServerID;
    public static String responsePatientID;
    String pids,name,email,serviceId;
    PatientSync patientSync;

    public static Context context ;
    public PatientFragment() {

    }

    public static PatientDTO patientDTO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        setupUI(view.findViewById(R.id.linearlayout));
        try {
            patientDTO = new PatientDTO();// DTO Object Creation
            patientDataBaseHelper = new PatientDataBaseHelper(getContext());
            ButterKnife.bind(this,view);
            disableUpdateButtonView();
            buttonAdd.setOnClickListener(this);
            buttonSearch.setOnClickListener(this);
            updateButton.setOnClickListener(this);
            patientSync = new PatientSync(getContext());
//            buttonViewData.setOnClickListener(this);


        /* Set Text Watcher listener */
            patientSearchTextInputEditText.addTextChangedListener(passwordWatcher);

//            //List View Code
             listView = (ListView)view.findViewById(R.id.listview);
             listViewOne = (ListView)view.findViewById(R.id.listview_one);

            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String umobile = String.valueOf(parent.getItemAtPosition(position));
                            patientSearchTextInputEditText.setText(umobile);
                            test1=patientSearchTextInputEditText.getText().toString();
                            listView.setVisibility(View.GONE);
                        }
                    }
            );
            listViewOne.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String uname = String.valueOf(parent.getItemAtPosition(position));
                                patientSearchTextInputEditText.setText(uname);
                                patientDataBaseHelper.viewSelectedData(patientSearchTextInputEditText.getText().toString(), test1);
                                String mobile = patientDataBaseHelper.patientDTO.getMobileNo();
                                String name = patientDataBaseHelper.patientDTO.getName();
                                String address = patientDataBaseHelper.patientDTO.getAddress();
                                String email = patientDataBaseHelper.patientDTO.getEmail();
                                getPid = patientDataBaseHelper.patientDTO.getGetPid();
                                patientMobile.setText(mobile);
                                patientName.setText(name);
                                patientAddress.setText(address);
                                patientEmail.setText(email);

                                listViewOne.setVisibility(View.GONE);
                            }
                            catch (Throwable t){
                                t.printStackTrace();
                            }
                        }
                    }
            );
        }
        catch (Throwable t){
            t.printStackTrace();
        }
        return view;
}

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            try {
                listViewOne.setVisibility(View.GONE);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public void onTextChanged(CharSequence s, int start, int  before, int count) {
            try {
                listView.setVisibility(View.VISIBLE);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public void afterTextChanged(Editable s) {
            try {
                if (s.length() < 3) {
                    listView.setVisibility(View.GONE);
                }
                else if(s.length() >= 3) {
                    Cursor c= patientDataBaseHelper.getMobileNo(patientSearchTextInputEditText.getText().toString());
                    int i = 0;
                    array = new ArrayList<>();
                    while(c.moveToNext()){
                        String umobile = c.getString(c.getColumnIndex(patientDataBaseHelper.PCOL2));
                        array.add(umobile);
                        Log.d("Test", array.toString());
                        i++;
                    }
                    listAdapter = new CustomAdapter(getContext(),array);
                    listView.setAdapter(listAdapter);
            }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.buttonAdd:
                    disableUpdateButtonView();
                    try{
                        patientDTO.setMobileNo(patientMobile.getText().toString());
                        patientDTO.setName(patientName.getText().toString());
                        patientDTO.setAddress(patientAddress.getText().toString());
                        patientDTO.setEmail(patientEmail.getText().toString());
                        patientDTO.setServer_id(null);

                        if (validatePatientInfo(patientDTO)) {
//                            String pid= null;
//                            String apo_id= null;
                            boolean isInserted = patientDataBaseHelper.insertData(
                                   patientDTO.getMobileNo(),
                                   patientDTO.getName(),
                                   patientDTO.getAddress(),
                                    patientDTO.getEmail(), " ", " ");
                            if (isInserted) {
                                patientDataBaseHelper.getId(patientMobile.getText().toString(), patientName.getText().toString(),
                                        patientAddress.getText().toString(), patientEmail.getText().toString() );
                                String id= patientDTO.getPatientId();
                                pid= "GIGA00"+id;
                                patientDataBaseHelper.updateDataWithServerID(
                                        patientMobile.getText().toString(),
                                        patientName.getText().toString(),
                                        patientAddress.getText().toString(),
                                        patientEmail.getText().toString(), pid, " ",id);
                                Toast.makeText(getContext(), GigadocsConstants.PATIENT_ADDED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                                patientMobile.setText("");
                                patientName.setText("");
                                patientAddress.setText("");
                                patientEmail.setText("");
                                try {
                                    if (CheckInternetConnection.isConnectionAvailable(getContext())) {
                                        new AddPatientAsyncTask().execute();
                                    }else{
                                        Toast.makeText(getContext(), "Internet is not Available patient Stored in Local", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    break;

                case R.id.buttonSearch:
                    try {
                        if(patientSearchTextInputEditText.getText().toString().equals("")){
                            Toast.makeText(getContext(), "Please Enter mobile number to search", Toast.LENGTH_SHORT).show();
                        } else {
                            enableUpdateButtonView();
                            disableAddButtonView();
                            buttonSearch.setClickable(true);
                            try {
                                Log.i("Test", patientSearchTextInputEditText.getText().toString());
                                listViewOne.setVisibility(View.VISIBLE);
                                Cursor c = patientDataBaseHelper.getName(patientSearchTextInputEditText.getText().toString());
                                array1 = new ArrayList<>();
                                while (c.moveToNext()) {
                                    String uname = c.getString(c.getColumnIndex(patientDataBaseHelper.PCOL3));
                                    array1.add(uname);
                                }

                                listAdapter = new CustomAdapter(getContext(), array1);
                                listViewOne.setAdapter(listAdapter);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.buttonUpdate:
                    try {
                        disableAddButtonView();
                        updateButton.setClickable(true);
                        if (validatePatientInfoForUpdate()) {
                            boolean isInserted= patientDataBaseHelper.updateData(
                                    patientMobile.getText().toString(),
                                    patientName.getText().toString(),
                                    patientAddress.getText().toString(),
                                    patientEmail.getText().toString(), getPid);
                            if (isInserted) {
                                Toast.makeText(getContext(), GigadocsConstants.PATIENT_UPDATE_SUCCESSFULLY,
                                        Toast.LENGTH_LONG).show();
                                enableAddButtonView();
                                disableUpdateButtonView();
                                patientMobile.setText("");
                                patientName.setText("");
                                patientAddress.setText("");
                                patientEmail.setText("");
                            } else {
                                setMessage("Error", "No Data Updated");
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public void setMessage(String title, String Message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle(title);
            builder.setMessage(Message);
            builder.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public boolean validatePatientInfo(@NonNull PatientDTO patientDTO) {
        try {
            if (GigaDocsUtils.checkEmptyString(patientDTO.getMobileNo())) {
                patientMobile.setError("Mobile No should not be Empty");
            }else if (!GigaDocsUtils.isValidIndianMobileNumber(patientDTO.getMobileNo())) {
                patientMobile.setError("please enter valid Indian Mobile Number");
            }else if (GigaDocsUtils.checkEmptyString(patientDTO.getName())) {
                patientName.setError("Name should not be Empty");
            }else if (GigaDocsUtils.checkEmptyString(patientDTO.getAddress())) {
                patientAddress.setError("Address should not be Empty");
            }else if (GigaDocsUtils.checkEmptyString(patientDTO.getEmail())) {
                patientEmail.setError("Email should not be Empty");
            } else if (!GigaDocsUtils.isValidEmail(patientDTO.getEmail())) {
                patientEmail.setError("Enter Valid Email");
            }  else {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean validatePatientInfoForUpdate() {
        try {
            if (GigaDocsUtils.checkEmptyString(patientMobile.getText().toString())) {
                patientMobile.setError("Mobile No should not be Empty");
            }else if (!GigaDocsUtils.isValidIndianMobileNumber(patientMobile.getText().toString())) {
                patientMobile.setError("please enter valid Indian Mobile Number");
            }else if (GigaDocsUtils.checkEmptyString(patientName.getText().toString())) {
                patientName.setError("Name should not be Empty");
            }else if (GigaDocsUtils.checkEmptyString(patientAddress.getText().toString())) {
                patientAddress.setError("Address should not be Empty");
            }else if (GigaDocsUtils.checkEmptyString(patientEmail.getText().toString())) {
                patientEmail.setError("Email should not be Empty");
            } else if (!GigaDocsUtils.isValidEmail(patientEmail.getText().toString())) {
                patientEmail.setError("Enter Valid Email");
            }  else {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setupUI(View view) {

        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if(!(view instanceof EditText)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(getActivity());
                        return false;
                    }

                });
            }

            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {

                try {
                    for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                        View innerView = ((ViewGroup) view).getChildAt(i);
                        setupUI(innerView);
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
    public void enableAddButtonView() {
        try {
            buttonAdd.setEnabled(true);
            buttonAdd.setClickable(true);
            buttonAdd.setBackgroundColor(Color.CYAN);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableAddButtonView() {
        try {
            buttonAdd.setEnabled(false);
            buttonAdd.setClickable(false);
            buttonAdd.setBackgroundColor(Color.WHITE);
            buttonAdd.setTextColor(Color.BLACK);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enableUpdateButtonView() {
        try {
            updateButton.setClickable(true);
            updateButton.setEnabled(true);
            updateButton.setBackgroundColor(Color.CYAN);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableUpdateButtonView() {
        try {
            updateButton.setClickable(false);
            updateButton.setEnabled(false);
            updateButton.setBackgroundColor(Color.WHITE);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public class AddPatientAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.postAddPatientService(getContext());
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
                    Log.i("coming to post", "cam to post");
                    responsePatientServerID = jsonObject.getString("server_id");
                    Log.i("responsePatientServerID", responsePatientServerID);
                    responsePatientID = jsonObject.getString("id");
                    patientDataBaseHelper.updateDataWithServerID(
                            patientDTO.getMobileNo(),
                            patientDTO.getName(),
                            patientDTO.getAddress(),
                            patientDTO.getEmail(),
                            pid,
                            responsePatientServerID,responsePatientID);
                    patientDataBaseHelper=new PatientDataBaseHelper(getContext());
                    Cursor c = patientDataBaseHelper.viewAllData();
                    while (c.moveToNext()) {
                        c.getString(0);
                      name=  c.getString(1);
                       email= c.getString(2);
                        c.getString(3);
                        c.getString(4);
                      pids=  c.getString(5);
                        serviceId = c.getString(6);
                        Log.d("Name,Email,pids,Sid",""+name+"  "+email+"  "+pids+"  "+serviceId);
                    }
                Log.d("PATIENT UPDATE*******", "***"+pids+"***"+email+"***"+name+"***"+serviceId);

                    Toast.makeText(getContext(), "Patient Added to Server", Toast.LENGTH_SHORT).show();
                    try {
                        patientSync.patientSync();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), "Failed Add details to Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }
}
