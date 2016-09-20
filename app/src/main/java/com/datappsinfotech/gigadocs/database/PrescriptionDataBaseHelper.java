package com.datappsinfotech.gigadocs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datappsinfotech.gigadocs.utils.dtos.PatientDTO;


public class PrescriptionDataBaseHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="prescription.db";

    public static final String PRESCRIPTION_TABLE_NAME="prescription_table";
    public static final String PCOL1="APPOINTMENT_ID";
    public static final String PCOL2="PATIENT_ACCEPT";
    public static final String PCOL3="IMAGE";



    public PrescriptionDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PRESCRIPTION_TABLE_NAME+ " (APPOINTMENT_ID TEXT, PATIENT_ACCEPT INTEGER, IMAGE TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PRESCRIPTION_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String appointment_id, String patiente_accept, String image){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put(PCOL1, appointment_id);
        contentValues.put(PCOL2, patiente_accept);
        contentValues.put(PCOL3, image);

        long result= db.insert(PRESCRIPTION_TABLE_NAME, null, contentValues);
        if(result== -1){
            return false;
        }
        else
            return true;
    }
    public void deleteTable(){
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            db.delete(PRESCRIPTION_TABLE_NAME, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public Cursor viewAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res= db.rawQuery("select * from "+ PRESCRIPTION_TABLE_NAME, null);
        return res;
    }
}
