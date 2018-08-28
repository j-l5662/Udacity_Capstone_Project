package com.example.johann.awsdocs.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.utils.NetworkUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AWSDetailActivity extends AppCompatActivity{

    private AWSService mAwsService;

//    @BindView(R.id.aws_detail_url_text_view)
//    TextView mTextView;

    @BindView(R.id.aws_service_list)
    ListView mListView;

    private ArrayList<String> mHeaders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aws_activity_detail);

        ButterKnife.bind(this);
        if(getIntent().hasExtra(getString(R.string.main_activity_extra))) {
            mAwsService = getIntent().getParcelableExtra(getString(R.string.main_activity_extra));
        }
        setTitle(mAwsService.returnName());

//        mHeaders = NetworkUtils.makeAWS2HeaderRequestOffline(this);
        makeRequest(this);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.aws_activity_detail_list_view,R.id.text_view,mHeaders);
//        mListView.setAdapter(adapter);


    }

    public String makeRequest(Context context) {

        InputStream file;
        Document document = null;


        try {
            file = context.getAssets().open("aws2.html");
            document = Jsoup.parse(file,"UTF-8","Test");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        ArrayList<AWSDocumentation> awsDocumentations = new ArrayList<>();
        Elements titleSections = document.getElementsByClass("title-wrapper section");
        Elements tableSections = document.getElementsByClass("table-wrapper section");
        for ( int i = 0; i < titleSections.size(); i++) {
            Element header = titleSections.get(i).select("h3").get(0);
            String header_title = header.text();

            Element tableSection = tableSections.get(i);
            Elements tables = tableSection.select("table");
            for (Element table : tables) {
                Elements rows = table.select("tr");

                for (int j = 0; j < rows.size(); j++) {
                    Element row = rows.get(j);
                    Elements column = row.select("td");

                    Elements links = column.select("a");

                    for (Element link : links) {
                        String linkContent = link.attr("abs:href");
                        String linkContentText = link.text();
                        if(linkContentText.equals("HTML") || linkContentText.equals("PDF") || linkContentText.equals("Kindle")) {}
                        else{
                            awsDocumentations.add(new AWSDocumentation(header_title,linkContentText,linkContent));
                            //TODO add to viewmodel 
                        }

                    }
                }
            }
        }
        return "";
    }
}
