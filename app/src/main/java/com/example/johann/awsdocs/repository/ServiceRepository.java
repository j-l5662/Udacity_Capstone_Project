package com.example.johann.awsdocs.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.johann.awsdocs.data.AWSService;
import com.example.johann.awsdocs.data.ServiceDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ServiceRepository {

    private String DB_NAME = "aws_services";

    private ServiceDatabase serviceDatabase;

    public ServiceRepository(Context context) {

        serviceDatabase = Room.databaseBuilder(context,ServiceDatabase.class,DB_NAME)
                .fallbackToDestructiveMigration().build();
    }

    public void insertService(final AWSService awsService) {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                if(serviceDatabase.serviceDao().getServiceName(awsService.getServiceName()) == null) {
                    serviceDatabase.serviceDao().insert(awsService);
                }

                return null;
            }
        }.execute();
    }

    public void deleteAllDocs() {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                serviceDatabase.clearAllTables();
                return null;
            }
        }.execute();
    }

    public ArrayList<AWSService> getServices() throws ExecutionException,InterruptedException{

        final ArrayList<AWSService> awsServiceArrayList;


        AWSService[] services = new AsyncTask<Void,Void,AWSService[]>() {
            @Override
            protected AWSService[] doInBackground(Void... voids) {
                return serviceDatabase.serviceDao().getAllServices();
            }
        }.execute().get();

        awsServiceArrayList = new ArrayList<>(Arrays.asList(services));

        return awsServiceArrayList;
    }
}
