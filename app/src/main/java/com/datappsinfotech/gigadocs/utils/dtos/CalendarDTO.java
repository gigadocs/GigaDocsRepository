package com.datappsinfotech.gigadocs.utils.dtos;

import android.os.Parcel;
import android.os.Parcelable;


public class CalendarDTO implements Parcelable{

    private String setCalendar;
    private String setPatientName;
    private boolean isPrescribed;
    private int position;

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPatientAppointmentID() {
        return patientAppointmentID;
    }

    public void setPatientAppointmentID(String patientAppointmentID) {
        this.patientAppointmentID = patientAppointmentID;
    }

    private String patientAppointmentID;
    private String patientAddress;
    private String patientEmail;


    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    private boolean isSaved;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;


    protected CalendarDTO(Parcel in) {
        setCalendar = in.readString();
        setPatientName = in.readString();
        mobile=in.readString();
        isPrescribed = in.readByte()==1;
        isSaved=in.readByte()==1;
        position = in.readInt();
    }

    public static final Creator<CalendarDTO> CREATOR = new Creator<CalendarDTO>() {
        @Override
        public CalendarDTO createFromParcel(Parcel in) {
            return new CalendarDTO(in);
        }

        @Override
        public CalendarDTO[] newArray(int size) {
            return new CalendarDTO[size];
        }
    };

    public CalendarDTO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(setCalendar);
        dest.writeString(setPatientName);
        dest.writeString(mobile);
        dest.writeByte((byte)(isPrescribed?1:0));
        dest.writeByte((byte)(isPrescribed?1:0));
        dest.writeInt(position);
    }

    public String getSetCalendar() {
        return setCalendar;
    }

    public void setSetCalendar(String setCalendar) {
        this.setCalendar = setCalendar;
    }

    public String getSetPatientName() {
        return setPatientName;
    }

    public void setSetPatientName(String setPatientName) {
        this.setPatientName = setPatientName;
    }

    public boolean isPrescribed() {
        return isPrescribed;
    }

    public void setPrescribed(boolean prescribed) {
        isPrescribed = prescribed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
