package com.example.johann.awsdocs.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.viewmodels.AWSDocumentationListViewModel;

public class AWSDocumentationViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private Application mApplication;
    private AWSService mAWSService;

    public AWSDocumentationViewModelFactory(Application application, AWSService awsService) {

        this.mApplication = application;
        this.mAWSService = awsService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AWSDocumentationListViewModel(mApplication,mAWSService);
    }
}
