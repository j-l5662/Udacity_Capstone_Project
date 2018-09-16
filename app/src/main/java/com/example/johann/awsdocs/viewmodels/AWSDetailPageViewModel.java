package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.utils.NetworkUtils;

import org.jetbrains.annotations.Async;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;

import timber.log.Timber;

public class AWSDetailPageViewModel extends AndroidViewModel {

    private Application mApplicaiton;
    private AWSDocumentation mAWSDocumentation;
    private MutableLiveData<String> mAWSHTMLPage = new MutableLiveData<>();

    public AWSDetailPageViewModel(Application application, AWSDocumentation awsDocumentation) {

        super(application);
        this.mApplicaiton = application;
        this.mAWSDocumentation = awsDocumentation;

        loadHTML();

    }
    public LiveData<String> returnAWSPageHTML() {
        return mAWSHTMLPage;
    }
    private void loadHTML() {

        Context androidContext = mApplicaiton.getApplicationContext();


        if(NetworkUtils.isAppOnline(androidContext)) {
            Timber.i("Online");
            new AsyncTask<Void,Void,Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    URL resourceUrl, base, next;
                    HttpURLConnection conn;
                    String location;
                    String url = mAWSDocumentation.getUrl();

                    while (true)
                    {
                        try{

                            resourceUrl = new URL(url);
                            conn        = (HttpURLConnection) resourceUrl.openConnection();

                            conn.setConnectTimeout(15000);
                            conn.setReadTimeout(15000);
                            conn.setInstanceFollowRedirects(false);   // Make the logic below easier to detect redirections
                            conn.setRequestProperty("User-Agent", "Mozilla/5.0...");

                            switch (conn.getResponseCode())
                            {
                                case HttpURLConnection.HTTP_MOVED_PERM:
                                case HttpURLConnection.HTTP_MOVED_TEMP:
                                    location = conn.getHeaderField("Location");
                                    location = URLDecoder.decode(location, "UTF-8");
                                    base     = new URL(url);
                                    next     = new URL(base, location);  // Deal with relative URLs
                                    url      = next.toExternalForm();
                                    continue;
                            }

                            break;
                        }
                        catch (Exception e){

                        }

                    }

                    try {

                        Document doc = Jsoup.connect(url).get();

                        String documentationHTML = NetworkUtils.appendRedirectURL(doc.html());

//                        String metaURL = doc.select("meta").get(3).attr("abs:content").toString();

                        String newURL = url+documentationHTML;

                        Document newDoc = Jsoup.connect(newURL).get();

                        String urlData = newDoc.getElementById("main-col-body").html();

//                        Timber.i(urlData);
                        mAWSHTMLPage.postValue(urlData);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

        }
        else {
            Timber.i("Offline");
            mAWSHTMLPage.setValue(NetworkUtils.makeDocumentationRequestOffline(androidContext));
        }
    }

}
