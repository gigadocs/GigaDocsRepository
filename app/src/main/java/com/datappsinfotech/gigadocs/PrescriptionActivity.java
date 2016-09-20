package com.datappsinfotech.gigadocs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.autosync.PrescriptionSync;
import com.datappsinfotech.gigadocs.database.PrescriptionDataBaseHelper;
import com.datappsinfotech.gigadocs.fragments.CalenderFragment;
import com.datappsinfotech.gigadocs.utils.dtos.CalendarDTO;
import com.datappsinfotech.gigadocs.utils.dtos.PrescriptionDTO;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsSharedPreferenceManager;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;
import com.datappsinfotech.gigadocs.widgets.AVUtils;
import com.datappsinfotech.gigadocs.widgets.InternetFailureDialog;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PrescriptionActivity extends Activity implements View.OnClickListener
{
//    ImageView imageView;
    @BindView(R.id.text_viewPrescriptionPatientName)
    TextView textViewPatientName;
    @BindView(R.id.shareprescription)
    ImageView buttonShare;
    @BindView(R.id.save)
    ImageView buttonSave;
    @BindView(R.id.clearPaint)
    ImageView buttonClear;
    @BindView(R.id.checkboxIaccept)
    CheckBox checkBox;
    @BindView(R.id.linearLayoutImage)
    LinearLayout linearLayoutImage;
    @BindView(R.id.text_viewDoctorName)
    TextView text_viewDoctorName;
    @BindView(R.id.textViewSpeciality)
    TextView textViewSpeciality;
    @BindView(R.id.textViewDoctorAddress)
    TextView textViewDoctorAddress;
    @BindView(R.id.textViewDoctorMobile)
    TextView textViewDoctorMobile;
    @BindView(R.id.textViewTime)
    TextView textViewTime;
    @BindView(R.id.text_viewPatientAddress)
    TextView text_viewPatientAddress;
    @BindView(R.id.text_viewPatientEmail)
    TextView text_viewPatientEmail;
    @BindView(R.id.text_viewPatientMobile)
    TextView text_viewPatientMobile;

    public static Dialog dialog;
    public static CalendarDTO calendarDTO;
    public static PrescriptionDTO prescriptionDTO;

    PrescriptionSync prescriptionSync;

    Bitmap bitmapImage;
    MyDrawView myDrawView;
    String filename;
    public  static  String encodedString;
    public  static  int one;
    Button buttonRetry,buttonDismiss;
    public static File pictureFileDir;
    public static File pictureFile;
    PrescriptionDataBaseHelper prescriptionDataBaseHelper;
    public static boolean flag;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_prescription);
            ButterKnife.bind(this);
            prescriptionSync = new PrescriptionSync(this);
            calendarDTO = new CalendarDTO();
            prescriptionDTO = new PrescriptionDTO();
            prescriptionDataBaseHelper = new PrescriptionDataBaseHelper(this);
            myDrawView=(MyDrawView)findViewById(R.id.draw);
            dialog = AVUtils.getAnimDialog(this);
            LoginActivity.internetFailureDialog = InternetFailureDialog.getAnimDialog(this);
             buttonRetry = (Button)LoginActivity.internetFailureDialog.findViewById(R.id.buttonRetry);
             buttonDismiss = (Button)LoginActivity.internetFailureDialog.findViewById(R.id.buttonDismiss);
            text_viewDoctorName.setText("Doctor name : Dr. "+ GigaDocsSharedPreferenceManager.getKey(getApplicationContext(),GigaDocsAPIConstants.DOCTOR_NAME,null));
            textViewDoctorMobile.setText("Mobile : "+ GigaDocsSharedPreferenceManager.getKey(getApplicationContext(),GigaDocsAPIConstants.DOCTOR_MOBILE,null));
            textViewSpeciality.setText("Speciality : "+ GigaDocsSharedPreferenceManager.getKey(getApplicationContext(),GigaDocsAPIConstants.DOCTOR_SPECIALITY,null));
            textViewDoctorAddress.setText("Address : "+GigaDocsSharedPreferenceManager.getKey(getApplicationContext(),GigaDocsAPIConstants.DOCTOR_ADDRESS,null));
            textViewTime.setText("Date : "+ GigaDocsSharedPreferenceManager.getKey(getApplicationContext(),GigaDocsAPIConstants.DOCTOR_TIME,null));

            textViewPatientName.setText("Patient Name : "+ CalenderFragment.patientNAME);
            text_viewPatientMobile.setText("Patient Mobile : "+ CalenderFragment.patientMobile);
            text_viewPatientEmail.setText("Patient Email : "+ CalenderFragment.PatientEmail);
            text_viewPatientAddress.setText("Patient Address : "+ CalenderFragment.patientAddress);

