package com.example.johann.awsdocs.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "documentations")
public class AWSDocumentation implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int docKey;

    @ColumnInfo(name = "documentation_name")
    private String documentationName;

    @ColumnInfo(name = "documentation_url")
    private String url;

    @ColumnInfo(name = "documentation_html")
    private String htmlText;

    @ColumnInfo(name = "column_header")
    private boolean columnHeader = false;

    @ColumnInfo(name = "awsService")
    private String awsService;

    public AWSDocumentation(String documentationName,
                            String url) {

        this.documentationName = documentationName;
        this.url = url;
    }

    private AWSDocumentation(Parcel in) {

        this.documentationName = in.readString();
        this.url = in.readString();
        this.docKey = in.readInt();
        this.htmlText = in.readString();
        this.awsService = in.readString();
        this.columnHeader = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.documentationName);
        parcel.writeString(this.url);
        parcel.writeInt(this.docKey);
        parcel.writeString(htmlText);
        parcel.writeString(awsService);
        parcel.writeByte((byte) (columnHeader ? 1 : 0));
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

    public void setDocumentationName(String documentationName) {
        this.documentationName = documentationName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url =  url;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public boolean isColumnHeader() {
        return columnHeader;
    }

    public void setColumnHeader(boolean columnHeader) {
        this.columnHeader = columnHeader;
    }

    public int getDocKey() { return docKey; }

    public void setDocKey(int docKey) { this.docKey = docKey; }

    public String getAwsService() { return awsService; }

    public void setAwsService(String awsService) {
        this.awsService = awsService;
    }
}
