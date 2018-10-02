package com.example.johann.awsdocs.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class AWSWidgetRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new AWSWidgetRemoteViewFactory(this.getApplicationContext(),intent);
    }
}
