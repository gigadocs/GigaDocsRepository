package com.datappsinfotech.gigadocs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.datappsinfotech.gigadocs.R;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsDataUnavailableException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsTimeOutException;
import com.datappsinfotech.gigadocs.utils.exception.GigadocsUnAutorisedException;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.services.CheckInternetConnection;
import com.datappsinfotech.gigadocs.utils.services.WebServiceClass;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {
@BindView(R.id.edit_textPatientEmailID)
    EditText editTextForgotPassword;
    @BindView(R.id.buttonReset)
    Button buttonReset;

   public static String forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_forgot_password);
            ButterKnife.bind(this);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (CheckInternetConnection.isConnectionAvailable(this)) {
                buttonReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forgotPassword = editTextForgotPassword.getText().toString();
                        new ForgotPasswordAsyncTask().execute();
                    }
                });
            }else{
                Toast.makeText(this, "Please check you Internet connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public class ForgotPasswordAsyncTask extends AsyncTask<Void,Void,JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                return WebServiceClass.forgotpasswordService();
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
                    Toast.makeText(getApplicationContext(), "Check your Email for Password", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Failed to Retrive Password Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
