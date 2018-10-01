package com.example.johann.awsdocs.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.johann.awsdocs.data.AWSDocumentation;
import com.example.johann.awsdocs.data.DocumentationDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class DocRepository {

    private String DB_NAME = "aws_docs";

    private DocumentationDatabase documentationDatabase;

    public DocRepository(Context context) {

        documentationDatabase= Room.databaseBuilder(context,DocumentationDatabase.class,DB_NAME).build();
    }

    public void insertDoc(final AWSDocumentation awsDocumentation) {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                documentationDatabase.daoAccess().insert(awsDocumentation);
                return null;
            }
        }.execute();
    }

    public void deleteAllDocs() {

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                documentationDatabase.clearAllTables();
                return null;
            }
        }.execute();
    }

    public ArrayList<AWSDocumentation> getServiceDocs(final String awsServiceName) throws ExecutionException, InterruptedException{

        final ArrayList<AWSDocumentation> awsDocumentations;

        AWSDocumentation[] documentations = new AsyncTask<Void,Void,AWSDocumentation[]>() {
            @Override
            protected AWSDocumentation[] doInBackground(Void... voids) {
                return documentationDatabase.daoAccess().queryDocumentations(awsServiceName);
            }
        }.execute().get();

        awsDocumentations = new ArrayList<>(Arrays.asList(documentations));

        return awsDocumentations;
    }

    public AWSDocumentation getDocumentation(final String awsDocumentation) throws ExecutionException, InterruptedException{

        AWSDocumentation queriedDocumentation = new AsyncTask<Void,Void,AWSDocumentation>() {
            @Override
            protected AWSDocumentation doInBackground(Void... voids) {
                return documentationDatabase.daoAccess().queryDocumentationHTML(awsDocumentation);
            }
        }.execute().get();

        return queriedDocumentation;
    }
}
