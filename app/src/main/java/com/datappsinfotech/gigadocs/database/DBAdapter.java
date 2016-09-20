package com.datappsinfotech.gigadocs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.datappsinfotech.gigadocs.utils.dtos.PatientDTO;

import java.util.ArrayList;

public class DBAdapter {

    private static DBHelper dbHelper;

    public DBAdapter(@NonNull Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean insertPatientInfo(@NonNull PatientDTO patientDTO){
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.PATIENT_MOBILE,patientDTO.getMobileNo());
            contentValues.put(DBConstants.PATIENT_NAME,patientDTO.getName());
            contentValues.put(DBConstants.PATIENT_ADDRESS,patientDTO.getAddress());
            contentValues.put(DBConstants.PATIENT_EMAIL,patientDTO.getEmail());
            long result = db.insert(DBConstants.PATIENT_TABLE,null,contentValues);
            return result >0;
        }catch (Throwable t){
            t.printStackTrace();
        }finally {
            closeConnection(db,null);
        }

        return false;
    }

    @NonNull
    public ArrayList<PatientDTO> getAllPatientsInfo(){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<PatientDTO> patientDTOs = new ArrayList<>();
        try{
            db = dbHelper.getReadableDatabase();
            cursor = db.query(false,DBConstants.PATIENT_TABLE,new String[]{"*"},null,null,null,null,null,null);
            PatientDTO patientDTO;
            while (cursor.moveToNext()){
                patientDTO = new PatientDTO();
                patientDTO.setMobileNo(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_MOBILE)));
                patientDTO.setName(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_NAME)));
                patientDTO.setAddress(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_ADDRESS)));
                patientDTO.setEmail(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_EMAIL)));
                patientDTOs.add(patientDTO);
            }

        }catch (Throwable t){
            t.printStackTrace();
        }finally {
            closeConnection(db,cursor);
        }
        return patientDTOs;
    }

    public  boolean deletePatientInfo(@NonNull String mobileNumber ){
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            int result = db.delete(DBConstants.PATIENT_TABLE,DBConstants.PATIENT_MOBILE+"=?",new String[]{mobileNumber});
            return result>0;
        }catch (Throwable t){
            t.printStackTrace();
        }finally {
//            closeConnection(db,null);
        }
        return false;
    }

    @NonNull
    public static ArrayList<PatientDTO> getPatientInfo(@NonNull String mobileNumber){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<PatientDTO> patientDTOs = new ArrayList<>();
        try{
            db = dbHelper.getReadableDatabase();
            cursor = db.query(false,DBConstants.PATIENT_TABLE,new String[]{"*"},DBConstants.PATIENT_MOBILE+" LIKE ? ",new String[]{String.format("%%%s%%", mobileNumber)},null,null,null,null);
            PatientDTO patientDTO;
            while (cursor.moveToNext()){
                patientDTO = new PatientDTO();
                patientDTO.setMobileNo(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_MOBILE)));
                patientDTO.setName(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_NAME)));
                patientDTO.setAddress(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_ADDRESS)));
                patientDTO.setEmail(cursor.getString(cursor.getColumnIndex(DBConstants.PATIENT_EMAIL)));
                patientDTOs.add(patientDTO);
            }
        }catch (Throwable t){
            t.printStackTrace();
        }finally {
//            closeConnection(db,cursor);
        }
        return patientDTOs;
    }


    public static class DBHelper extends SQLiteOpenHelper{

       /*Create Queries*/
       /*"CREATE TABLE " + TABLE_NAME+ " (PATIENT_MOBILE INTEGER PRIMARY KEY, PATIENT_NAME TEXT, PATIENT_ADDRESS TEXT, PATIENT_EMAIL TEXT)")*/
       private static final String CREATE_PATIENT_QUERY= "CREATE TABLE "+DBConstants.PATIENT_TABLE +"("+
               DBConstants.PATIENT_MOBILE +" TEXT, "+
               DBConstants.PATIENT_NAME+" TEXT, "+
               DBConstants.PATIENT_ADDRESS+" TEXT, "+
               DBConstants.PATIENT_EMAIL+" TEXT );";

       /*Drop Queries*/
       private  static  final String DROP_PATIENT_QUERIES = "DROP TABLE IF EXISTS "+DBConstants.PATIENT_TABLE+";";

       public DBHelper(@NonNull Context context) {
           super(context, DBConstants.DATABASE_NAME, null, DBConstants.DB_VERSION);
       }

       @Override
       public void onCreate(SQLiteDatabase db) {
        try{
            createDDL(db);
        }catch (Throwable t){
            t.printStackTrace();
        }
       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           try{
            dropDDL(db);
            onCreate(db);
           }catch (Throwable t){
               t.printStackTrace();
           }

       }

       private void createDDL(@NonNull SQLiteDatabase db) throws SQLiteException{
            db.execSQL(CREATE_PATIENT_QUERY);
       }

       private void dropDDL(@NonNull SQLiteDatabase db) throws SQLiteException{
            db.execSQL(DROP_PATIENT_QUERIES);

       }
   }

    private void closeConnection(SQLiteDatabase db, Cursor cursor){
        if (cursor!=null)
        cursor.close();
        if (db!=null)
            db.close();
    }
}

