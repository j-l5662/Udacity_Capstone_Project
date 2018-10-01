package com.example.johann.awsdocs.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {AWSDocumentation.class}, version = 1, exportSchema = false)
public abstract class DocumentationDatabase extends RoomDatabase{

    public abstract DocumentationDao daoAccess();
}
