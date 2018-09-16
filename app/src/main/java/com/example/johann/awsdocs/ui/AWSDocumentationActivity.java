package com.example.johann.awsdocs.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.adapters.MainRecyclerViewAdapter;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.factory.AWSDetailPageViewModelFactory;
import com.example.johann.awsdocs.utils.NetworkUtils;
import com.example.johann.awsdocs.viewmodels.AWSDetailPageViewModel;
import com.example.johann.awsdocs.viewmodels.AWSServiceListViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AWSDocumentationActivity extends AppCompatActivity {

    @BindView(R.id.documentation_header)
    WebView mWebView;


    private AWSDocumentation mAWSDocumentation;
    private String mIntentURL;
    private LiveData<String> mAWSDetailHTML;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aws_activity_documentation);


        ButterKnife.bind(this);

        if(getIntent().hasExtra(getString(R.string.detail_activity_extra))) {
            mAWSDocumentation = getIntent().getParcelableExtra(getString(R.string.detail_activity_extra));
        }
        setTitle(mAWSDocumentation.getDocumentationName());
        //String documentationHTML = NetworkUtils.makeDocumentationRequestOffline(this);

        setupViewModel();

        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                WebView.HitTestResult hr = ((WebView)view).getHitTestResult();
                Timber.i( "getExtra = "+ hr.getExtra());

                mIntentURL = hr.getExtra();
                String previousTitle = mAWSDocumentation.getDocumentationName();
                AWSDocumentation intentAWSDocumentation = new AWSDocumentation(previousTitle,mIntentURL);
                Intent intent;

                if(mIntentURL == null){
                    return false;

                }

                if (mIntentURL.contains("guide")) {
                    intent = new Intent(AWSDocumentationActivity.this, AWSDocumentationActivity.class);
                    intent.putExtra(getString(R.string.detail_activity_extra),intentAWSDocumentation);
                    startActivity(intent);
                }

                return false;
            }
        });
    }

    private void setupViewModel() {

        AWSDetailPageViewModel listViewModel = ViewModelProviders.of(this, new AWSDetailPageViewModelFactory(this.getApplication(),mAWSDocumentation))
                .get(AWSDetailPageViewModel.class);
        mAWSDetailHTML = listViewModel.returnAWSPageHTML();
        mAWSDetailHTML.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadData(s,"text/html","UTF-8");
            }
        });
    }
}
