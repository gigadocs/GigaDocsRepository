package com.datappsinfotech.gigadocs.utils.dtos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sandeep123 on 7/1/2016.
 */
public class RegistrationActivityDTO implements Parcelable{
    private String name_RegistrationActivity;
    private String email_RegistrationActivity;
    private String password_RegistrationActivity;
    private String mobile_RegistrationActivity;
    private String speciality_RegistrationActivity;

    public RegistrationActivityDTO() {
    }


    public String getName_RegistrationActivity() {
        return name_RegistrationActivity;
    }

    public void setName_RegistrationActivity(String name_RegistrationActivity) {
        this.name_RegistrationActivity = name_RegistrationActivity;
    }

    public String getSpeciality_RegistrationActivity() {
        return speciality_RegistrationActivity;
    }

    public void setSpeciality_RegistrationActivity(String speciality_RegistrationActivity) {
        this.speciality_RegistrationActivity = speciality_RegistrationActivity;
    }

    public String getMobile_RegistrationActivity() {
        return mobile_RegistrationActivity;
    }

    public void setMobile_RegistrationActivity(String mobile_RegistrationActivity) {
        this.mobile_RegistrationActivity = mobile_RegistrationActivity;
    }

    public String getPassword_RegistrationActivity() {
        return password_RegistrationActivity;
    }

    public void setPassword_RegistrationActivity(String password_RegistrationActivity) {
        this.password_RegistrationActivity = password_RegistrationActivity;
    }

    public String getEmail_RegistrationActivity() {
        return email_RegistrationActivity;
    }

    public void setEmail_RegistrationActivity(String email_RegistrationActivity) {
        this.email_RegistrationActivity = email_RegistrationActivity;
    }




    protected RegistrationActivityDTO(Parcel in) {
        name_RegistrationActivity = in.readString();
        email_RegistrationActivity = in.readString();
        password_RegistrationActivity = in.readString();
        mobile_RegistrationActivity = in.readString();
        speciality_RegistrationActivity = in.readString();
    }

    public static final Creator<RegistrationActivityDTO> CREATOR = new Creator<RegistrationActivityDTO>() {
        @Override
        public RegistrationActivityDTO createFromParcel(Parcel in) {
            return new RegistrationActivityDTO(in);
        }

        @Override
        public RegistrationActivityDTO[] newArray(int size) {
            return new RegistrationActivityDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name_RegistrationActivity);
        dest.writeString(email_RegistrationActivity);
        dest.writeString(password_RegistrationActivity);
        dest.writeString(mobile_RegistrationActivity);
        dest.writeString(speciality_RegistrationActivity);
    }
}
