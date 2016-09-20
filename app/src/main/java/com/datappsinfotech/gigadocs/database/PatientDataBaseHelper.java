package com.datappsinfotech.gigadocs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.datappsinfotech.gigadocs.fragments.PatientFragment;
import com.datappsinfotech.gigadocs.utils.dtos.AppointmentDTO;
import com.datappsinfotech.gigadocs.utils.dtos.PatientDTO;

import java.util.ArrayList;


public class PatientDataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="patient.db";
    public static final String PATIENT_TABLE_NAME="patient_table";
    public static final String PCOL1="PATIENT_ID";
    public static final String PCOL2="PATIENT_MOBILE";
    public static final String PCOL3="PATIENT_NAME";
    public static final String PCOL4="PATIENT_ADDRESS";
    public static final String PCOL5="PATIENT_EMAIL";
    public static final String PCOL6="PID";
    public static final String PCOL7="SERVER_ID";

    public static PatientDTO patientDTO = new PatientDTO();
    public static AppointmentDTO appointmentDTO = new AppointmentDTO();

    public PatientDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PATIENT_TABLE_NAME+ " (PATIENT_ID INTEGER PRIMARY KEY, PATIENT_MOBILE INTEGER, PATIENT_NAME TEXT, PATIENT_ADDRESS TEXT, PATIENT_EMAIL TEXT, PID TEXT, SERVER_ID TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PATIENT_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String mobile, String name, String address, String email, String pid, String server_id){
        SQLiteDatabase db= this.getWritableDatabase();
        long result=0;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PCOL2, mobile);
            contentValues.put(PCOL3, name);
            contentValues.put(PCOL4, address);
            contentValues.put(PCOL5, email);
            contentValues.put(PCOL6, pid);
            contentValues.put(PCOL7, server_id);
            result = db.insert(PATIENT_TABLE_NAME, null, contentValues);
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }

        if(result== -1){
            return false;
        }
        else
            return true;
        }
    public void deleteTable(){
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            db.delete(PATIENT_TABLE_NAME, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public Cursor viewAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res= null;
        try {
            res = db.rawQuery("select * from " + PATIENT_TABLE_NAME, null);
        }
        catch (SQLiteException se ) {
        }


        return res;
    }

    public ArrayList<PatientDTO>viewSelectedData(String name, String mobile){
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<PatientDTO> patientDTOs = new ArrayList<>();
        try {
            String query = "SELECT * FROM "+ PATIENT_TABLE_NAME + " where PATIENT_NAME = ? and PATIENT_MOBILE = ?";
            Cursor c = db.rawQuery(query, new String[] {name, mobile});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        patientDTO.setMobileNo(c.getString(c.getColumnIndex(PCOL2)));
                        patientDTO.setName(c.getString(c.getColumnIndex(PCOL3)));
                        patientDTO.setAddress(c.getString(c.getColumnIndex(PCOL4)));
                        patientDTO.setEmail(c.getString(c.getColumnIndex(PCOL5)));
                        patientDTO.setGetPid(c.getString(c.getColumnIndex(PCOL6)));
                        appointmentDTO.setPid(c.getString(c.getColumnIndex(PCOL6)));
                        Log.i("setPid",appointmentDTO.getPid());


                        patientDTOs.add(patientDTO);
                    } while (c.moveToNext());
                }
            }
        }
        catch (SQLiteException se ) {
        } finally {
            if (db != null)

                db.close();
        }

        return patientDTOs;
    }

    public Cursor viewSelectedDataForCalendar(String name, String test1){
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<PatientDTO> patientDTOs = new ArrayList<>();
            String query = "SELECT * FROM "+ PATIENT_TABLE_NAME + " where PATIENT_NAME = ? and PATIENT_MOBILE = ?";
            Cursor c = db.rawQuery(query, new String[] {name, test1});
        return c;
    }
    public boolean updateData(String mobile, String name, String address, String email, String pid){
        Log.i("pid to update", pid);
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(PCOL2, mobile);
        contentValues.put(PCOL3, name);
        contentValues.put(PCOL4, address);
        contentValues.put(PCOL5, email);
        long result=db.update(PATIENT_TABLE_NAME, contentValues, "PID='"+pid+"'",null);
        if(result== -1){
            return false;
        }
        else {
            return true;
        }
    }

//    public boolean updateDataAfterInsertion(String mobile, String name, String address, String email, String pid, String server_id){
//        SQLiteDatabase db= this.getWritableDatabase();
//        ContentValues contentValues= new ContentValues();
//        contentValues.put(PCOL2, mobile);
//        contentValues.put(PCOL3, name);
//        contentValues.put(PCOL4, address);
//        contentValues.put(PCOL5, email);
//        contentValues.put(PCOL6, pid);
//        contentValues.put(PCOL7, server_id);
//        long result=db.update(PATIENT_TABLE_NAME, contentValues, "PATIENT_MOBILE= ? and PATIENT_NAME = ? and PATIENT_ADDRESS = ? and PATIENT_EMAIL = ? and PID = ? and SERVER_ID = ?" , new String[] {mobile, name, address,email,pid,server_id});
//        return result != -1;
//    }
    public boolean updateDataWithServerID(String mobile, String name, String address, String email, String pid, String server_id,String responsePatientID){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(PCOL2, mobile);
        contentValues.put(PCOL3, name);
        contentValues.put(PCOL4, address);
        contentValues.put(PCOL5, email);
        contentValues.put(PCOL6, pid);
        contentValues.put(PCOL7, server_id);
        long result=db.update(PATIENT_TABLE_NAME, contentValues, "PATIENT_ID='"+responsePatientID+"'",null);
        //long result=db.update(PATIENT_TABLE_NAME, contentValues, PCOL1+" = "+responsePatientID ,null);
        return result != -1;
    }

    public boolean updateSingleData(String name){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(PCOL3, name);
        long result=db.update(PATIENT_TABLE_NAME, contentValues, "PATIENT_MOBILE= ?", new String[] {name});
        if(result== -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Integer deleteData(String mobile){
        SQLiteDatabase db= this.getWritableDatabase();
        return db.delete(PATIENT_TABLE_NAME, "PATIENT_MOBILE= ?", new String[] {mobile});
    }

    public Cursor getSearch(String search) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "
                + PATIENT_TABLE_NAME + " where " + PCOL2 + " like '" + search
                + "%'", null);
        return c;
    }

    public Cursor getMobileNo(String search){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT distinct PATIENT_MOBILE FROM "
                + PATIENT_TABLE_NAME + " where " + PCOL2 + " like '" + search
                + "%'", null);
        return c;
    }

    public Cursor getId(String mobile, String name, String address, String email){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT distinct PATIENT_ID FROM " + PATIENT_TABLE_NAME + " where PATIENT_MOBILE = ? and " +
                    "PATIENT_NAME = ? and " +
                    "PATIENT_ADDRESS = ? " +
                    "and PATIENT_EMAIL = ?";
            Cursor c = db.rawQuery(query, new String[]{mobile, name, address, email});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        PatientFragment.patientDTO.setPatientId(c.getString(c.getColumnIndex(PCOL1)));
                    } while (c.moveToNext());
                }
            }
        }
        catch (SQLiteException se ) {
        } finally {
            if (db != null)

                db.close();
        }

        return null;
    }

    public Cursor getName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT PATIENT_NAME FROM "
                + PATIENT_TABLE_NAME + " where " + PCOL2 + " like '" + name
                + "%'", null);
        return c;
    }
    }

