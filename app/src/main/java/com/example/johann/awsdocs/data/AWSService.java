package com.example.johann.awsdocs.data;

import android.net.Uri;
import android.webkit.WebView;

import java.io.Serializable;
import java.util.ArrayList;

public class AWSService implements Serializable{

    private String serviceURL;
    private String serviceName;
    private ArrayList<String> pagesURL;
    private String pageBody;
    private WebView webView;

    public AWSService(String serviceName,String serviceURL) {
        this.serviceName = serviceName;
        this.serviceURL = serviceURL;
    }

    public Uri returnURL() {
        return Uri.parse(serviceURL);
    }

    public String returnName() {
        return serviceName;
    }
}
