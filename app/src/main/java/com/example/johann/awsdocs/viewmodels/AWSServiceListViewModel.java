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

    private MutableLiveData<ArrayList<AWSService>> mServiceList = new MutableLiveData<>();
    private Application mApplication;

    public AWSServiceListViewModel(Application application) {

        super(application);
        mApplication = application;

        loadServiceList();
    }

    public LiveData<ArrayList<AWSService>> getAWSServiceList() {
        return mServiceList;
    }

    private void loadServiceList() {

        Context androidContext = mApplication.getApplicationContext();
        final ArrayList<AWSService> awsServiceArrayList = new ArrayList<>();
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
                                awsServiceArrayList.add(columnHeader);
                                Elements text = ele.getElementsByClass("aws-link");
                                for (Element t : text){
                                    awsServiceArrayList.add(new AWSService(t.text(),t.select("a").attr("abs:href")));
                                }
                            }

                            mServiceList.postValue(awsServiceArrayList);
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
            //TODO Room Request
            Timber.i("Offline");
            mServiceList.setValue(NetworkUtils.makeAWSMainRequestOffline(androidContext));
        }
    }
}
