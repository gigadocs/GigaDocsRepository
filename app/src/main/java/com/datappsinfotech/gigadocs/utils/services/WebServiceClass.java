package com.datappsinfotech.gigadocs.utils.services;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.datappsinfotech.gigadocs.ForgotPasswordActivity;
import com.datappsinfotech.gigadocs.HomeScreenActivity;
import com.datappsinfotech.gigadocs.LoginActivity;
import com.datappsinfotech.gigadocs.PrescriptionActivity;
import com.datappsinfotech.gigadocs.RegistrationActivity;
import com.datappsinfotech.gigadocs.RegistrationDetailsActivity;
import com.datappsinfotech.gigadocs.database.AppointmentDataBaseHelper;
import com.datappsinfotech.gigadocs.database.PatientDataBaseHelper;
import com.datappsinfotech.gigadocs.database.PrescriptionDataBaseHelper;
import com.datappsinfotech.gigadocs.fragments.AppointmentBookingFragment;
import com.datappsinfotech.gigadocs.fragments.CalenderFragment;
import com.datappsinfotech.gigadocs.fragments.PatientFragment;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsDataUnavailableException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsTimeOutException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsUnAutorisedException;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsSharedPreferenceManager;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigadocsConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class WebServiceClass {

    private static ServiceClass serviceClass = new ServiceClass();
    public static JSONObject postSignUpService() throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String, String > postSignUpParametersMap = new HashMap<>();
        postSignUpParametersMap.put(GigaDocsAPIConstants.NAME, RegistrationActivity.registrationActivityDTO.getName_RegistrationActivity());
        postSignUpParametersMap.put(GigaDocsAPIConstants.EMAIL, RegistrationActivity.registrationActivityDTO.getEmail_RegistrationActivity());
        postSignUpParametersMap.put(GigaDocsAPIConstants.MOBILE, RegistrationActivity.registrationActivityDTO.getMobile_RegistrationActivity());
        postSignUpParametersMap.put(GigaDocsAPIConstants.PASSWORD, RegistrationActivity.registrationActivityDTO.getPassword_RegistrationActivity());
        postSignUpParametersMap.put(GigaDocsAPIConstants.SPECIALITIES, RegistrationActivity.registrationActivityDTO.getSpeciality_RegistrationActivity());
        postSignUpParametersMap.put(GigaDocsAPIConstants.ADDRESS, String.format("%s", RegistrationDetailsActivity.registrationDetailsActivityDTO.getAddress_RegistrationDetails()));
        postSignUpParametersMap.put(GigaDocsAPIConstants.I_ATTEND_PATIENT, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getiAttendPatientTime_RegistrationDetails()));
        if (RegistrationDetailsActivity.registrationDetailsActivityDTO.isChecked_RegistrationDetails()){
            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_FROM_SHIFT_ONE, String.format("%s", RegistrationDetailsActivity.registrationDetailsActivityDTO.getiWorkFromShiftOneTimeFetch_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_TO_SHIFT_ONE, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getiWorkToShiftOneTimeFetch_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_ONE_AM_PM_START, String.format("%s", RegistrationDetailsActivity.registrationDetailsActivityDTO.getShift1AMPMFROM_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_ONE_AM_PM_END, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getShift1AMPMTO_RegistrationDetails()));

            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_FROM_SHIFT_TWO, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getiWorkFromShiftTwoTimeFetch_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_TO_SHIFT_TWO, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getiWorkToShiftTwoTimeFetch_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_TWO_AM_PM_START, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getShift2AMPMFROM_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_TWO_AM_PM_END, String.format("%s", RegistrationDetailsActivity.registrationDetailsActivityDTO.getShift2AMPMTO_RegistrationDetails()));

        }else {
            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_FROM_SHIFT_ONE, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getiWorkFromTimeFetch_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_TO_SHIFT_ONE, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getiWorkToTimeFetch_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_ONE_AM_PM_START, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getShiftAMPMFROM_RegistrationDetails()));
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_ONE_AM_PM_END, String.format("%s",RegistrationDetailsActivity. registrationDetailsActivityDTO.getShiftAMPMTO_RegistrationDetails()));

            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_FROM_SHIFT_TWO, "");
            postSignUpParametersMap.put(GigaDocsAPIConstants.I_WORK_TO_SHIFT_TWO,"");
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_TWO_AM_PM_START,"");
            postSignUpParametersMap.put(GigaDocsAPIConstants.SHIFT_TWO_AM_PM_END,"");
        }
        String result = serviceClass.postDataString(WebServiceURL.getRegisterUrl(),postSignUpParametersMap);
        return new JSONObject(result);
// complete
    }


    public static JSONObject postLoginService() throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String,String> postLoginserviceMap = new HashMap<>();
        postLoginserviceMap.put(GigaDocsAPIConstants.DOCTOR_LOGIN_EMAIL_ID, LoginActivity.loginActivityDTO.getEmail_LoginActivity());
        postLoginserviceMap.put(GigaDocsAPIConstants.DOCTOR_LOGIN_PASSWORD,LoginActivity.loginActivityDTO.getPassword_LoginActivity());
        String result = serviceClass.postDataString(WebServiceURL.getLoginUrl(),postLoginserviceMap);
        return new JSONObject(result);
    }
