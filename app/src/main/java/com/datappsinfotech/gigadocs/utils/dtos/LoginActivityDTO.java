package com.datappsinfotech.gigadocs.utils.dtos;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class LoginActivityDTO implements Parcelable {

    private String email_LoginActivity;
    private String password_LoginActivity;
    private String forgotPassword_LoginActivity;
    private String remember_LoginActivity;
    private String user_ID;


    public String getUser_ID() {
        Log.i("Dto UserId", user_ID);

        return user_ID;
    }

    public  void setUser_ID(String user_ID) {
        Log.i("Dto UserId", user_ID);
        this.user_ID = user_ID;
    }




    private  String token_Login_Activity;
    private  String slotTimings_LoginActivity;

    protected LoginActivityDTO(Parcel in) {
        email_LoginActivity = in.readString();
        password_LoginActivity = in.readString();
        forgotPassword_LoginActivity = in.readString();
        remember_LoginActivity = in.readString();
        token_Login_Activity =in.readString();
        slotTimings_LoginActivity=in.readString();
        user_ID=in.readString();
    }

    public static final Creator<LoginActivityDTO> CREATOR = new Creator<LoginActivityDTO>() {
        @Override
        public LoginActivityDTO createFromParcel(Parcel in) {
            return new LoginActivityDTO(in);
        }

        @Override
        public LoginActivityDTO[] newArray(int size) {
            return new LoginActivityDTO[size];
        }
    };

    public LoginActivityDTO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email_LoginActivity);
        dest.writeString(password_LoginActivity);
        dest.writeString(forgotPassword_LoginActivity);
        dest.writeString(remember_LoginActivity);
        dest.writeString(slotTimings_LoginActivity);
        dest.writeString(token_Login_Activity);
        dest.writeString(user_ID);

    }

    public String getEmail_LoginActivity() {
        return email_LoginActivity;
    }

    public void setEmail_LoginActivity(String email_LoginActivity) {
        this.email_LoginActivity = email_LoginActivity;
    }

    public String getPassword_LoginActivity() {
        return password_LoginActivity;
    }

    public void setPassword_LoginActivity(String password_LoginActivity) {
        this.password_LoginActivity = password_LoginActivity;
    }

    public String getForgotPassword_LoginActivity() {
        return forgotPassword_LoginActivity;
    }

    public void setForgotPassword_LoginActivity(String forgotPassword_LoginActivity) {
        this.forgotPassword_LoginActivity = forgotPassword_LoginActivity;
    }

    public String getRemember_LoginActivity() {
        return remember_LoginActivity;
    }

    public void setRemember_LoginActivity(String remember_LoginActivity) {
        this.remember_LoginActivity = remember_LoginActivity;
    }
    public String getToken_Login_Activity() {
        return token_Login_Activity;
    }

    public void setToken_Login_Activity(String token_Login_Activity) {
        this.token_Login_Activity = token_Login_Activity;
    }

    public String getSlotTimings_LoginActivity() {
        return slotTimings_LoginActivity;
    }

    public void setSlotTimings_LoginActivity(String slotTimings_LoginActivity) {
        this.slotTimings_LoginActivity = slotTimings_LoginActivity;
    }
}
