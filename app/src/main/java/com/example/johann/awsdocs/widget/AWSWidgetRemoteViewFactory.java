package com.example.johann.awsdocs.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AWSWidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<AWSService> mServices;
    private ServiceRepository mServiceRepository;

    public AWSWidgetRemoteViewFactory(Context context,Intent intent) {

        mContext = context;
        mServiceRepository = new ServiceRepository(mContext);

        returnDatabaseServices();
    }

    @Override
    public void onCreate() {
        returnDatabaseServices();

    }

    @Override
    public void onDataSetChanged() {

        if(mServices.size() == 0) {
            returnDatabaseServices();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mServices.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.awswidget_list_view);
        AWSService awsService = mServices.get(position);
        rv.setTextViewText(R.id.widget_adapter_text_view,awsService.getServiceName());

        Intent activityIntent = new Intent();
        activityIntent.putExtra(mContext.getString(R.string.main_activity_extra),awsService);

        rv.setOnClickFillInIntent(R.id.widget_adapter_text_view,activityIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void returnDatabaseServices() {
        try{
            mServices = mServiceRepository.getServices();
        }
        catch(InterruptedException e) {
            Log.v("ServiceFactory",e.toString());
        }
        catch(ExecutionException e) {
            Log.v("ServiceFactory",e.toString());
        }
    }
}
