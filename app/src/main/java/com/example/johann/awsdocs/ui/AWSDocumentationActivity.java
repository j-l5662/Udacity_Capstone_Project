package com.example.johann.awsdocs.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AWSDocumentationActivity extends AppCompatActivity {

    @BindView(R.id.documentation_header)
    TextView mHeaderTextView;

    private AWSDocumentation mAWSDocumentation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aws_activity_documentation);


        ButterKnife.bind(this);

        if(getIntent().hasExtra(getString(R.string.detail_activity_extra))) {
            mAWSDocumentation = getIntent().getParcelableExtra(getString(R.string.detail_activity_extra));
        }
        setTitle(mAWSDocumentation.getDocumentationName());
        mHeaderTextView.setText(NetworkUtils.makeDocumentationRequestOffline(this));
    }
}
