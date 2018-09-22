package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.utils.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import timber.log.Timber;

public class AWSDetailPageViewModel extends AndroidViewModel {

    private Application mApplication;
    private AWSDocumentation mAWSDocumentation;
    private MutableLiveData<String> mAWSHTMLPage = new MutableLiveData<>();

    public AWSDetailPageViewModel(Application application, AWSDocumentation awsDocumentation) {

        super(application);
        this.mApplication = application;
        this.mAWSDocumentation = awsDocumentation;

        loadHTML();

    }
    public LiveData<String> returnAWSPageHTML() {
        return mAWSHTMLPage;
    }
    private void loadHTML() {

        Context androidContext = mApplication.getApplicationContext();

        if(NetworkUtils.isAppOnline(androidContext)) {
            Timber.i("Online");
            new AsyncTask<Void,Void,Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    URL resourceUrl, base, next;
                    HttpURLConnection conn;
                    String location;
                    String url = mAWSDocumentation.getUrl();

                    Timber.i(url);

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
                        String newURL = url;

                        Timber.i(url);

                        if(!url.contains(".html")) {
                            String documentationHTML = NetworkUtils.appendRedirectURL(doc.html());
                            newURL = url+documentationHTML;
                        }

                        Document newDoc = Jsoup.connect(newURL).get();

                        Element urlData = newDoc.getElementById("main-col-body");

                        if(urlData.getElementById("divRegionDisclaimer") != null ||  urlData.select("table") != null) {
                            urlData.getElementById("divRegionDisclaimer").remove();
                            urlData.select("table").get(0).remove();
                        }
//                        Timber.i(urlData);
                        mAWSHTMLPage.postValue(urlData.html());
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
