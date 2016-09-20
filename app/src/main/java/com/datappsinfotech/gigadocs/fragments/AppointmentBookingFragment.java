package com.datappsinfotech.gigadocs.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.R;
import com.datappsinfotech.gigadocs.adapters.CustomAdapter;
import com.datappsinfotech.gigadocs.autosync.AppointmentSync;
import com.datappsinfotech.gigadocs.database.AppointmentDataBaseHelper;
import com.datappsinfotech.gigadocs.database.PatientDataBaseHelper;
import com.datappsinfotech.gigadocs.utils.dtos.AppointmentDTO;
import com.datappsinfotech.gigadocs.utils.dtos.PatientDTO;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsSharedPreferenceManager;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsUtils;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigadocsConstants;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;
import com.datappsinfotech.gigadocs.validations.ViewStatus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppointmentBookingFragment extends Fragment implements View.OnClickListener ,ViewStatus {

    PatientDataBaseHelper patientDataBaseHelper;
    public static AppointmentDataBaseHelper appointmentDataBaseHelper;
    Context context= getContext();
    AppointmentSync appointmentSync;
//    @BindView(R.id.buttonViewAppointment)
//    Button buttonViewAppointment;
    @BindView(R.id.edit_textAppointmentSearch)
    EditText edit_textAppointmentSearch;
    @BindView(R.id.buttonSearch)
    ImageButton buttonSearch;

    @BindView(R.id.buttonAdd)
    Button buttonAdd;
    @BindView(R.id.buttonUpdate)
    Button buttonUpdate;
    @BindView(R.id.spinnerPatientName)
    Spinner spinnerPatientName;

    static ArrayList<String> array;
    static ArrayList<String> array1;
    ListView listView;
    ListView listViewOne;
    ListAdapter listAdapter;
    static String test1;
    ArrayList<String> patientName;
    ArrayAdapter<String> timeProvidedArrayAdapter;
    String iAttendPatientTimeInMinutes;
    public static TextView textViewSelcetDate;
    public static Spinner availableslotsSpinner;
//    public static String pid;

    public static ArrayList<String> availableSlotsList ;
    public static ArrayAdapter<String> availableSlotsArrayAdapter;
    String slots;
    public static String slot;
    public static String apo_id;
    public static HashMap<String, ArrayList<String>> map1= new HashMap<>();
    public static ArrayList<String> map2;
    public static ArrayList<String> map3;
    public static ArrayList<String > setId = new ArrayList<>();

    public static String responseAppointmentServerID;
    public static String responseAppointmentID;


    public  static PatientDTO patientDTO ;
    public static AppointmentDTO appointmentDTO;
    public AppointmentBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_appointment_booking, container, false);
        try {
            setupUI(view.findViewById(R.id.appointment_lLinear_layout));
            ButterKnife.bind(this, view);
             patientDTO= new PatientDTO();
            appointmentDTO = new AppointmentDTO();
            appointmentSync = new AppointmentSync(getContext());
            textViewSelcetDate = (TextView)view.findViewById(R.id.text_viewSelcetDate);
            availableslotsSpinner =(Spinner)view.findViewById(R.id.spinnerAppointmentSlot);
            availabilitySlotsOnItemClickListner();

            textViewSelcetDate.setOnClickListener(this);
            patientDataBaseHelper = new PatientDataBaseHelper(getContext());
            appointmentDataBaseHelper = new AppointmentDataBaseHelper(getContext());
            buttonAdd.setOnClickListener(this);
            buttonSearch.setOnClickListener(this);
            buttonUpdate.setOnClickListener(this);
//            buttonViewAppointment.setOnClickListener(this);

            patientItemClickListner();

        /* Set Text Watcher listener */
            edit_textAppointmentSearch.addTextChangedListener(passwordWatcher);

//            //List View Code
            listView = (ListView) view.findViewById(R.id.listview);
            listViewOne = (ListView) view.findViewById(R.id.listview_one);

            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String umobile = String.valueOf(parent.getItemAtPosition(position));
                                edit_textAppointmentSearch.setText(umobile);
                                test1 = edit_textAppointmentSearch.getText().toString();
                                listView.setVisibility(View.GONE);
                            }
                            catch (Throwable t){
                                t.printStackTrace();
                            }
                        }
                    }
            );
            listViewOne.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                String uapo_id = String.valueOf(parent.getItemAtPosition(position));
                                edit_textAppointmentSearch.setText(uapo_id);
                                test1 = edit_textAppointmentSearch.getText().toString();
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

    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int  before, int count) {
        }

        public void afterTextChanged(Editable s) {
            try {
                if (s.length() < 3) {
                    listView.setVisibility(View.GONE);
                    listViewOne.setVisibility(View.GONE);
//                    ArrayList<Spinner> m = new ArrayList<>();
//                    m.add(null);
//                    listAdapter = new ArrayAdapter<>(getContext(), m);
//                    listViewOne.setAdapter(listAdapter);
//                    listView.setAdapter(listAdapter);
                }
                else if(s.length() >= 3) {
                    if (edit_textAppointmentSearch.getText().toString().matches("[0-9]+")){
                        listView.setVisibility(View.VISIBLE);
                    Cursor c = patientDataBaseHelper.getMobileNo(edit_textAppointmentSearch.getText().toString());
                        int i = 0;
                        array = new ArrayList<>();
                        while (c.moveToNext()) {
                            String umobile = c.getString(c.getColumnIndex(patientDataBaseHelper.PCOL2));
                            array.add(umobile);
                            i++;
                            listAdapter = new CustomAdapter(getContext(), array);
                            listView.setAdapter(listAdapter);
                        }
                    } else  {
                        listViewOne.setVisibility(View.VISIBLE);
                        Cursor c1 = appointmentDataBaseHelper.getApoId(edit_textAppointmentSearch.getText().toString());
                        if (c1 != null) {
                            int i = 0;
                            array1 = new ArrayList<>();
                            while (c1.moveToNext()) {
                                String uapo_id = c1.getString(c1.getColumnIndex(appointmentDataBaseHelper.ACOL6));
                                array1.add(uapo_id);
                                i++;
                                listAdapter = new CustomAdapter(getContext(), array1);
                                listViewOne.setAdapter(listAdapter);
                            }
                        }
                    }
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

                    String slot3 = null;
                    if (edit_textAppointmentSearch.getText().toString().matches("[0-9]+")) {
                        enableAddButtonView();
                        try {
                            if (validatePatientInfo()) {
                                boolean isInserted = false;
                                slot3 = slot;
                                if (!slot.equals("select slot")) {
//                                    patientDataBaseHelper = new PatientDataBaseHelper(getContext());
                                    patientDataBaseHelper.viewSelectedData(appointmentDTO.getPatientName(), edit_textAppointmentSearch.getText().toString());
                                    Log.i("getSetPid", PatientDataBaseHelper.appointmentDTO.getPid());
                                    isInserted = appointmentDataBaseHelper.insertData(
                                            edit_textAppointmentSearch.getText().toString(),
                                            appointmentDTO.getPatientName(),
                                            appointmentDTO.getSetDate(),
                                            slot,
                                            null,
                                            " ",
                                            PatientDataBaseHelper.appointmentDTO.getPid(),
                                            "false");
                                    appointmentDTO.setAppointmentAdded(true);
                                    appointmentDTO.setServer_id(null);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Please Select Slot From the Drop Down", Toast.LENGTH_SHORT).show();
                                }
                                if (isInserted) {
                                    try {
                                        appointmentDataBaseHelper.getId(edit_textAppointmentSearch.getText().toString(),
                                                appointmentDTO.getPatientName(),
                                                appointmentDTO.getSetDate(),
                                                slot3);
                                        String id = appointmentDTO.getPatientId();
                                        apo_id = "APOID0" + id;
                                        Log.i("Apo id check", apo_id);
                                        Log.i("Check Slots", slot3);
                                        appointmentDataBaseHelper.updateDataAfterInsertion(
                                                edit_textAppointmentSearch.getText().toString(),
                                                appointmentDTO.getPatientName(),
                                                appointmentDTO.getSetDate(),
                                                slot3, apo_id, " ", PatientDataBaseHelper.appointmentDTO.getPid());
                                        if (CheckInternetConnection.isConnectionAvailable(getContext())) {
                                            new AddAppointmentAsyncTaskService().execute();
                                            Toast.makeText(getContext(), GigadocsConstants.APPOINTMENT_ADDED_SUCCESSFULLY + "Appointment Id is : " + apo_id, Toast.LENGTH_LONG).show();
                                        }
                                        disableUpdateButtonView();
                                        disableAddButtonView();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        edit_textAppointmentSearch.setError("Please Enter Mobile Number To Add Patient");
                    }
                    break;
                case R.id.buttonSearch:
                    if(!edit_textAppointmentSearch.getText().toString().equals("")) {
                        if (edit_textAppointmentSearch.getText().toString().matches("[0-9]+")) {
                            if(edit_textAppointmentSearch.getText().length()>9) {
                                Cursor c = patientDataBaseHelper.getName(edit_textAppointmentSearch.getText().toString());
                                patientName = new ArrayList<>();
                                while (c.moveToNext()) {
                                    String uname = c.getString(c.getColumnIndex(patientDataBaseHelper.PCOL3));
                                    patientName.add(uname);
                                    timeProvidedArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, patientName);
                                    timeProvidedArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerPatientName.setAdapter(timeProvidedArrayAdapter);
                                }
                            }
                        } else {
                            Cursor c1 = appointmentDataBaseHelper.chechSlotAvailability(edit_textAppointmentSearch.getText().toString().toUpperCase());
                            patientName = new ArrayList<>();
                            while (c1.moveToNext()) {
                                String uname = c1.getString(c1.getColumnIndex(appointmentDataBaseHelper.ACOL3));
                                String date = c1.getString(c1.getColumnIndex(appointmentDataBaseHelper.ACOL4));
                                patientName.add(uname);
                                timeProvidedArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, patientName);
                                timeProvidedArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerPatientName.setAdapter(timeProvidedArrayAdapter);
                                textViewSelcetDate.setText(date);
                                ArrayList<String> slot1 = new ArrayList<>();
                                slot1.add(c1.getString(c1.getColumnIndex(appointmentDataBaseHelper.ACOL5)));
                                ArrayAdapter<String> availableSlotsArrayAdapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, slot1);
                                availableSlotsArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                availableslotsSpinner.setAdapter(availableSlotsArrayAdapter1);

                            }
                        }
                    }
                    else{
                        edit_textAppointmentSearch.setError("Please Fill The Empty Field");
                    }
                            break;
                case R.id.buttonUpdate:
                    if (!edit_textAppointmentSearch.getText().toString().matches("[0-9]+")) {

                        enableUpdateButtonView();
                    }
                    else{
                        disableAddButtonView();
                    }
                    if (edit_textAppointmentSearch.getText().toString().matches("[0-9]+")){
                        edit_textAppointmentSearch.setError("Please Enter ApoId To Update Data");
                    }
                    else{
                    if (validatePatientInfo()) {
//                        appointmentDataBaseHelper.deleteSlot(edit_textAppointmentSearch.getText().toString());
                    boolean isInserted1= appointmentDataBaseHelper.updateDataWithApoid(edit_textAppointmentSearch.getText().toString(),
                            appointmentDTO.getPatientName(),
                            appointmentDTO .getSetDate(),
                            slot );
                    if (isInserted1) {
                        Toast.makeText(getContext(), GigadocsConstants.APPOINTMENT_UPDATED_SUCCESSFULLY,
                                Toast.LENGTH_LONG).show();
                    }
                    }}
                    break;

                case R.id.text_viewSelcetDate:
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getActivity().getFragmentManager(), "datePicker");
                    break;
            }
        } catch (Throwable t) {
            t.printStackTrace();
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

    public boolean validatePatientInfo() {
        try {
            if (GigaDocsUtils.checkEmptyString(edit_textAppointmentSearch.getText().toString())) {
                edit_textAppointmentSearch.setError("1. Enter Mobile no. to add Patient\n 2. Appointment Id to Update Patient");
            }else if (GigaDocsUtils.checkEmptyString(textViewSelcetDate.getText().toString())) {
                textViewSelcetDate.setError("Date should not be Empty");
            }  else {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void setupUI(View view) {
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

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public void patientItemClickListner() {
        try {
            spinnerPatientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    iAttendPatientTimeInMinutes = patientName.get(position);
                    appointmentDTO.setPatientName(iAttendPatientTimeInMinutes);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void availabilitySlotsOnItemClickListner() {
        try {
            availableslotsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    try{
                       slot= availableSlotsArrayAdapter.getItem(position);
                        appointmentDTO.setPosition(position);
                        slots = availableSlotsList.get(position);
                        appointmentDTO.setSaveSlots(slots);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

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
    public void enableAddButtonView() {
        try {
            buttonAdd.setEnabled(true);
            buttonAdd.setClickable(true);
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
            buttonUpdate.setClickable(true);
            buttonUpdate.setEnabled(true);
            buttonUpdate.setBackgroundColor(Color.CYAN);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void disableUpdateButtonView() {
        try {
            buttonUpdate.setClickable(false);
            buttonUpdate.setEnabled(false);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static class DatePickerFragment extends DialogFragment

            implements DatePickerDialog.OnDateSetListener {
        int yearCurrent;
        int monthCurrent;
        int dayCurrent;
        String date;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            yearCurrent = c.get(Calendar.YEAR);
            monthCurrent = c.get(Calendar.MONTH);
            dayCurrent = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yearCurrent, monthCurrent, dayCurrent);
        }
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Do something with the date chosen by the user
            try {
                if (year < yearCurrent) {
                    Toast.makeText(getActivity(), "Please Select the Current Date or  Future Date", Toast.LENGTH_LONG).show();
                    AppointmentBookingFragment.textViewSelcetDate.setError("Please Select the Current Date or  Future Date");
                    AppointmentBookingFragment.textViewSelcetDate.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            AppointmentBookingFragment.textViewSelcetDate.setError(null);
                            return false;
                        }
                    });
                    view.updateDate(yearCurrent, monthCurrent, dayCurrent);

                } else if (monthOfYear < monthCurrent && year == yearCurrent) {
                    view.updateDate(yearCurrent, monthCurrent, dayCurrent);
                    Toast.makeText(getActivity(), "Please Select the Current Date or  Future Date", Toast.LENGTH_LONG).show();
                    AppointmentBookingFragment.textViewSelcetDate.setError("Please Select the Current Date or  Future Date");
                    AppointmentBookingFragment.textViewSelcetDate.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            AppointmentBookingFragment.textViewSelcetDate.setError(null);
                            return false;
                        }
                    });

                } else if (dayOfMonth < dayCurrent && year == yearCurrent && monthOfYear == monthCurrent) {
                    view.updateDate(yearCurrent, monthCurrent, dayCurrent);
                    Toast.makeText(getActivity(), "Please Select the Current Date or  Future Date", Toast.LENGTH_LONG).show();
                    AppointmentBookingFragment.textViewSelcetDate.setError("Please Select the Current Date or  Future Date");
                    AppointmentBookingFragment.textViewSelcetDate.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            AppointmentBookingFragment.textViewSelcetDate.setError(null);
                            return false;
                        }
                    });
                } else {
                    if((monthOfYear + 1) <10 && dayOfMonth < 10 ) {
                        date = String.valueOf(new StringBuilder().append(year).append("-").append(0).append((monthOfYear + 1)).append("-").append(0).append(dayOfMonth));
                    }
                    else if ((monthOfYear + 1) <10){
                        date = String.valueOf(new StringBuilder().append(year).append("-").append(0).append((monthOfYear + 1)).append("-").append(dayOfMonth));

                    }
                    else if (dayOfMonth <10){
                        date = String.valueOf(new StringBuilder().append(year).append("-").append((monthOfYear + 1)).append("-").append(dayOfMonth));

                    }
                    else
                    {
                        date = String.valueOf(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
                    }                }
                AppointmentBookingFragment.textViewSelcetDate.setText(date);
                Cursor c = appointmentDataBaseHelper.getDate();
                int i = 0;
                while(c.moveToNext()){
                    String dateFromDb = c.getString(c.getColumnIndex(appointmentDataBaseHelper.ACOL4));
                    map1.put(dateFromDb, null);
                    i++;
                }
                checkDate(date);
                appointmentDTO .setSetDate(AppointmentBookingFragment.textViewSelcetDate.getText().toString());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public void checkDate(String date){
            availableSlotsList = new ArrayList<>(Arrays.asList(GigaDocsSharedPreferenceManager.getKey(getActivity(), GigaDocsAPIConstants.SHIFT_TIMINGS,null).split(",")));
            availableSlotsList.add(0, "select slot");
            map3 = new ArrayList<>();
            try {
                if(map1.containsKey(date)){
                    Cursor c = appointmentDataBaseHelper.getSlot(date);
                    int i = 0;
                    map2 = new ArrayList<>();
                    while(c.moveToNext()){
                        String slot= c.getString(c.getColumnIndex(appointmentDataBaseHelper.ACOL5));
                        map2.add(slot);
                        Log.i("Slot For Map2", map2.toString());
                        i++;
                    }
                      for(int j=0; j<availableSlotsList.size(); j++ ){
                          if(!map2.contains(availableSlotsList.get(j))){
                              map3.add(availableSlotsList.get(j));
                          }
                    }
                    availableSlotsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, map3);
                    availableSlotsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    availableslotsSpinner.setAdapter(availableSlotsArrayAdapter);
                }
                else{
                    availableSlotsList = new ArrayList<>(Arrays.asList(GigaDocsSharedPreferenceManager.getKey(getActivity(), GigaDocsAPIConstants.SHIFT_TIMINGS,null).split(",")));
                    availableSlotsList.add(0, "select slot");
                    availableSlotsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, availableSlotsList);
                    availableSlotsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    availableslotsSpinner.setAdapter(availableSlotsArrayAdapter);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    public class AddAppointmentAsyncTaskService extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.postAddAppointmentService(getContext());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                if(jsonObject.getBoolean(GigaDocsAPIConstants.STATUS)){
                    String server_id_response = jsonObject.getString("server_id");
                    responseAppointmentServerID = jsonObject.getString("server_id");
                    responseAppointmentID =jsonObject.getString("id");
                    appointmentDataBaseHelper.updateDataAfterInsertion(
                                                edit_textAppointmentSearch.getText().toString(),
                                                appointmentDTO.getPatientName(),
                                                appointmentDTO.getSetDate(),
                                                slot, apo_id, server_id_response, PatientDataBaseHelper.appointmentDTO.getPid());
                    Toast.makeText(getContext(), "Appointment Sent to Server Successfully", Toast.LENGTH_SHORT).show();
                    try {
                        appointmentSync.appointmentSync();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), "Failed to send server", Toast.LENGTH_SHORT).show();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}

