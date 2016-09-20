package com.datappsinfotech.gigadocs.utils.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sandeep123 on 7/1/2016.
 */
public class AppointmentDTO implements Parcelable {

    private  String setDate;
    private  String saveSlots;
    private String patientName;
    public String patientId;
    private String appointmentListId;
    private String appointment_UniqeID;
    private boolean isAppointmentAdded;
    private int position;
    private String apo_id;
    private String server_id;
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getApo_id() {
        return apo_id;
    }

    public void setApo_id(String apo_id) {
        this.apo_id = apo_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPatientId() {
        return patientId;
    }

    public boolean isAppointmentAdded() {
        return isAppointmentAdded;
    }

    public void setAppointmentAdded(boolean appointmentAdded) {
        isAppointmentAdded = appointmentAdded;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    protected AppointmentDTO(Parcel in) {
        setDate = in.readString();
        saveSlots = in.readString();
        patientName=in.readString();
        isAppointmentAdded=in.readByte()==1;
        appointmentListId=in.readString();
        appointment_UniqeID=in.readString();
        position = in.readInt();

    }

    public static final Creator<AppointmentDTO> CREATOR = new Creator<AppointmentDTO>() {
        @Override
        public AppointmentDTO createFromParcel(Parcel in) {
            return new AppointmentDTO(in);
        }

        @Override
        public AppointmentDTO[] newArray(int size) {
            return new AppointmentDTO[size];
        }
    };

    public AppointmentDTO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(setDate);
        dest.writeString(saveSlots);
        dest.writeString(appointmentListId);
        dest.writeString(patientName);
        dest.writeString(appointment_UniqeID);
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public String getSaveSlots() {
        return saveSlots;
    }

    public void setSaveSlots(String saveSlots) {
        this.saveSlots = saveSlots;
    }
    public String getAppointmentListId() {
        return appointmentListId;
    }

    public void setAppointmentListId(String appointmentListId) {
        this.appointmentListId = appointmentListId;
    }
    public String getAppointment_UniqeID() {
        return appointment_UniqeID;
    }

    public void setAppointment_UniqeID(String appointment_UniqeID) {
        this.appointment_UniqeID = appointment_UniqeID;
    }
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

}
