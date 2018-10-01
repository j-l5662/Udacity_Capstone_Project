package com.example.johann.awsdocs.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.factory.AWSDetailPageViewModelFactory;
import com.example.johann.awsdocs.viewmodels.AWSDocumentationPageViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AWSDocumentationActivity extends AppCompatActivity {

    @BindView(R.id.documentation_header)
    WebView mWebView;

    @BindView(R.id.detail_aws_list)
    ListView mOverFlowList;

    @BindView(R.id.detail_drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarToggle;
    private AWSDocumentation mAWSDocumentation;
    private LiveData<String> mAWSDetailHTML;

    private ArrayList<AWSDocumentation> mOverFlowListAWSDocumentation;
    private ArrayAdapter<String> mOverFlowAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aws_activity_documentation);


        ButterKnife.bind(this);

        if(getIntent().hasExtra(getString(R.string.detail_activity_extra))) {
            mAWSDocumentation = getIntent().getParcelableExtra(getString(R.string.detail_activity_extra));
        }
        setTitle(mAWSDocumentation.getDocumentationName());

        mActionBarToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,R.string.navigation_open,R.string.navigation_close);

        mActionBarToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewModel();

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                WebView.HitTestResult hr = ((WebView)view).getHitTestResult();

                String intentURL = hr.getExtra();
                String previousTitle = mAWSDocumentation.getDocumentationName();
                AWSDocumentation intentAWSDocumentation = new AWSDocumentation(previousTitle,intentURL);
                return launchNewDetailActivity(intentAWSDocumentation);
            }
        });

        mOverFlowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AWSDocumentation clickedLink = mOverFlowListAWSDocumentation.get(i);

                launchNewDetailActivity(clickedLink);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mActionBarToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewModel() {

        AWSDocumentationPageViewModel listViewModel = ViewModelProviders.of(this, new AWSDetailPageViewModelFactory(this.getApplication(),mAWSDocumentation))
                .get(AWSDocumentationPageViewModel.class);
        mAWSDetailHTML = listViewModel.returnAWSPageHTML();

        mAWSDetailHTML.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                String newHTML = parseHTML(s);

                setupOverflowList(s);
                setupOverflowMenu();

                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadData(newHTML,"text/html","UTF-8");
            }
        });
    }

    private String parseHTML(String html) {

        Document newDoc = Jsoup.parse(html);

        Element urlData = newDoc.getElementById("main-col-body");

        if(urlData.getElementById("divRegionDisclaimer") != null) {
            urlData.getElementById("divRegionDisclaimer").remove();
        }

        if( urlData.select("table") != null) {
            urlData.select("table").get(0).remove();
        }

        return urlData.html();
    }

    private void setupOverflowList(String html) {

        mOverFlowListAWSDocumentation = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        
        Element urlLinks = doc.getElementsByClass("awstoc").get(0);

        Elements links = urlLinks.select("li[class~=awstoc*]");

        for(Element link : links) {

            Element selectedLink = link.select("a").get(0);


            String url = selectedLink.attr("href");

            String title = selectedLink.text();

            AWSDocumentation overflowLink = new AWSDocumentation(title,url);

            mOverFlowListAWSDocumentation.add(overflowLink);
        }
    }

    private void setupOverflowMenu() {

        if (mOverFlowListAWSDocumentation.size() == 0){
            return;
        }
        ArrayList<String> overFlowTitleList = new ArrayList<>();

        for(int i = 0; i < mOverFlowListAWSDocumentation.size(); i++) {
            overFlowTitleList.add(mOverFlowListAWSDocumentation.get(i).getDocumentationName());
        }

        String [] stringArrayList = overFlowTitleList.toArray(new String[overFlowTitleList.size()]);

        mOverFlowAdapter = new ArrayAdapter<String>(this,R.layout.detail_overflow_adapter,stringArrayList);

        mOverFlowList.setAdapter(mOverFlowAdapter);
    }

    private boolean launchNewDetailActivity(AWSDocumentation intentAWSDocumentation) {

        Intent intent;

        String intentURL = intentAWSDocumentation.getUrl();

        if(intentURL == null){

            return false;
        }

        if (intentURL.contains("guide")) {
            intent = new Intent(AWSDocumentationActivity.this, AWSDocumentationActivity.class);
            intent.putExtra(getString(R.string.detail_activity_extra),intentAWSDocumentation);
            startActivity(intent);
        }
        return false;
    }
}
