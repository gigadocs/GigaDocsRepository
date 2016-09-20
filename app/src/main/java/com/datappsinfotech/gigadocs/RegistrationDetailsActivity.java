package com.datappsinfotech.gigadocs;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.utils.dtos.RegistrationDetailsActivityDTO;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigadocsConstants;
import com.datappsinfotech.gigadocs.utils.communicators.IattendPatient;
import com.datappsinfotech.gigadocs.utils.communicators.TimeSpecification;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;
import com.datappsinfotech.gigadocs.validations.Validations;
import com.datappsinfotech.gigadocs.widgets.AVUtils;
import com.datappsinfotech.gigadocs.widgets.InternetFailureDialog;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationDetailsActivity extends AppCompatActivity implements Validations, TimeSpecification, View.OnClickListener, IattendPatient {

    @BindView(R.id.buttonSignUp)
    Button buttonSignUp;
    @BindView(R.id.text_viewIWorkFrom)
    TextView textViewIWorkFrom;
    @BindView(R.id.text_viewIworkTO)
    TextView textViewIWorkTo;
    @BindView(R.id.edit_textDoctorAddress)
    EditText editTextDoctorAddress;
    @BindView(R.id.switchWorkInTwoShift)
    public Switch switchWorkInTwoShift;
    @BindView(R.id.linear_layoutIWorkFromAndToTitles)
    LinearLayout linearLayoutIworkFromAndToTitles;
    @BindView(R.id.linear_layoutIWorkFromAndTo)
    LinearLayout linearLayoutIworkFromAndTo;
    @BindView(R.id.dropdown_iGivePatient)
    Spinner spinnerDropDownIGivePatient;


    public static Dialog internetFailure,dailog;

    String iAttendPatientTimeInMinutes;

    int flag;
    PopupWindow popupWindowIGiveTime,
            popupWindowIWorkInTwoShift;
    LayoutInflater layoutInflater;
    FrameLayout popupElementFrameLayout;
    FrameLayout timepickerFrameLayout;
    Button buttonOk,
            buttonCancel,
            buttonSetTime;

    TextView textViewIworkFromShift1,
            textViewIworkFromShift2,
            textViewIworkToShift1,
            textViewIworkToShift2;

    ArrayList<String> iAttendPatientTime;
    ArrayAdapter<String> timeProvidedArrayAdapter;


    NumberPicker numberPickerhr,numberPickermin,numberPickerampm;
    TextView textViewHours,textViewMinutes,textViewAmPm;
    String hours,minutes,pmam;

    public static Context context;

    String stringTextViewIworkFrom;
    String stringTextViewIworkTo;
    String stringTextViewIworkFromShift1;
    String stringTextViewIworkToShift1;
    String stringTextViewIworkToShift2;
    String stringTextViewIworkFromShift2;

    public static RegistrationDetailsActivityDTO registrationDetailsActivityDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_registration_details);
            ButterKnife.bind(this);
            registrationDetailsActivityDTO = new RegistrationDetailsActivityDTO();
            internetFailure = InternetFailureDialog.getAnimDialog(this);
            dailog= AVUtils.getAnimDialog(this);
            context = getBaseContext();
            Button buttonRetry = (Button)internetFailure.findViewById(R.id.buttonRetry);
            textViewIWorkFrom.setOnClickListener(this);
            textViewIWorkTo.setOnClickListener(this);
            buttonSignUp.setOnClickListener(this);
            popupElementFrameLayout = (FrameLayout) findViewById(R.id.popup_element);
            timepickerFrameLayout = (FrameLayout) findViewById(R.id.timepickerxml);
            iAttendPatient();
            iAttendPatientItemClickListner();

            if (switchWorkInTwoShift != null) {
                switchWorkInTwoShift.setChecked(false);
            }
            if (buttonRetry!=null){
                buttonRetry.setOnClickListener(this);
            }
            switchWorkInTwoShift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {
                            iWorkInTwoShift();
                            linearLayoutIworkFromAndToTitles.setVisibility(View.GONE);
                            linearLayoutIworkFromAndTo.setVisibility(View.GONE);
                        }else {
                            linearLayoutIworkFromAndTo.setVisibility(View.VISIBLE);
                            linearLayoutIworkFromAndToTitles.setVisibility(View.VISIBLE);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ShiftTimeSelection() {
        try {
            layoutInflater = (LayoutInflater) RegistrationDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.time_picker, (ViewGroup) findViewById(R.id.timepickerxml));

            popupWindowIGiveTime = new PopupWindow(layout, 0, 0, true);
            textViewHours =(TextView) popupWindowIGiveTime.getContentView().findViewById(R.id.text_viewHours);
            textViewMinutes =(TextView)popupWindowIGiveTime.getContentView().findViewById(R.id.text_viewMins);
            textViewAmPm =(TextView) popupWindowIGiveTime.getContentView().findViewById(R.id.text_viewAmPm);

            numberPickerhr = (NumberPicker) popupWindowIGiveTime.getContentView().findViewById(R.id.hr);
            numberPickermin = (NumberPicker)popupWindowIGiveTime.getContentView(). findViewById(R.id.min);
            numberPickerampm = (NumberPicker) popupWindowIGiveTime.getContentView().findViewById(R.id.ampm);

            buttonCancel = (Button) popupWindowIGiveTime.getContentView().findViewById(R.id.buttonCancel);
            buttonSetTime = (Button) popupWindowIGiveTime.getContentView().findViewById(R.id.buttonSetTime);

            popupWindowIGiveTime.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindowIGiveTime.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindowIGiveTime.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 0);
            textViewHours.setTextColor(Color.parseColor("#27AE60"));
            final String[] hour= {"--","01","02", "03", "04", "05","06","07","08","09","10","11","12"};
            final String[] min= {"--","05","10", "15", "20", "25","30","35","40","45","50","55","00"};
            final String[] ampm= {"--","am","pm"};

            numberPickerhr.setMinValue(0);
            numberPickerhr.setMaxValue(hour.length-1);
            numberPickerhr.setWrapSelectorWheel(true);
            numberPickerhr.setDisplayedValues(hour);

            numberPickermin.setMinValue(0);
            numberPickermin.setMaxValue(min.length-1);
            numberPickermin.setWrapSelectorWheel(true);
            numberPickermin.setDisplayedValues(min);

            numberPickerampm.setMinValue(0);
            numberPickerampm.setMaxValue(ampm.length-1);
            numberPickerampm.setWrapSelectorWheel(true);
            numberPickerampm.setDisplayedValues(ampm);

            numberPickerhr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal){

                    textViewHours.setText(hour[newVal]);
                    hours = hour[newVal];
                }
            });

            numberPickermin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    textViewMinutes.setText(min[newVal]);
                    minutes =min[newVal];
                }
            });
            numberPickerampm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    textViewAmPm.setText(ampm[newVal]);
                    pmam = ampm[newVal];
                }
            });
            buttonSetTime.setOnClickListener(this);
            buttonCancel.setOnClickListener(this);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void myHospitalLogo() {

    }

    @Override
    public void iWorkInTwoShift() {
        try {
            layoutInflater = (LayoutInflater) RegistrationDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.screen_popup, (ViewGroup) findViewById(R.id.popup_element));
            popupWindowIWorkInTwoShift = new PopupWindow(layout, 0, 0, true);

            textViewIworkFromShift1 = (TextView) popupWindowIWorkInTwoShift.getContentView().findViewById(R.id.edit_textI_work_from);
            textViewIworkToShift1 = (TextView) popupWindowIWorkInTwoShift.getContentView().findViewById(R.id.edit_textI_work_to);
            textViewIworkFromShift2 = (TextView) popupWindowIWorkInTwoShift.getContentView().findViewById(R.id.edit_textI_work_fromShift2);
            textViewIworkToShift2 = (TextView) popupWindowIWorkInTwoShift.getContentView().findViewById(R.id.edit_textI_work_toShift2);
            buttonOk = (Button) popupWindowIWorkInTwoShift.getContentView().findViewById(R.id.buttonOk);

            textViewIworkFromShift1.setOnClickListener(this);
            textViewIworkToShift1.setOnClickListener(this);
            textViewIworkFromShift2.setOnClickListener(this);
            textViewIworkToShift2.setOnClickListener(this);
            buttonOk.setOnClickListener(this);

            popupWindowIWorkInTwoShift.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindowIWorkInTwoShift.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
            popupWindowIWorkInTwoShift.showAtLocation(layout, Gravity.CENTER_VERTICAL, 0, 0);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void iWorkFrom() {
        try {
            ShiftTimeSelection();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Override
    public void iWorkto() {
        try {
            ShiftTimeSelection();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.buttonSignUp:
                    /*Post the below parameters to Server*/

                    // address
                    registrationDetailsActivityDTO.setAddress_RegistrationDetails(editTextDoctorAddress.getText().toString());
                    registrationDetailsActivityDTO.setiAttendPatientTime_RegistrationDetails(iAttendPatientTimeInMinutes);

                    // shift timings if it is two shifts
                    if (registrationDetailsActivityDTO.setChecked_RegistrationDetails(switchWorkInTwoShift.isChecked())){
                        registrationDetailsActivityDTO.setiWorkFromShiftOneTimeFetch_RegistrationDetails(stringTextViewIworkFromShift1);
                        registrationDetailsActivityDTO.setiWorkFromShiftTwoTimeFetch_RegistrationDetails(stringTextViewIworkFromShift2);
                        registrationDetailsActivityDTO.setiWorkToShiftOneTimeFetch_RegistrationDetails(stringTextViewIworkToShift1);
                        registrationDetailsActivityDTO.setiWorkToShiftTwoTimeFetch_RegistrationDetails(stringTextViewIworkToShift2);

//                        Toast.makeText(RegistrationDetailsActivity.this,
//                                registrationDetailsActivityDTO.getiAttendPatientTime_RegistrationDetails()
//                                + registrationDetailsActivityDTO.getiWorkFromShiftOneTimeFetch_RegistrationDetails()
//                                + registrationDetailsActivityDTO.getiWorkFromShiftTwoTimeFetch_RegistrationDetails()
//                                + registrationDetailsActivityDTO.getiWorkToShiftOneTimeFetch_RegistrationDetails()
//                                + registrationDetailsActivityDTO.getiWorkToShiftTwoTimeFetch_RegistrationDetails(), Toast.LENGTH_SHORT).show();
                    }
                else {
                        registrationDetailsActivityDTO.setiWorkFromTimeFetch_RegistrationDetails(stringTextViewIworkFrom);
                        registrationDetailsActivityDTO.setiWorkToTimeFetch_RegistrationDetails(stringTextViewIworkTo);

//                        Toast.makeText(RegistrationDetailsActivity.this,
//                                registrationDetailsActivityDTO.getAddress_RegistrationDetails()
//                                + registrationDetailsActivityDTO.getiAttendPatientTime_RegistrationDetails()
//                                + registrationDetailsActivityDTO.getiWorkFromTimeFetch_RegistrationDetails()
//                                + registrationDetailsActivityDTO.getiWorkToTimeFetch_RegistrationDetails()
//                                , Toast.LENGTH_SHORT).show();
                    }
                    if (validationSuccess()) {
                        // starting activity
                        try {
                            if (CheckInternetConnection.isConnectionAvailable(getApplicationContext())){
                                new SignUpAsyncTask().execute();
                            }else {
                                internetFailure.show();
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.edit_textI_work_from:
                    ShiftTimeSelection();
                    flag = 9;
                    break;
                case R.id.edit_textI_work_to:
                    ShiftTimeSelection();
                    flag = 8;
                    break;
                case R.id.edit_textI_work_fromShift2:
                    ShiftTimeSelection();
                    flag = 7;
                    break;
                case R.id.edit_textI_work_toShift2:
                    ShiftTimeSelection();
                    flag = 6;
                    break;
                case R.id.buttonOk:
                    // popup button ok functions
                    if (validateShiftTimings()) {
                        popupWindowIWorkInTwoShift.dismiss();
                    }
                    break;
                case R.id.dropdown_iGivePatient:
                    flag = 0;
                    break;
                case R.id.text_viewIWorkFrom:
                    flag = 1;
                    iWorkFrom();
                    break;
                case R.id.text_viewIworkTO:
                    flag = 2;
                    iWorkto();
                    break;
                case R.id.buttonSetTime:
                    Toast.makeText(RegistrationDetailsActivity.this, hours+":"+minutes+" "+pmam, Toast.LENGTH_SHORT).show();
                    if (flag == 1) {
                        textViewIWorkFrom.setText(hours+":"+minutes+" "+pmam);
                         stringTextViewIworkFrom = hours+":"+minutes;
                        registrationDetailsActivityDTO.setShiftAMPMFROM_RegistrationDetails(pmam);
                    } else if (flag == 2) {
                        textViewIWorkTo.setText(hours+":"+minutes+" "+pmam);
                        stringTextViewIworkTo = hours+":"+minutes;
                        registrationDetailsActivityDTO.setShiftAMPMTO_RegistrationDetails(pmam);
                    } else if (flag == 9) {
                        textViewIworkFromShift1.setText(hours+":"+minutes+" "+pmam);
                        stringTextViewIworkFromShift1 = hours+":"+minutes;
                        registrationDetailsActivityDTO.setShift1AMPMFROM_RegistrationDetails(pmam);
                    } else if (flag == 8) {
                        textViewIworkToShift1.setText(hours+":"+minutes+" "+pmam);
                        stringTextViewIworkToShift1 = hours+":"+minutes;
                        registrationDetailsActivityDTO.setShift1AMPMTO_RegistrationDetails(pmam);
                    } else if (flag == 7) {
                        textViewIworkFromShift2.setText(hours+":"+minutes+" "+pmam);
                        stringTextViewIworkFromShift2 = hours+":"+minutes;
                        registrationDetailsActivityDTO.setShift2AMPMFROM_RegistrationDetails(pmam);
                    } else if (flag == 6) {
                        textViewIworkToShift2.setText(hours+":"+minutes+" "+pmam);
                        stringTextViewIworkToShift2 = hours+":"+minutes;
                        registrationDetailsActivityDTO.setShift2AMPMTO_RegistrationDetails(pmam);
                    } else {
                        Toast.makeText(RegistrationDetailsActivity.this, "Please set the Time", Toast.LENGTH_SHORT).show();
                    }
                    popupWindowIGiveTime.dismiss();
                    break;
                case R.id.buttonCancel:
                    popupWindowIGiveTime.dismiss();
                    break;
                case R.id.buttonRetry:
                    try {
                        if (CheckInternetConnection.isConnectionAvailable(getApplicationContext())) {
                            new SignUpAsyncTask().execute();
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
    public void iAttendPatientItemClickListner() {
        try {
            spinnerDropDownIGivePatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    iAttendPatientTimeInMinutes = iAttendPatientTime.get(position);
                    Toast.makeText(getApplicationContext(), iAttendPatientTime.get(position), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void iAttendPatient() {
        try {
            iAttendPatientTime = new ArrayList<>();

            iAttendPatientTime.add(GigadocsConstants.FIVE_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.TEN_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.FIFTEEN_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.TWENTY_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.TWENTYFIVE_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.THIRTY_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.THIRTYFIVE_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.FOURTY_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.FOURTYFIVE_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.FIFTY_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.FIFTYFIVE_MINUTES);
            iAttendPatientTime.add(GigadocsConstants.SIXTY_MINUTES);

            timeProvidedArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, iAttendPatientTime);
            timeProvidedArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDropDownIGivePatient.setAdapter(timeProvidedArrayAdapter);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            spinnerDropDownIGivePatient.setLayoutParams(params);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean validationSuccess() {

        try {
            if (editTextDoctorAddress.getText().toString().length() < 8) {
                editTextDoctorAddress.setError("Address must be greater than 8 characters");
                editTextDoctorAddress.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        editTextDoctorAddress.setError(null);
                        return false;
                    }
                });
                return false;
            }
            if (switchWorkInTwoShift.isChecked()) {
                validateShiftTimings();
            } else if (textViewIWorkFrom.getText().toString().length() == 0) {
                textViewIWorkFrom.setError("Invalid shift timings");
                textViewIWorkFrom.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textViewIWorkFrom.setError(null);
                        return false;
                    }
                });
                return false;
            } else if (textViewIWorkTo.getText().toString().length() == 0) {
                textViewIWorkTo.setError("Invalid shift timings");
                textViewIWorkTo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textViewIWorkTo.setError(null);
                        return false;
                    }
                });
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean validateShiftTimings() {

        try {
            if (textViewIworkFromShift1.getText().toString().length() == 0) {
                 textViewIworkFromShift1.setError("Invalid shift 1 timings");
                 textViewIworkFromShift1.setOnTouchListener(new View.OnTouchListener() {
                     @Override
                     public boolean onTouch(View v, MotionEvent event) {
                         textViewIworkFromShift1.setError(null);
                         return false;
                     }
                 });
                 return false;
             } else if (textViewIworkToShift1.getText().toString().length() == 0) {
                 textViewIworkToShift1.setError("Invalid shift 1 timings");
                 textViewIworkToShift1.setOnTouchListener(new View.OnTouchListener() {
                     @Override
                     public boolean onTouch(View v, MotionEvent event) {
                         textViewIworkToShift1.setError(null);
                         return false;
                     }
                 });
                 return false;
             } else if (textViewIworkFromShift2.getText().toString().length() == 0) {
                 textViewIworkFromShift2.setError("Invalid shift 2 timings");
                 textViewIworkFromShift2.setOnTouchListener(new View.OnTouchListener() {
                     @Override
                     public boolean onTouch(View v, MotionEvent event) {
                         textViewIworkFromShift2.setError(null);
                         return false;
                     }
                 });
                 return false;
             }else if (textViewIworkToShift2.getText().toString().length() == 0) {
                textViewIworkToShift2.setError("Invalid shift 2 timings");
                textViewIworkToShift2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textViewIworkToShift2.setError(null);
                        return false;
                    }
                });
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
    public  class SignUpAsyncTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            try {
                super.onPreExecute();
                dailog.show();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                return WebServiceClass.postSignUpService();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                if (jsonObject.getBoolean("Status")) {
                    Toast.makeText(RegistrationDetailsActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationDetailsActivity.context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    RegistrationDetailsActivity.context.startActivity(intent);
                    dailog.dismiss();
                }else{
                    if (jsonObject.getString("message")!=null){
                        Toast.makeText(getApplicationContext(), "This Email is Already Registered Please Use Another Email", Toast.LENGTH_SHORT).show();
                        dailog.dismiss();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }finally {
                dailog.dismiss();
            }
        }
    }
}


