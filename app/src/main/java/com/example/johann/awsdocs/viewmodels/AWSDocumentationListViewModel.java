package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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

    private MutableLiveData<ArrayList<AWSDocumentation>> mAWSDocumentations = new MutableLiveData<>();
    private AWSService mAWSService;
    private Application mApplication;

    public AWSDocumentationListViewModel(Application mApplication, AWSService awsService) {
        super(mApplication);
        this.mApplication = mApplication;
        this.mAWSService = awsService;

        loadDocumentationList();
    }

    public LiveData<ArrayList<AWSDocumentation>> returnAWSDocumentationList() {
        return mAWSDocumentations;
    }
    private void loadDocumentationList() {

        Context androidContext = mApplication.getApplicationContext();

        if(NetworkUtils.isAppOnline(androidContext)) {
            Timber.i("Online");
            RequestQueue queue = Volley.newRequestQueue(androidContext);
            String url = mAWSService.returnURL().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<AWSDocumentation> awsDocumentations = new ArrayList<>();
                            Document document = Jsoup.parse(response);
                            Elements titleSections = document.getElementsByClass("title-wrapper section");
                            Elements tableSections = document.getElementsByClass("table-wrapper section");
                            for (int i = 0; i < titleSections.size(); i++) {
                                Element header = titleSections.get(i).select("h3").get(0);

                                String header_title = (header.text().isEmpty()) ? mAWSService.returnName() : header.text();

                                AWSDocumentation awsDocumentation = new AWSDocumentation(header_title,"");
                                awsDocumentation.setasColumnHeader();
                                awsDocumentations.add(awsDocumentation);


                                Element tableSection = tableSections.get(i);
                                Elements tables = tableSection.select("table");
                                for (Element table : tables) {
                                    Elements rows = table.select("tr");

                                    for (int j = 0; j < rows.size(); j++) {
                                        Element row = rows.get(j);
                                        Elements column = row.select("td");

                                        Elements links = column.select("a");

                                        for (Element link : links) {
                                            String linkContent = link.attr("abs:href");
                                            String linkContentText = link.text();
                                            if (linkContentText.equals("HTML") || linkContentText.equals("PDF") || linkContentText.equals("Kindle") || linkContentText.isEmpty()) {
                                            } else {
                                                awsDocumentations.add(new AWSDocumentation(linkContentText, linkContent));
                                            }
                                        }
                                    }
                                }
                            }
                            mAWSDocumentations.setValue(awsDocumentations);
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
            Timber.i("Offline");
            mAWSDocumentations.setValue(NetworkUtils.makeListRequestOffline(androidContext,mAWSService));
        }
    }
}
