package com.datappsinfotech.gigadocs.utils.gigadocsutils;

/**
 * Created by sandeep123 on 6/25/2016.
 */
public interface GigaDocsAPIConstants {

    String AVAILABLE_SLOTS = "shft_timing";
    /* signUp first screen service */

    String NAME = "name";
    String EMAIL = "email";
    String MOBILE = "mobile";
    String PASSWORD = "password";
    String SPECIALITIES = "specialities";
    String SPECIALITY = "speciality";
    String PATIENT_NAME = "patient_name";
    String PATIENT_ID = "patient_id";
    String PATIENT_MOBILE = "patient_mobile";

    /* signUp second screen service */

    String ADDRESS = "address";
    String I_ATTEND_PATIENT = "slots";
    String I_WORK_FROM_SHIFT_ONE = "start_time";
    String I_WORK_FROM_SHIFT_TWO = "start_time1";
    String I_WORK_TO_SHIFT_ONE = "end_time";
    String I_WORK_TO_SHIFT_TWO = "end_time1";
    String SHIFT_ONE_AM_PM_START = "start_ampm";
    String SHIFT_ONE_AM_PM_END = "end_ampm";
    String SHIFT_TWO_AM_PM_START = "start_ampm1";
    String SHIFT_TWO_AM_PM_END = "end_ampm1";

    /*   Appointment List              */
     String GET_APPOINTMENT_LIST_TO_DB ="message";
     String GET_APPOINTMENT_ID_TO_DB ="id";
     String GET_APPOINTMENT_SLOT_TO_DB ="slot_time";
     String GET_APPOINTMENT_DATE_OF_APPOINTMENT_TO_DB ="date_appoinment";
     String GET_APPOINTMENT_APPOINTMENT_UNIQUE_ID_TO_DB ="apo_id";

    /*   Patient List              */

    String GET_PATIENT_LIST_TO_DB ="message";

    String GET_PATIENT_ID_TO_DB ="id";
    String GET_PATIENT_NAME_TO_DB ="name";
    String GET_PATIENT_ADDRESS_TO_DB ="description";
    String GET_PATIENT_MOBILE_TO_DB ="mobile";
    String GET_PATIENT_EMAIL_TO_DB ="email";

    /*Add Prescription To Server*/
    String USER_FILE ="attach";
    String APPOINTMENT_ID_SEND_PRESCRIPTION ="appoint_id";
    String PATIENT_ACCEPT ="patient_accept";


/* LOgin Api Details*/
    String DOCTOR_LOGIN_EMAIL_ID="email_id";
    String DOCTOR_LOGIN_PASSWORD="password";

    String STATUS = "Status";
    String TOKEN = "token";
    String SHIFT_TIMINGS ="shft_timing";
    String MESSAGES ="message";
    String SPECIALITIES_ID ="id";
    String USER_ID ="user_id" ;
    String PID ="pid" ;
//    String PATIENT_ID ="id" ;
    String DATE_APPOINTMENT ="date_appoinment" ;
    String SLOT_TIME ="slot_time" ;
    String APO_ID = "apo_id";
    String ID ="id" ;
    String SERVER_ID ="server_id" ;
    String DOCTOR_NAME ="name";
    String DOCTOR_MOBILE = "mobile";
    String DOCTOR_SPECIALITY = "specialities";
    String DOCTOR_ADDRESS = "address";
    String DOCTOR_TIME = "date_created";
    String ALL_DETAILS ="all_detils" ;
    String PRESCRIPTION_STATUS ="prescription_status" ;
}
