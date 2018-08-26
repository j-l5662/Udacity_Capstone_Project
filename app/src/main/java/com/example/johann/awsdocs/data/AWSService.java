package com.example.johann.awsdocs.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebView;

import java.io.Serializable;
import java.util.ArrayList;

public class AWSService implements Parcelable{

    private String serviceURL;
    private String serviceName;
    private ArrayList<String> pagesURL;
    private String pageBody;
    private WebView webView;

    private ArrayList<AWSDocumentation> serviceDocumentation;
    private boolean columnHeader = false;

    public AWSService(String serviceName,String serviceURL) {

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
        parcel.writeString(serviceName);
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

    public Uri returnURL() {
        return Uri.parse(serviceURL);
    }

    public String returnName() {
        return serviceName;
    }

    public void setColumnHeader() {
        this.columnHeader = true;
    }

    public boolean isColumnHeader() {
        return this.columnHeader;
    }
}