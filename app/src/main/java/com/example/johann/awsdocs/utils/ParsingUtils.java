package com.example.johann.awsdocs.utils;

import java.util.List;

public class ParsingUtils {


    public static String appendURL(String url) {

        String awsBaseURL = "https://www.amazonaws.cn";
        String documentationURL = "/en/documentation/";
        String returnURL = url;

        if(url.contains(documentationURL)){

            returnURL = awsBaseURL + url;
        }

        return returnURL;
    }
}
