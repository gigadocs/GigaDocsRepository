package com.datappsinfotech.gigadocs.utils.dtos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sandeep123 on 7/1/2016.
 */
public class RegistrationDetailsActivityDTO implements Parcelable {

    private  String address_RegistrationDetails;
    private  String iAttendPatientTime_RegistrationDetails;

    private  String iWorkFromTimeFetch_RegistrationDetails;
    private  String iWorkToTimeFetch_RegistrationDetails;
    private  String shiftAMPMFROM_RegistrationDetails;
    private  String shiftAMPMTO_RegistrationDetails;

    private  String iWorkFromShiftOneTimeFetch_RegistrationDetails;
    private  String iWorkFromShiftTwoTimeFetch_RegistrationDetails;
    private  String shift1AMPMFROM_RegistrationDetails;
    private  String shift1AMPMTO_RegistrationDetails;

    private  String iWorkToShiftOneTimeFetch_RegistrationDetails;
    private  String iWorkToShiftTwoTimeFetch_RegistrationDetails;
    private  String shift2AMPMFROM_RegistrationDetails;
    private  String shift2AMPMTO_RegistrationDetails;


    private  boolean isChecked_RegistrationDetails;


    protected RegistrationDetailsActivityDTO(Parcel in) {
        address_RegistrationDetails = in.readString();
        iAttendPatientTime_RegistrationDetails = in.readString();
        iWorkFromTimeFetch_RegistrationDetails = in.readString();
        iWorkToTimeFetch_RegistrationDetails = in.readString();
        shiftAMPMFROM_RegistrationDetails = in.readString();
        shiftAMPMTO_RegistrationDetails = in.readString();
        iWorkFromShiftOneTimeFetch_RegistrationDetails = in.readString();
        iWorkFromShiftTwoTimeFetch_RegistrationDetails = in.readString();
        shift1AMPMFROM_RegistrationDetails = in.readString();
        shift1AMPMTO_RegistrationDetails = in.readString();
        iWorkToShiftOneTimeFetch_RegistrationDetails = in.readString();
        iWorkToShiftTwoTimeFetch_RegistrationDetails = in.readString();
        shift2AMPMFROM_RegistrationDetails = in.readString();
        shift2AMPMTO_RegistrationDetails = in.readString();
        isChecked_RegistrationDetails = in.readByte() != 0;
    }

    public static final Creator<RegistrationDetailsActivityDTO> CREATOR = new Creator<RegistrationDetailsActivityDTO>() {
        @Override
        public RegistrationDetailsActivityDTO createFromParcel(Parcel in) {
            return new RegistrationDetailsActivityDTO(in);
        }

        @Override
        public RegistrationDetailsActivityDTO[] newArray(int size) {
            return new RegistrationDetailsActivityDTO[size];
        }
    };

