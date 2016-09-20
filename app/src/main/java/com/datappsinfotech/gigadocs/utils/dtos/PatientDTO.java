package com.datappsinfotech.gigadocs.utils.dtos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DATapps on 6/27/2016.
 */
public class PatientDTO implements Parcelable{

    private  String mobileNo;
    private  String name;
    private  String address;
    private  String email;
    private  String patientId;
    private  String get_PatientListId;
    private String server_id;
    private String getPid;

    public String getGetPid() {
        return getPid;
    }

    public void setGetPid(String getPid) {
        this.getPid = getPid;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    protected PatientDTO(Parcel in) {
        mobileNo = in.readString();
        name = in.readString();
        address = in.readString();
        email = in.readString();
        patientId = in.readString();
        get_PatientListId =in.readString();
    }

    public static final Creator<PatientDTO> CREATOR = new Creator<PatientDTO>() {
        @Override
        public PatientDTO createFromParcel(Parcel in) {
            return new PatientDTO(in);
        }

        @Override
        public PatientDTO[] newArray(int size) {
            return new PatientDTO[size];
        }
    };

    public PatientDTO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobileNo);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(patientId);
        dest.writeString(get_PatientListId);
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getGet_PatientListId() {
        return get_PatientListId;
    }

    public void setGet_PatientListId(String get_PatientListId) {
        this.get_PatientListId = get_PatientListId;
    }
}
