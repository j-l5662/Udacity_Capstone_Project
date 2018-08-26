package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.utils.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import timber.log.Timber;

public class AWSServiceListViewModel extends AndroidViewModel {

    private ArrayList<AWSService> mServiceList;
    private Application mApplication;

    public AWSServiceListViewModel(Application application) {

        super(application);
        mApplication = application;
        if (mServiceList == null) {
            loadServiceList();
        }
    }

    public ArrayList<AWSService> getAWSServiceList() {
        return mServiceList;
    }

    private void loadServiceList() {

        Context androidContext = mApplication.getApplicationContext();
        if(NetworkUtils.isAppOnline(androidContext)) {
            Timber.i("Online");
            RequestQueue queue = Volley.newRequestQueue(androidContext);
            String url = mApplication.getApplicationContext().getString(R.string.aws_doc_url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Document document = Jsoup.parse(response);
                            Elements link = document.select("div[id^=aws-nav-flyout-3-doc-]");

                            for(Element ele : link) {
                                String title = ele.select("h6").first().text();
                                Timber.i(title);
                                AWSService columnHeader = new AWSService(title,null);
                                columnHeader.setColumnHeader();
                                mServiceList.add(columnHeader);
                                Elements text = ele.getElementsByClass("aws-link");
                                for (Element t : text){
                                    mServiceList.add(new AWSService(t.text(),t.select("a").attr("abs:href")));
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
        else {
            Timber.i("Offline");
            mServiceList = NetworkUtils.makeAWSMainRequestOffline(androidContext);
        }
    }
}