    public RegistrationDetailsActivityDTO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address_RegistrationDetails);
        dest.writeString(iAttendPatientTime_RegistrationDetails);
        dest.writeString(iWorkFromTimeFetch_RegistrationDetails);
        dest.writeString(iWorkToTimeFetch_RegistrationDetails);
        dest.writeString(shiftAMPMFROM_RegistrationDetails);
        dest.writeString(shiftAMPMTO_RegistrationDetails);
        dest.writeString(iWorkFromShiftOneTimeFetch_RegistrationDetails);
        dest.writeString(iWorkFromShiftTwoTimeFetch_RegistrationDetails);
        dest.writeString(shift1AMPMFROM_RegistrationDetails);
        dest.writeString(shift1AMPMTO_RegistrationDetails);
        dest.writeString(iWorkToShiftOneTimeFetch_RegistrationDetails);
        dest.writeString(iWorkToShiftTwoTimeFetch_RegistrationDetails);
        dest.writeString(shift2AMPMFROM_RegistrationDetails);
        dest.writeString(shift2AMPMTO_RegistrationDetails);
        dest.writeByte((byte) (isChecked_RegistrationDetails ? 1 : 0));
    }


    public String getAddress_RegistrationDetails() {
        return address_RegistrationDetails;
    }

    public void setAddress_RegistrationDetails(String address_RegistrationDetails) {
        this.address_RegistrationDetails = address_RegistrationDetails;
    }

    public boolean isChecked_RegistrationDetails() {
        return isChecked_RegistrationDetails;
    }

    public boolean setChecked_RegistrationDetails(boolean checked_RegistrationDetails) {
        isChecked_RegistrationDetails = checked_RegistrationDetails;
        return checked_RegistrationDetails;
    }

    public String getShift2AMPMTO_RegistrationDetails() {
        return shift2AMPMTO_RegistrationDetails;
    }

    public void setShift2AMPMTO_RegistrationDetails(String shift2AMPMTO_RegistrationDetails) {
        this.shift2AMPMTO_RegistrationDetails = shift2AMPMTO_RegistrationDetails;
    }

    public String getiAttendPatientTime_RegistrationDetails() {
        return iAttendPatientTime_RegistrationDetails;
    }

    public void setiAttendPatientTime_RegistrationDetails(String iAttendPatientTime_RegistrationDetails) {
        this.iAttendPatientTime_RegistrationDetails = iAttendPatientTime_RegistrationDetails;
    }

    public String getiWorkFromTimeFetch_RegistrationDetails() {
        return iWorkFromTimeFetch_RegistrationDetails;
    }

    public void setiWorkFromTimeFetch_RegistrationDetails(String iWorkFromTimeFetch_RegistrationDetails) {
        this.iWorkFromTimeFetch_RegistrationDetails = iWorkFromTimeFetch_RegistrationDetails;
    }

    public String getShiftAMPMTO_RegistrationDetails() {
        return shiftAMPMTO_RegistrationDetails;
    }

    public void setShiftAMPMTO_RegistrationDetails(String shiftAMPMTO_RegistrationDetails) {
        this.shiftAMPMTO_RegistrationDetails = shiftAMPMTO_RegistrationDetails;
    }

    public String getShiftAMPMFROM_RegistrationDetails() {
        return shiftAMPMFROM_RegistrationDetails;
    }

    public void setShiftAMPMFROM_RegistrationDetails(String shiftAMPMFROM_RegistrationDetails) {
        this.shiftAMPMFROM_RegistrationDetails = shiftAMPMFROM_RegistrationDetails;
    }

    public String getiWorkToTimeFetch_RegistrationDetails() {
        return iWorkToTimeFetch_RegistrationDetails;
    }

    public void setiWorkToTimeFetch_RegistrationDetails(String iWorkToTimeFetch_RegistrationDetails) {
        this.iWorkToTimeFetch_RegistrationDetails = iWorkToTimeFetch_RegistrationDetails;
    }

    public String getiWorkFromShiftOneTimeFetch_RegistrationDetails() {
        return iWorkFromShiftOneTimeFetch_RegistrationDetails;
    }

    public void setiWorkFromShiftOneTimeFetch_RegistrationDetails(String iWorkFromShiftOneTimeFetch_RegistrationDetails) {
        this.iWorkFromShiftOneTimeFetch_RegistrationDetails = iWorkFromShiftOneTimeFetch_RegistrationDetails;
    }

    public String getiWorkFromShiftTwoTimeFetch_RegistrationDetails() {
        return iWorkFromShiftTwoTimeFetch_RegistrationDetails;
    }

    public void setiWorkFromShiftTwoTimeFetch_RegistrationDetails(String iWorkFromShiftTwoTimeFetch_RegistrationDetails) {
        this.iWorkFromShiftTwoTimeFetch_RegistrationDetails = iWorkFromShiftTwoTimeFetch_RegistrationDetails;
    }

    public String getShift1AMPMFROM_RegistrationDetails() {
        return shift1AMPMFROM_RegistrationDetails;
    }

    public void setShift1AMPMFROM_RegistrationDetails(String shift1AMPMFROM_RegistrationDetails) {
        this.shift1AMPMFROM_RegistrationDetails = shift1AMPMFROM_RegistrationDetails;
    }

    public String getShift1AMPMTO_RegistrationDetails() {
        return shift1AMPMTO_RegistrationDetails;
    }

    public void setShift1AMPMTO_RegistrationDetails(String shift1AMPMTO_RegistrationDetails) {
        this.shift1AMPMTO_RegistrationDetails = shift1AMPMTO_RegistrationDetails;
    }

    public String getiWorkToShiftOneTimeFetch_RegistrationDetails() {
        return iWorkToShiftOneTimeFetch_RegistrationDetails;
    }

    public void setiWorkToShiftOneTimeFetch_RegistrationDetails(String iWorkToShiftOneTimeFetch_RegistrationDetails) {
        this.iWorkToShiftOneTimeFetch_RegistrationDetails = iWorkToShiftOneTimeFetch_RegistrationDetails;
    }

    public String getiWorkToShiftTwoTimeFetch_RegistrationDetails() {
        return iWorkToShiftTwoTimeFetch_RegistrationDetails;
    }

    public void setiWorkToShiftTwoTimeFetch_RegistrationDetails(String iWorkToShiftTwoTimeFetch_RegistrationDetails) {
        this.iWorkToShiftTwoTimeFetch_RegistrationDetails = iWorkToShiftTwoTimeFetch_RegistrationDetails;
    }

    public String getShift2AMPMFROM_RegistrationDetails() {
        return shift2AMPMFROM_RegistrationDetails;
    }

    public void setShift2AMPMFROM_RegistrationDetails(String shift2AMPMFROM_RegistrationDetails) {
        this.shift2AMPMFROM_RegistrationDetails = shift2AMPMFROM_RegistrationDetails;
    }

}
