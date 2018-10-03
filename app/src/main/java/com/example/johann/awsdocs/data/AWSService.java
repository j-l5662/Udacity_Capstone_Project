package com.example.johann.awsdocs.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "services")
public class AWSService implements Parcelable{



    @PrimaryKey(autoGenerate = true)
    private int serviceID;

    @ColumnInfo(name = "service_url")
    private String serviceURL;

    @ColumnInfo(name = "service_name")
    private String serviceName;

    @ColumnInfo(name = "column_header")
    private boolean columnHeader = false;

    public AWSService(@NonNull String serviceName, String serviceURL) {

        this.serviceName = serviceName;
        this.serviceURL = serviceURL;
    }

    private AWSService(Parcel in) {

        this.serviceName = in.readString();
        this.serviceURL = in.readString();
        this.serviceID = in.readInt();
        this.columnHeader = in.readByte() !=0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(serviceName);
        parcel.writeString(serviceURL);
        parcel.writeInt(serviceID);
        parcel.writeByte((byte) (columnHeader ? 1 : 0));
    }

    @Override
    public int describeContents() { return 0; }

    public static final Parcelable.Creator<AWSService> CREATOR = new Parcelable.Creator<AWSService>() {

        @Override
        public AWSService createFromParcel(Parcel source) {
            return new AWSService(source);
        }

        @Override
        public AWSService[] newArray(int size) {
            return new AWSService[size];
        }
    };

    public boolean isColumnHeader() {
        return this.columnHeader;
    }

    public void setColumnHeader(boolean columnHeader) {
        this.columnHeader = columnHeader;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}