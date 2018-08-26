package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.renderscript.ScriptGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.utils.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import timber.log.Timber;

public class AWSDocumentationListViewModel extends AndroidViewModel {

    private ArrayList<AWSDocumentation> mAWSDocumentations;
    private AWSService mAWSservice;
    private Application mApplication;

    public AWSDocumentationListViewModel(Application mApplication, AWSService mAWSservice) {
        super(mApplication);
        this.mApplication = mApplication;
        this.mAWSservice = mAWSservice;
    }

    private void loadDocumentationList() {

        Context androidContext = mApplication.getApplicationContext();

        if(NetworkUtils.isAppOnline(androidContext)) {
            Timber.i("Online");
            RequestQueue queue = Volley.newRequestQueue(androidContext);
            String url = mAWSservice.returnURL().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Document document = Jsoup.parse(response);
                            Elements titleTag = document.select("h3");
                            String header;
                            for (Element e : titleTag) {
                                header = e.text();
                                if(header.isEmpty()) {
                                    header = mAWSservice.returnName();
                                }
                            }

//                            for(Element ele : link) {
//                                String title = ele.select("h6").first().text();
//                                AWSService columnHeader = new AWSService(title,null);
//                                columnHeader.setColumnHeader();
//                                mServiceList.add(columnHeader);
//                                Elements text = ele.getElementsByClass("aws-link");
//                                for (Element t : text){
//                                    mServiceList.add(new AWSService(t.text(),t.select("a").attr("abs:href")));
//                                }
//                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Timber.i(error.toString());
                }
            });
            queue.add(stringRequest);
        }
        else {
            InputStream file;
            Document document = null;
            Timber.i("Offline");
            try {
                file = mApplication.getApplicationContext().getAssets().open("aws2.html");
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
                    //TODO Make AWS Documentation
//                    headers.add(header);
                }
            }
        }
    }
}
