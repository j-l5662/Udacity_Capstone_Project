package com.example.johann.awsdocs.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.factory.AWSDocumentationViewModelFactory;
import com.example.johann.awsdocs.viewmodels.AWSDocumentationListViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AWSDetailActivity extends AppCompatActivity{

    private AWSService mAWSService;

//    @BindView(R.id.aws_detail_url_text_view)
//    TextView mTextView;

    @BindView(R.id.aws_service_list)
    ListView mListView;

    private LiveData<ArrayList<AWSDocumentation>> mAWSDocumentations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.aws_activity_detail);

        ButterKnife.bind(this);
        if(getIntent().hasExtra(getString(R.string.main_activity_extra))) {
            mAWSService = getIntent().getParcelableExtra(getString(R.string.main_activity_extra));
        }
        setTitle(mAWSService.returnName());

        setupViewModel();
    }

    private void setupViewModel() {
        AWSDocumentationListViewModel listViewModel = ViewModelProviders.of(this, new AWSDocumentationViewModelFactory(this.getApplication(), mAWSService)).get(AWSDocumentationListViewModel.class);
        mAWSDocumentations = listViewModel.returnAWSDocumentationList();
        mAWSDocumentations.observe(this, new Observer<ArrayList<AWSDocumentation>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AWSDocumentation> awsDocumentations) {
                ArrayList<String> documentations = new ArrayList<>();
                for(int i = 0; i < mAWSDocumentations.getValue().size();i++){
                    documentations.add(awsDocumentations.get(i).getDocumentationName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AWSDetailActivity.this, R.layout.aws_activity_detail_list_view,R.id.text_view,documentations);
                mListView.setAdapter(adapter);
            }
        });
    }
}
