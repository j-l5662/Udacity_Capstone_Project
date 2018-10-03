package com.example.johann.awsdocs.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.repository.DocRepository;
import com.example.johann.awsdocs.utils.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

public class AWSDocumentationPageViewModel extends AndroidViewModel {

    private Application mApplication;
    private AWSDocumentation mAWSDocumentation;
    private MutableLiveData<String> mAWSHTMLPage = new MutableLiveData<>();

    private DocRepository mDocRepository;

    public AWSDocumentationPageViewModel(Application application, AWSDocumentation awsDocumentation) {

        super(application);
        this.mApplication = application;
        this.mAWSDocumentation = awsDocumentation;

        mDocRepository = new DocRepository(mApplication);

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

                    while (true)
                    {
                        try{

                            resourceUrl = new URL(url);
                            conn = (HttpURLConnection) resourceUrl.openConnection();

                            conn.setConnectTimeout(1000);
                            conn.setReadTimeout(1000);
                            conn.setInstanceFollowRedirects(false);

                            switch (conn.getResponseCode())
                            {
                                case HttpURLConnection.HTTP_MOVED_TEMP:
                                    location = conn.getHeaderField("Location");
                                    location = URLDecoder.decode(location, "UTF-8");
                                    base = new URL(url);
                                    next = new URL(base, location);
                                    url = next.toExternalForm();
                                    continue;
                            }
                            break;
                        }
                        catch (MalformedURLException e) {
                            Timber.e(e.getMessage());
                        }
                        catch (UnsupportedEncodingException e) {
                            Timber.e(e.getMessage());
                        }
                        catch (IOException e) {
                            Timber.e(e.getMessage());
                        }
                    }

                    try {

                        Document doc = Jsoup.connect(url).get();
                        String newURL = url;

                        if(!url.contains(".html")) {
                            String documentationHTML = NetworkUtils.appendRedirectURL(doc.html());
                            newURL = url+documentationHTML;
                        }

                        Document newDoc = Jsoup.connect(newURL).get();

                        String urlData = newDoc.html();
                        mAWSDocumentation.setHtmlText(urlData);
                        Timber.i("SERVICE IS" +mAWSDocumentation.getAwsService() );

                        mDocRepository.insertDoc(mAWSDocumentation);

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

            AWSDocumentation awsDocumentation = null;
            String queriedDocHTML = "";

            mDocRepository = new DocRepository(mApplication);

            try{
                awsDocumentation = mDocRepository.getDocumentation(mAWSDocumentation.getDocumentationName());
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
                if(awsDocumentation == null) {
                    Timber.i("Error: Query String Empty");
                }
                else{
                    queriedDocHTML = awsDocumentation.getHtmlText();
                }
                mAWSHTMLPage.postValue(queriedDocHTML);
            }
        }
    }
}
