package com.example.johann.awsdocs.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.viewmodels.AWSDocumentationPageViewModel;

public class AWSDetailPageViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private AWSDocumentation mAWSDocumentation;

    public AWSDetailPageViewModelFactory(Application application, AWSDocumentation awsDocumentation) {

        this.mApplication = application;
        this.mAWSDocumentation = awsDocumentation;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new AWSDocumentationPageViewModel(mApplication,mAWSDocumentation);
    }
}