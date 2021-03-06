package com.example.johann.awsdocs.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.johann.awsdocs.R;
import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.repository.ServiceRepository;
import com.example.johann.awsdocs.ui.AWSDetailActivity;
import com.example.johann.awsdocs.ui.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class AWSWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context,AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.awswidget_provider);

        intent = new Intent(context, MainActivity.class);

        String aws_widget_title = context.getString(R.string.app_name);
        views.setTextViewText(R.id.aws_widget_title, aws_widget_title);

        Intent listIntent = new Intent(context,AWSWidgetRemoteViewService.class);

        views.setRemoteAdapter(R.id.widget_list_view,listIntent);

        views.setEmptyView(R.id.widget_list_view,R.id.empty_widget_list);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.aws_widget_title,pendingIntent);

        Intent serviceIntent = new Intent(context,AWSDetailActivity.class);

        PendingIntent servicePendingIntent = PendingIntent.getActivity(context,appWidgetId,serviceIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.widget_list_view,servicePendingIntent);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private static ArrayList<AWSService> getServices(Context context) {

        ServiceRepository serviceRepository = new ServiceRepository(context);
        ArrayList<AWSService> awsService = new ArrayList<>();

        try {
            awsService = serviceRepository.getServices();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        return awsService;
    }
}

