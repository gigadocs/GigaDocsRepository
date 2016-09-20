package com.datappsinfotech.gigadocs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class TestDb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="test.db";

    public static final String APPOINTMENT_TABLE_NAME="test_table";
    public static final String ACOL1="APPOINTMENT_ID";
    public static final String ACOL2="PATIENT_MOBILE";
    public static final String ACOL3="PATIENT_NAME";
    public static final String ACOL4="PATIENT_ADDRESS";
    public static final String ACOL5="PATIENT_EMAIL";

    public TestDb(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + APPOINTMENT_TABLE_NAME+ " (APPOINTMENT_ID INTEGER PRIMARY KEY, PATIENT_MOBILE INTEGER, PATIENT_NAME TEXT, PATIENT_ADDRESS TEXT, PATIENT_EMAIL TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+APPOINTMENT_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTestData(String mobile, String name, String address, String email){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        Log.i("TestDb1",mobile);
        Log.i("TestDb1",name);
        Log.i("TestDb1",address);
        Log.i("TestDb1",email);
        contentValues.put(ACOL2,mobile);
        contentValues.put(ACOL3, name);
        contentValues.put(ACOL4, address);
        contentValues.put(ACOL5, email);

        long result= db.insert(APPOINTMENT_TABLE_NAME, null, contentValues);
        if(result== -1){
            return false;
        }
        else
            return true;
    }

    public void deleteTable(){
        SQLiteDatabase db=this.getWritableDatabase();

        db.delete(APPOINTMENT_TABLE_NAME, null, null);

    }
    public Cursor viewAllData(){
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor res= db.rawQuery("select * from "+ APPOINTMENT_TABLE_NAME, null);
        return res;
    }

    public ArrayList<String> viewAllDataTest(){
        ArrayList<String > arraylist = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor c= db.rawQuery("select * from "+ APPOINTMENT_TABLE_NAME, null);
        while(c.moveToNext()) {
            arraylist.add(c.getString(1));
            arraylist.add(c.getString(2));
            arraylist.add(c.getString(3));
            arraylist.add(c.getString(4));

        }
        c.close();
        Log.i("Tag",arraylist.toString());
        return arraylist;
    }

}
