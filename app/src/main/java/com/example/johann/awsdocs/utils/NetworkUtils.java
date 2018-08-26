package com.example.johann.awsdocs.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.ui.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import timber.log.Timber;

public class NetworkUtils {

    public static boolean isAppOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static Document retrieveHTMLfromFile(File file) {

        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            Timber.e(e.getMessage());
        }

        return doc;
    }


    public void makeAWSMainRequest(Context context) throws InterruptedException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.aws_doc_url);
//        final CountDownLatch cdl = new CountDownLatch(1);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<AWSService> awsServiceList = new ArrayList<>();
                        Document document = Jsoup.parse(response);
                        Elements link = document.select("div[id^=aws-nav-flyout-3-doc-]");

                        for(Element ele : link) {
                            String title = ele.select("h6").first().text();
                            AWSService columnHeader = new AWSService(title,null);
                            columnHeader.setColumnHeader();
                            awsServiceList.add(columnHeader);
                            Elements text = ele.getElementsByClass("aws-link");
                            for (Element t : text){
                                awsServiceList.add(new AWSService(t.text(),t.select("a").attr("abs:href")));
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO Add Timber
                Timber.i(error.toString());
            }
        });
        queue.add(stringRequest);
    }

    // FOR Debug Purposes TODO REMOVE WHEN RELEASE
    public static ArrayList<AWSService> makeAWSMainRequestOffline(Context context) {

        InputStream file;
        Document document = null;
        ArrayList<AWSService> awsServiceList = new ArrayList<>();

        try {
            file = context.getAssets().open("aws.html");
            document = Jsoup.parse(file,"UTF-8","Test");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Elements link = document.select("div[id^=aws-nav-flyout-3-doc-]");

        for(Element ele : link) {
            String title = ele.select("h6").first().text();
            AWSService columnHeader = new AWSService(title,null);
            columnHeader.setColumnHeader();
            awsServiceList.add(columnHeader);
            Elements text = ele.getElementsByClass("aws-link");
            for (Element t : text){
                awsServiceList.add(new AWSService(t.text(),t.select("a").attr("abs:href")));
            }
        }

        return awsServiceList;
    }

    public static ArrayList<String> makeAWS2HeaderRequestOffline(Context context) {

        InputStream file;
        Document document = null;
        ArrayList<String> headers = new ArrayList<>();


        try {
            file = context.getAssets().open("aws2.html");
            document = Jsoup.parse(file,"UTF-8","Test");
        }
        catch (IOException e){
            e.printStackTrace();
        }

        Elements titleTag = document.select("h3");
        if(titleTag.isEmpty()) {

        }
        else {
            for (Element e : titleTag) {
                String header = e.text();
                headers.add(header);
            }
        }
        return headers;
    }
}