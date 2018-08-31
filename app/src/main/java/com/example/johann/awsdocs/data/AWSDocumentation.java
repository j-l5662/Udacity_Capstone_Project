package com.example.johann.awsdocs.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AWSDocumentation implements Parcelable{

    //TODO If the url does not have userguide /or guide in it, open up a webpage
    private String documentationName;
    private String url;
    private String text;

    private boolean columnHeader = false;

    public AWSDocumentation(String documentationName,
                            String url) {

        this.documentationName = documentationName;
        this.url = url;
    }

    public AWSDocumentation(Parcel in) {

        this.documentationName = in.readString();
        this.url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.documentationName);
        parcel.writeString(this.url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AWSDocumentation> CREATOR = new Parcelable.Creator<AWSDocumentation>() {
        @Override
        public AWSDocumentation createFromParcel(Parcel parcel) { return new AWSDocumentation(parcel); }

        @Override
        public AWSDocumentation[] newArray(int size) { return new AWSDocumentation[size]; }
    };

    public String getDocumentationName() {
        return documentationName;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public boolean isColumnHeader() {
        return columnHeader;
    }

    public void setasColumnHeader() {
        this.columnHeader = true;
    }
}
