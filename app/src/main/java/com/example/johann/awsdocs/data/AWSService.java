package com.example.johann.awsdocs.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "services")
public class AWSService implements Parcelable{



    @PrimaryKey(autoGenerate = true)
    private int serviceID;

    @ColumnInfo(name = "service_url")
    private String serviceURL;

    @ColumnInfo(name = "service_name")
    private String serviceName;

//    private ArrayList<String> pagesURL;
//    private String pageBody;

//    private ArrayList<AWSDocumentation> serviceDocumentation;
    @Ignore
    private boolean columnHeader = false;

    public AWSService(@NonNull String serviceName, String serviceURL) {

        this.serviceName = serviceName;
        this.serviceURL = serviceURL;
    }

    public AWSService(Parcel in) {

        this.serviceName = in.readString();
        this.serviceURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(serviceName);
        parcel.writeString(serviceURL);
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

    public void setColumnHeader() {
        this.columnHeader = true;
    }

    public boolean isColumnHeader() {
        return this.columnHeader;
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