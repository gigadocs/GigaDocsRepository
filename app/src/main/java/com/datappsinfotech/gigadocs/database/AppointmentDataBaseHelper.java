package com.datappsinfotech.gigadocs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.datappsinfotech.gigadocs.fragments.AppointmentBookingFragment;


public class AppointmentDataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="appointment.db";
    public static final String APPOINTMENT_TABLE_NAME="appointment_table";
    public static final String ACOL1="APPOINTMENT_ID";
    public static final String ACOL2="PATIENT_MOBILE";
    public static final String ACOL3="PATIENT_NAME";
    public static final String ACOL4="APPOINTMENT_DATE";
    public static final String ACOL5="APPOINTMENT_SLOT";
    public static final String ACOL6="APO_ID";
    public static final String ACOL7="STATUS";
    public static final String ACOL8="SERVER_ID";
    public static final String ACOL9="PATIENT_ID";

    public AppointmentDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + APPOINTMENT_TABLE_NAME+ " (APPOINTMENT_ID INTEGER PRIMARY KEY, PATIENT_MOBILE INTEGER, PATIENT_NAME TEXT, APPOINTMENT_DATE TEXT, APPOINTMENT_SLOT TEXT, APO_ID TEXT, STATUS TEXT, SERVER_ID TEXT, PATIENT_ID TEXT )");
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS "+APPOINTMENT_TABLE_NAME);
            onCreate(db);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean insertData(String mobile, String name, String date, String slots ,String apo_id, String server_id, String patient_id, String prescription_status){
        try {
            SQLiteDatabase db= this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();
            contentValues.put(ACOL2, mobile);
            contentValues.put(ACOL3, name);
            contentValues.put(ACOL4, date);
            contentValues.put(ACOL5, slots);
            contentValues.put(ACOL6, apo_id);
            contentValues.put(ACOL7, prescription_status);
            contentValues.put(ACOL8, server_id);
            contentValues.put(ACOL9, patient_id);

            long result= db.insert(APPOINTMENT_TABLE_NAME, null, contentValues);
            if(result== -1){
                return false;
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

//    public void deleteTable(String server_id){
//        try {
//            SQLiteDatabase db=this.getWritableDatabase();
//            db.delete(APPOINTMENT_TABLE_NAME,
//                    "SERVER_ID" + "=? ",
//                    new String[] {server_id});
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
////        db.delete(APPOINTMENT_TABLE_NAME, "SERVER_ID", null);
//
//    }

    public void deleteTable(){
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            db.delete(APPOINTMENT_TABLE_NAME, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    public Cursor viewAllData(){
        Cursor res= null;
        try {
            SQLiteDatabase db=this.getWritableDatabase();

            res = db.rawQuery("select * from "+ APPOINTMENT_TABLE_NAME, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return res;
    }

    public Cursor viewAllDataForLogout(String server_id){
        Cursor c = null;
        try {
            SQLiteDatabase db=this.getWritableDatabase();

            c = db.rawQuery("select * from "
                    + APPOINTMENT_TABLE_NAME + " where " + ACOL8 + " like '" + server_id
                    + "%'", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }

    public void updateData( String slot, String status){
        Log.i("Slot", slot);
//        Log.i("Status", status);

        try {
            SQLiteDatabase db= this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();
            contentValues.put(ACOL7, status);
            long result=db.update(APPOINTMENT_TABLE_NAME, contentValues, "APPOINTMENT_SLOT= ?", new String[] {slot});
//        if(result== -1){
//            return false;
//        }
//        else {
//            return true;
//        }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public boolean updateDataWithApoid(String apo_id, String name, String date, String slot){
        try {
            SQLiteDatabase db= this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();
            contentValues.put(ACOL3, name);
            contentValues.put(ACOL4, date);
            contentValues.put(ACOL5, slot);
            long result=db.update(APPOINTMENT_TABLE_NAME, contentValues, "APO_ID= ?", new String[] {apo_id});
            if(result== -1){
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean updateDataAfterInsertion(String mobile, String name, String date, String slot, String apo_id, String  server_id, String patient_id){
        try {
            SQLiteDatabase db= this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();
            contentValues.put(ACOL2, mobile);
            contentValues.put(ACOL3, name);
            contentValues.put(ACOL4, date);
            contentValues.put(ACOL5, slot);
            contentValues.put(ACOL6, apo_id);
            contentValues.put(ACOL8, server_id);
            contentValues.put(ACOL9, patient_id);
            long result=db.update(APPOINTMENT_TABLE_NAME, contentValues, "PATIENT_MOBILE= ? and PATIENT_NAME = ? and APPOINTMENT_DATE = ? and APPOINTMENT_SLOT = ?", new String[] {mobile, name, date, slot});

            if(result== -1){
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }
    public Cursor getCalanderData(String date){
        Cursor c = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("SELECT PATIENT_NAME, APPOINTMENT_SLOT, PATIENT_MOBILE, APO_ID FROM "
                    + APPOINTMENT_TABLE_NAME + " where " + ACOL4 + " like '" + date
                    + "%'", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }

    public Cursor getApoId(String mobile, String slot){
        Cursor c = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT distinct APO_ID FROM " + APPOINTMENT_TABLE_NAME + " where PATIENT_MOBILE = ? and " + "APPOINTMENT_SLOT = ? ";
             c = db.rawQuery(query, new String[]{mobile, slot});
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return c;
    }

    public void getId(String mobile, String name, String date, String slot){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT distinct APPOINTMENT_ID FROM " + APPOINTMENT_TABLE_NAME + " where PATIENT_MOBILE = ? and " +
                    "PATIENT_NAME = ? and " +
                    "APPOINTMENT_DATE = ? " +
                    "and APPOINTMENT_SLOT = ?";
            Cursor c = db.rawQuery(query, new String[]{mobile, name, date, slot});
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        AppointmentBookingFragment.appointmentDTO.setPatientId(c.getString(c.getColumnIndex(ACOL1)));
                    } while (c.moveToNext());
                }
            }
        }
        catch (Throwable se ) {
        } finally {
            if (db != null)

                db.close();
        }
    }

    public Cursor chechSlotAvailability(String name) {
        Cursor c = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("SELECT * FROM "
                    + APPOINTMENT_TABLE_NAME + " where " + ACOL6 + " like '" + name
                    + "%'", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }
    public Cursor getApoId(String search){
        Cursor c = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("SELECT distinct APO_ID FROM "
                    + APPOINTMENT_TABLE_NAME + " where " + ACOL6 + " like '" + search
                    + "%'", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }

    public Cursor getName(String name){
        Cursor c = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("SELECT PATIENT_NAME FROM "
                    + APPOINTMENT_TABLE_NAME + " where " + ACOL6 + " like '" + name
                    + "%'", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }
    public Cursor getDate(){
        Cursor c= null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("select APPOINTMENT_DATE, APPOINTMENT_SLOT from "+ APPOINTMENT_TABLE_NAME, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }

    public Cursor getSlot(String date){
        Cursor c = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("SELECT APPOINTMENT_SLOT FROM "
                    + APPOINTMENT_TABLE_NAME + " where " + ACOL4 + " like '" + date
                    + "%'", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }

    public Cursor getStatus(String date, String slot){
        Cursor c = null;
        try {
            Log.i("Slot getStatus", slot);
            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery("SELECT STATUS FROM "
                    + APPOINTMENT_TABLE_NAME + " where APPOINTMENT_DATE = ? " +
                    "and APPOINTMENT_SLOT = ?" , new String[]{date, slot});
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return c;
    }


    public void deleteSlot(String apo_id){
        try {
            SQLiteDatabase db=this.getWritableDatabase();

            db.rawQuery("DELETE APPOINTMENT_SLOT FROM " + APPOINTMENT_TABLE_NAME + " where " + ACOL6 + " like '"+ apo_id
                    + "%'", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
  }

