package com.example.johann.awsdocs.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.adapters.DetailRecyclerViewAdapter;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.factory.AWSDocumentationListViewModelFactory;
import com.example.johann.awsdocs.viewmodels.AWSDocumentationListViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AWSDetailActivity extends AppCompatActivity implements DetailRecyclerViewAdapter.ServiceClickListener{

    private AWSService mAWSService;

    private RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.aws_service_list_recycler_view)
    RecyclerView mRecyclerView;

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

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupViewModel();
    }

    private void setupViewModel() {
        AWSDocumentationListViewModel listViewModel = ViewModelProviders.of(this, new AWSDocumentationListViewModelFactory(this.getApplication(),
                mAWSService)).get(AWSDocumentationListViewModel.class);
        mAWSDocumentations = listViewModel.returnAWSDocumentationList();
        mAWSDocumentations.observe(this, new Observer<ArrayList<AWSDocumentation>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AWSDocumentation> awsDocumentations) {

                DetailRecyclerViewAdapter adapter = new DetailRecyclerViewAdapter(awsDocumentations,AWSDetailActivity.this);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(int position) {

        AWSDocumentation awsDocumentation = mAWSDocumentations.getValue().get(position);


        Class documentationActivity = AWSDocumentationActivity.class;
        Intent intent = new Intent(this,documentationActivity);
        intent.putExtra(getString(R.string.detail_activity_extra),awsDocumentation);

        startActivity(intent);

    }
}