//          textViewAppointmentId.setText();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            if (buttonSave != null) {
                    buttonSave.setOnClickListener(this);
            }
            if (buttonShare!=null){
                    buttonShare.setOnClickListener(this);
            }
            if (buttonClear!=null){
                    buttonClear.setOnClickListener(this);
            }
            if (checkBox!=null){
                checkBox.setOnClickListener(this);
            }
            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (CheckInternetConnection.isConnectionAvailable(PrescriptionActivity.this)) {
                            new SendPrescriptionToServerAsyncTask().execute();
                            finish();
                        }else{
                            LoginActivity.internetFailureDialog.show();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            buttonDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginActivity.internetFailureDialog.dismiss();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkboxIaccept:
                if (checkBox.isChecked()){
                    one =1;
                    System.out.println(one);
                }else {
                    one =0;
                    System.out.println(one);
                }
                break;
            case R.id.save:
                try {
                    saveBitMap(this, linearLayoutImage);
                    saveBitMap(getApplicationContext(),linearLayoutImage);
                    Toast.makeText(PrescriptionActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                    saved();
                    bitmapImage= BitmapFactory.decodeFile(pictureFile.getPath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();
                    encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);

                    try {
                        if (CheckInternetConnection.isConnectionAvailable(this)) {

                            new SendPrescriptionToServerAsyncTask().execute();
                        }else{
                            try {

                                prescriptionDataBaseHelper.insertData(
                                        CalenderFragment.apo_id,
                                        String.valueOf(PrescriptionActivity.one),
                                        pictureFile.getPath()
                                );

                                Log.i("PRESSSSS",  CalenderFragment.apo_id);
                                Log.i("PRESSSSS",  String.valueOf(PrescriptionActivity.one));
                                Log.i("PRESSSSS", pictureFile.getPath());
                                System.out.println("Successfully Inserted in to database");
                                flag =true;
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            case R.id.shareprescription:
                try {
                    saveBitMap(getApplicationContext(),linearLayoutImage);
                    final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
                    emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent1.putExtra(Intent.EXTRA_STREAM,   Uri.parse(pictureFile.getPath()) );
                    emailIntent1.setType("image/jpg");
                    startActivity(emailIntent1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
                break;
            case R.id.clearPaint:
                try {
                    myDrawView.clear();
                }catch (Throwable t){
                    t.printStackTrace();
                }
                break;
        }
    }
    private File saveBitMap(Context context, View drawView){
        try {
            pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Handcare");
            if (!pictureFileDir.exists()) {
                boolean isDirectoryCreated = pictureFileDir.mkdirs();
                if(!isDirectoryCreated)
                    Log.i("ATG", "Can't create directory to save the image");
                return null;
            }
            filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis()+".jpg";
            pictureFile = new File(filename);
            Bitmap bitmap =getBitmapFromView(drawView);

            try {
                pictureFile.createNewFile();
                FileOutputStream oStream = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
                oStream.flush();
                oStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("TAG", "There was an issue saving the image.");
            }
            scanGallery((Context) context,pictureFile.getAbsolutePath());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return pictureFile;
    }
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = null;
        try {
            //Define a bitmap with the same size as the view
            returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
            //Bind a canvas to it
            Canvas canvas = new Canvas(returnedBitmap);
            //Get the view's background
            Drawable bgDrawable = view.getBackground();
            if (bgDrawable != null) {
                //has background drawable, then draw it on the canvas
                bgDrawable.draw(canvas);
            } else {
                //does not have background drawable, then draw white background on the canvas
                canvas.drawColor(Color.WHITE);
            }
            // draw the view on the canvas
            view.draw(canvas);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //return the bitmap
        return returnedBitmap;
    }
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
public void saved(){
    try {
        CalenderFragment.calendarDTO.setSaved(true);
        CalenderFragment.array.set(CalenderFragment.calendarDTO.getPosition(), CalenderFragment.calendarDTO);
    } catch (Throwable e) {
        e.printStackTrace();
    }
}
    public class SendPrescriptionToServerAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.show();
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.PostPrescriptionToServer(PrescriptionActivity.this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                if (jsonObject.getBoolean(GigaDocsAPIConstants.STATUS)){
                    Toast.makeText(getApplicationContext(),"Prescription Synced to Server ",Toast.LENGTH_LONG).show();
                    try {
                        if (flag){
                            prescriptionSync.prescriptionSync();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Failed to send the prescription to server ", Toast.LENGTH_SHORT).show();
                }
//                dialog.dismiss();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}