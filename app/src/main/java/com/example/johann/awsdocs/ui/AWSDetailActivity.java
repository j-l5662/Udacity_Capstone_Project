package com.example.johann.awsdocs.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
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
            Timber.i(mAwsService.returnName());
        }
        setTitle(mAwsService.returnName());

        mHeaders = NetworkUtils.makeAWS2HeaderRequestOffline(this);
        makeRequest(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.aws_activity_detail_list_view,R.id.text_view,mHeaders);
        mListView.setAdapter(adapter);


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

        Element table = document.select("table").first();
        String title = table.text();
        Timber.i(title);
        return title;
    }
}