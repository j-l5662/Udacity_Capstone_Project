package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.utils.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
            String url = mAWSService.getServiceURL();
            Timber.i(url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<AWSDocumentation> awsDocumentations = new ArrayList<>();
                            Document document = Jsoup.parse(response);

                            Elements titleSections = document.getElementsByClass("title-wrapper section");
                            Elements tableSections = document.getElementsByClass("aws-text-box section");

                            int tableIterator = (titleSections.size() == 0 ) ? 1 : titleSections.size();

                            Timber.i(String.valueOf(tableIterator));

                            for (int i = 0; i < tableIterator; i++) {
                                Elements header;
                                String header_title;
                                if(titleSections.size() != 0) {
                                    header = titleSections.get(i).getElementsByClass("twelve columns").select("h3");

                                    header_title = header.text();
//                                    Timber.i(header_title);
                                }

                                else {
                                    header_title = mAWSService.getServiceName();
                                }

                                Element tableSection = tableSections.get(i);
                                Elements tables = tableSection.getElementsByClass("  ").select("p");


                                //TODO Get Link Here
                                Element htmlPTag = tables.get(1);

                                String linkContent = htmlPTag.select("a").attr("href");

                                AWSDocumentation awsDocumentation = new AWSDocumentation(header_title,linkContent);
                                awsDocumentations.add(awsDocumentation);

                            }
                                mAWSDocumentations.setValue(awsDocumentations);
                            }
                        }
                    , new Response.ErrorListener() {

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
