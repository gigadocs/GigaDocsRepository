package com.datappsinfotech.gigadocs.utils.services;


public class WebServiceURL {

    private static final String PROTOCOL = "http";
    private static final String HOST = "www.gigadocs.com";
    private static final String SERVICE = "giga_api";

    /*Base Url*/
    private static final String BASE_URL = String.format("%s://%s/%s", PROTOCOL,HOST,SERVICE);

    /*Urls*/
    private static final String REGISTER_URL = "signup";
    private static final String LOGIN_URL = "login";
    private static final String SPECIALITIES = "specialities";
    private static final String ADD_PATIENT = "addpatient";
    private static final String ADD_PATIENT_LIST = "patientlist";
    private static final String ADD_APPOINTMENT = "addappoinment";
    private static final String ADD_APPOINTMENT_LIST = "appoinmentlist";
    private static final String SEND_PRESCRIPTION = "addprescription";
    private static final String FORGOT_PASSWORD = "forgetpswd";



    public static String getRegisterUrl(){
        return String.format("%s/%s", BASE_URL,REGISTER_URL);
    }
    public static String getLoginUrl() {
        return String.format("%s/%s", BASE_URL,LOGIN_URL);
    }
    public static String getSPECIALITIES() {
        return String.format("%s/%s", BASE_URL,SPECIALITIES);
    }
    public static String getAddPatient() {
        return String.format("%s/%s", BASE_URL,ADD_PATIENT);
    }
    public static String getAddPatientList() {
        return String.format("%s/%s", BASE_URL,ADD_PATIENT_LIST);
    }
    public static String getAddAppointment() {
        return String.format("%s/%s", BASE_URL,ADD_APPOINTMENT);
    }
    public static String getAddAppointmentList() {
        return String.format("%s/%s", BASE_URL,ADD_APPOINTMENT_LIST);
    }
    public static String getSendPrescription() {
        return String.format("%s/%s", BASE_URL,SEND_PRESCRIPTION);
    }

    public static String getForgotPassword() {
        return String.format("%s/%s", BASE_URL,FORGOT_PASSWORD);
    }

}
