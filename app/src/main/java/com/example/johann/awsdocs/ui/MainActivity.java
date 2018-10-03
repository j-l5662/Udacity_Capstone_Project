package com.example.johann.awsdocs.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.example.johann.awsdocs.BuildConfig;
import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.adapters.MainRecyclerViewAdapter;
import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.log.NoLoggingTree;
import com.example.johann.awsdocs.repository.DocRepository;
import com.example.johann.awsdocs.repository.ServiceRepository;
import com.example.johann.awsdocs.viewmodels.AWSServiceListViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.ServiceClickListener{

    @BindView(R.id.services_recycler_view)
    public RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private MainRecyclerViewAdapter mAdapter;

    private LiveData<ArrayList<AWSService>> mAwsServiceList;

    private ServiceRepository mServiceRepository;
    private DocRepository mDocRepository;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG)
            Timber.plant(new DebugTree());
        else{

            Timber.plant(new NoLoggingTree());
        }
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mDocRepository = new DocRepository(getApplicationContext());

        mServiceRepository = new ServiceRepository(getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        sendLoggingEventtoFirebase();

        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearDB:
                clearDataBases();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(int position) {

        Context context = MainActivity.this;

        AWSService awsService = mAwsServiceList.getValue().get(position);

        mServiceRepository.insertService(awsService);

        Class destinationActivity;
        Intent intent;
        if(awsService.getServiceURL().contains("UserGuide")) {

            destinationActivity = AWSDocumentationActivity.class;
            intent = new Intent(context,destinationActivity);
            intent.putExtra(getString(R.string.detail_activity_extra),new AWSDocumentation(awsService.getServiceName(),awsService.getServiceURL()));
        }
        else{

            destinationActivity = AWSDetailActivity.class;
            intent = new Intent(context,destinationActivity);
            intent.putExtra(getString(R.string.main_activity_extra),awsService);

        }
        startActivity(intent);
    }

    private void setupViewModel() {

        AWSServiceListViewModel listViewModel = ViewModelProviders.of(this).get(AWSServiceListViewModel.class);
        mAwsServiceList = listViewModel.getAWSServiceList();
        mAwsServiceList.observe(this, new Observer<ArrayList<AWSService>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AWSService> awsServices) {
                mAdapter = new MainRecyclerViewAdapter(awsServices,MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    private void clearDataBases() {

        mServiceRepository.deleteAllDocs();

        mDocRepository.deleteAllDocs();
    }

    private void sendLoggingEventtoFirebase() {

        Bundle bundle = new Bundle();

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,bundle);
    }
}