// complete
    public static JSONObject getSpecialitiesService() throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap <String,String> getSpecialities = new HashMap<>();
        String result = serviceClass.postDataString(WebServiceURL.getSPECIALITIES(),getSpecialities);
        return new JSONObject(result);
    }
//complete
    public  static JSONObject postAddPatientService(Context context) throws JSONException, GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException {

        HashMap<String,String>postAddPatientAddService = new HashMap<>();
        postAddPatientAddService.put(GigaDocsAPIConstants.NAME, PatientFragment.patientDTO.getName());
        postAddPatientAddService.put(GigaDocsAPIConstants.EMAIL, PatientFragment.patientDTO.getEmail());
        postAddPatientAddService.put(GigaDocsAPIConstants.MOBILE, PatientFragment.patientDTO.getMobileNo());
        postAddPatientAddService.put(GigaDocsAPIConstants.ADDRESS, PatientFragment.patientDTO.getAddress());
        postAddPatientAddService.put(GigaDocsAPIConstants.ID, PatientFragment.patientDTO.getPatientId());
        postAddPatientAddService.put(GigaDocsAPIConstants.PID, PatientFragment.pid);
        postAddPatientAddService.put("server_id", " ");
        postAddPatientAddService.put(GigaDocsAPIConstants.USER_ID, GigaDocsSharedPreferenceManager.getKey(context,GigaDocsAPIConstants.TOKEN,null));
        Log.i(GigadocsConstants.GIGADOCS,postAddPatientAddService.toString());
        String result = serviceClass.postDataString(WebServiceURL.getAddPatient(),postAddPatientAddService);

        return new JSONObject(result);
    }
    public  static JSONObject postAddAppointmentService(Context context) throws JSONException, GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException {

        HashMap<String,String>postAddAppointmentAddService = new HashMap<>();
        postAddAppointmentAddService.put(GigaDocsAPIConstants.DATE_APPOINTMENT, AppointmentBookingFragment.appointmentDTO.getSetDate());
        postAddAppointmentAddService.put(GigaDocsAPIConstants.SLOT_TIME, AppointmentBookingFragment.appointmentDTO.getSaveSlots());
        postAddAppointmentAddService.put(GigaDocsAPIConstants.PATIENT_ID, PatientDataBaseHelper.appointmentDTO.getPid());
        postAddAppointmentAddService.put(GigaDocsAPIConstants.ID, AppointmentBookingFragment.appointmentDTO.getPatientId());
        postAddAppointmentAddService.put(GigaDocsAPIConstants.APO_ID, AppointmentBookingFragment.apo_id );
        postAddAppointmentAddService.put(GigaDocsAPIConstants.SERVER_ID, " " );
        postAddAppointmentAddService.put(GigaDocsAPIConstants.USER_ID,  HomeScreenActivity.TOKEN);
//        Log.i("USER_ID",LoginActivity.loginToken );
        String result = serviceClass.postDataString(WebServiceURL.getAddAppointment(),postAddAppointmentAddService);

        return new JSONObject(result);
    }
    public static JSONObject getPatientListForDB(Context context) throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String,String>getPatientLitHashMap = new HashMap<>();
        getPatientLitHashMap.put(GigaDocsAPIConstants.USER_ID, LoginActivity.loginToken);
        String result =serviceClass.postDataString(WebServiceURL.getAddPatientList(),getPatientLitHashMap);
        return new JSONObject(result);
    }
    public static JSONObject getPatientListSyncForDB(Context context) throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String,String>getPatientLitHashMap = new HashMap<>();
        getPatientLitHashMap.put(GigaDocsAPIConstants.USER_ID,HomeScreenActivity.TOKEN);
        String result =serviceClass.postDataString(WebServiceURL.getAddPatientList(),getPatientLitHashMap);
        return new JSONObject(result);
    }

    public static JSONObject getAppointmentListForDB(Context context) throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String,String>getAppointmentLitHashMap = new HashMap<>();
        getAppointmentLitHashMap.put(GigaDocsAPIConstants.USER_ID,  LoginActivity.loginToken);
        String result =serviceClass.postDataString(WebServiceURL.getAddAppointmentList(),getAppointmentLitHashMap);
        return new JSONObject(result);
    }
 public static JSONObject getAppointmentListSyncForDB(Context context) throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String,String>getAppointmentLitHashMap = new HashMap<>();
        getAppointmentLitHashMap.put(GigaDocsAPIConstants.USER_ID,HomeScreenActivity.TOKEN);
        String result =serviceClass.postDataString(WebServiceURL.getAddAppointmentList(),getAppointmentLitHashMap);
        return new JSONObject(result);
    }

    public static JSONObject PostPrescriptionToServer(Context context) throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String,String>sendPrescriptionHashMap = new HashMap<>();
        sendPrescriptionHashMap.put(GigaDocsAPIConstants.USER_FILE, String.valueOf(PrescriptionActivity.encodedString));
        sendPrescriptionHashMap.put(GigaDocsAPIConstants.APPOINTMENT_ID_SEND_PRESCRIPTION, CalenderFragment.apo_id);
        sendPrescriptionHashMap.put(GigaDocsAPIConstants.PATIENT_ACCEPT, String.valueOf(PrescriptionActivity.one));
        sendPrescriptionHashMap.put(GigaDocsAPIConstants.USER_ID, GigaDocsSharedPreferenceManager.getKey(context,GigaDocsAPIConstants.TOKEN,null));
        String result =serviceClass.postDataString(WebServiceURL.getSendPrescription(),sendPrescriptionHashMap);
        return new JSONObject(result);
    }

    //PATIENT SYNC
    public  static JSONObject postPatientSync(Context context, HashMap<String, String > patientSyncMap) throws JSONException, GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException {
        PatientDataBaseHelper patientDataBaseHelper;
        String result = null;
        patientDataBaseHelper = new PatientDataBaseHelper(context);
        Cursor c = patientDataBaseHelper.viewAllData();
        patientSyncMap = new HashMap<>();

                while (c.moveToNext()) {
                    patientSyncMap.put(GigaDocsAPIConstants.USER_ID, HomeScreenActivity.TOKEN);
                    patientSyncMap.put("id", c.getString(0));
                    patientSyncMap.put("mobile", c.getString(1));
                    patientSyncMap.put("name", c.getString(2));
                    patientSyncMap.put("address", c.getString(3));
                    patientSyncMap.put("email", c.getString(4));
                    patientSyncMap.put("pid", c.getString(5));

                        patientSyncMap.put("server_id", c.getString(6));
                        Log.i("Patient Sync", patientSyncMap.toString());
                        result = serviceClass.postDataString(WebServiceURL.getAddPatient(), patientSyncMap);

                }
        return new JSONObject(result);
    }
    // APPOINTMENT SYNC
    public  static JSONObject postAppointmentSYNC(Context context, HashMap<String,String> appointmentSyncMap) throws JSONException, GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException {
            AppointmentDataBaseHelper appointmentDataBaseHelper;
            appointmentDataBaseHelper = new AppointmentDataBaseHelper(context);
            Cursor cursor = appointmentDataBaseHelper.viewAllData();
            String result =null;
            appointmentSyncMap = new HashMap<>();
            while(cursor.moveToNext()) {
                appointmentSyncMap.put(GigaDocsAPIConstants.USER_ID, HomeScreenActivity.TOKEN);
                appointmentSyncMap.put("id", cursor.getString(0));
                appointmentSyncMap.put("patient_id", cursor.getString(8));
                appointmentSyncMap.put("date_appoinment", cursor.getString(3));
                appointmentSyncMap.put("slot_time", cursor.getString(4));
                appointmentSyncMap.put("apo_id", "APOID0" + cursor.getString(0));
                appointmentSyncMap.put("prescription_status", cursor.getString(6));
                appointmentSyncMap.put("server_id", cursor.getString(7));
                Log.i("APPOINTMENT SYNC", appointmentSyncMap.toString());
                result = serviceClass.postDataString(WebServiceURL.getAddAppointment(), appointmentSyncMap);
            }
        return new JSONObject(result);
    }
    public static JSONObject forgotpasswordService() throws JSONException, GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException {
        HashMap<String,String>forgotpasswordMap = new HashMap<>();
        forgotpasswordMap.put(GigaDocsAPIConstants.DOCTOR_LOGIN_EMAIL_ID, ForgotPasswordActivity.forgotPassword);
        String result = serviceClass.postDataString(WebServiceURL.getForgotPassword(),forgotpasswordMap);
         return new JSONObject(result);
    }

    // PRESCRIPTION SYNC
    public static JSONObject PostPrescriptionSync(Context context) throws GigadocsDataUnavailableException, GigadocsUnAutorisedException, GigadocsTimeOutException, JSONException {
        HashMap<String, String> prescriptionMap;
        PrescriptionDataBaseHelper prescriptionDataBaseHelper = new PrescriptionDataBaseHelper(context);
        Cursor c = prescriptionDataBaseHelper.viewAllData();
        Bitmap bitmapImage;
        String encodedString,result = null;
        prescriptionMap = new HashMap<>();
        prescriptionMap.put(GigaDocsAPIConstants.USER_ID, HomeScreenActivity.TOKEN);
        while (c.moveToNext()) {
            prescriptionMap.put(GigaDocsAPIConstants.APPOINTMENT_ID_SEND_PRESCRIPTION, c.getString(0));
            prescriptionMap.put(GigaDocsAPIConstants.PATIENT_ACCEPT, c.getString(1));
            bitmapImage = BitmapFactory.decodeFile(c.getString(2));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            prescriptionMap.put(GigaDocsAPIConstants.USER_FILE, encodedString);
            result = serviceClass.postDataString(WebServiceURL.getSendPrescription(), prescriptionMap);
        }
        return new JSONObject(result);
    }
}
