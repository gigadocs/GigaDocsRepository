package com.datappsinfotech.gigadocs.utils.dtos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;

/**
 * Created by sandeep123 on 7/7/2016.
 */
public class PrescriptionDTO implements Parcelable {
    private Uri SendImage;


    protected PrescriptionDTO(Parcel in) {
        SendImage = Uri.parse(in.readString());
    }

    public static final Creator<PrescriptionDTO> CREATOR = new Creator<PrescriptionDTO>() {
        @Override
        public PrescriptionDTO createFromParcel(Parcel in) {
            return new PrescriptionDTO(in);
        }

        @Override
        public PrescriptionDTO[] newArray(int size) {
            return new PrescriptionDTO[size];
        }
    };

    public PrescriptionDTO() {
    }

    public Uri getSendImage() {
        return SendImage;
    }

    public void setSendImage(Uri sendImage) {
        SendImage = sendImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(SendImage));

    }
}
