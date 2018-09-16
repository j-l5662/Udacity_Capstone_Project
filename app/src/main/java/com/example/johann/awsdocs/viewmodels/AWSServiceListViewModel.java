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

    private final String awsURL = "https://aws.amazon.com/";

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

            Timber.i(url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Document document = Jsoup.parse(response);

                            Elements linkColumn = document.getElementsByClass("lb-col lb-tiny-24 lb-mid-21");

                            Elements columnTitles = document.getElementsByClass("lb-tiny-v-margin lb-rtxt");
                            for(int i = 0; i < columnTitles.size();i++) {

                               String columnTitle = columnTitles.get(i).text();

                                AWSService columnHeader = new AWSService(columnTitle,null);
                                columnHeader.setColumnHeader();
                                awsServiceArrayList.add(columnHeader);

                                Elements links = linkColumn.get(i).getElementsByClass("lb-txt-none lb-txt");
                                for (Element t : links){

                                    String documnetationName = t.html();
                                    String documentationURL = t.attr("href");

                                    documentationURL = awsURL + documentationURL;
                                    awsServiceArrayList.add(new AWSService(documnetationName,documentationURL));
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
