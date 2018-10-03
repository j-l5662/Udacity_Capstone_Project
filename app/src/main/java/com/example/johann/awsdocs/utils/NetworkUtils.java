package com.example.johann.awsdocs.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

public class NetworkUtils {

    public static boolean isAppOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static Document retrieveHTMLfromFile(File file) {

        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            Timber.e(e.getMessage());
        }
        return doc;
    }

    public static String appendRedirectURL(String html) {

        html = html.replaceAll("\\s+","");
        System.out.println(html);
        String appendedRedirectURL = "test";

        if(html.isEmpty()) {
            return appendedRedirectURL;
        }

        Pattern pattern = Pattern.compile("varmyDefaultPage=\"(.+.html)\"",Pattern.UNICODE_CASE);

        Matcher m = pattern.matcher(html);
        if(m.find()) {}
        appendedRedirectURL = m.group(1);


        return appendedRedirectURL;
    }

}
