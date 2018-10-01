package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.app.Service;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.repository.ServiceRepository;
import com.example.johann.awsdocs.utils.NetworkUtils;
import com.example.johann.awsdocs.utils.ParsingUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

public class AWSServiceListViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<AWSService>> mServiceList = new MutableLiveData<>();
    private Application mApplication;
    private ServiceRepository mServiceRepository;

    public AWSServiceListViewModel(Application application) {

        super(application);
        mApplication = application;
        mServiceRepository = new ServiceRepository(mApplication);

        loadServiceList();
    }

    public LiveData<ArrayList<AWSService>> getAWSServiceList() {
        return mServiceList;
    }

    private void loadServiceList() {

        Context androidContext = mApplication.getApplicationContext();
        final ArrayList<AWSService> awsServiceArrayList = new ArrayList<>();
        if (NetworkUtils.isAppOnline(androidContext)) {
            Timber.i("Online");


            RequestQueue queue = Volley.newRequestQueue(androidContext);
            String url = mApplication.getApplicationContext().getString(R.string.aws_doc_url);

            Timber.i(url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            Document document = Jsoup.parse(response);

                            Elements titleSections = document.getElementsByClass("title-wrapper section");

                            Elements serviceTitles = document.getElementsByClass("aws-text-box section");


                            for (int i = 0; i < titleSections.size(); i++) {

                                String columnTitle = titleSections.get(i).getElementsByClass("twelve columns").select("h3").text();

                                AWSService columnHeader = new AWSService(columnTitle, null);
                                columnHeader.setColumnHeader(true);
                                awsServiceArrayList.add(columnHeader);

                                Elements service = serviceTitles.get(i + 1).getElementsByClass("  ").select("p");
                                for (Element t : service) {

                                    String documentationName = t.text();
                                    String documentationURL = t.select("a").attr("href");

                                    documentationURL = ParsingUtils.appendURL(documentationURL);

                                    awsServiceArrayList.add(new AWSService(documentationName, documentationURL));
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
        } else {
            Timber.i("Offline");
            ArrayList<AWSService> offlineAWSArrayList = new ArrayList<>();
            try{
               offlineAWSArrayList = mServiceRepository.getServices();
            }
            catch(InterruptedException e) {
                Timber.i("Interrupt Exception: " + e.getMessage());
                e.printStackTrace();
            }
            catch (ExecutionException e) {
                Timber.i("Execution Exception: " + e.getMessage());
                e.printStackTrace();
            }
            finally {
                if(offlineAWSArrayList.size() == 0) {
                    Timber.i("Error: Query Empty");
                }
                    mServiceList.postValue(offlineAWSArrayList);
            }
        }
    }
}
