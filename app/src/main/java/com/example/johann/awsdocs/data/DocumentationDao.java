package com.example.johann.awsdocs.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface DocumentationDao {

    @Insert
    Long insert(AWSDocumentation awsDocumentation);

    @Query("SELECT * FROM documentations WHERE awsService =:service")
    AWSDocumentation[] queryDocumentations(String service);

    @Query("DELETE FROM documentations")
    void deleteAllDocumentations();

    @Query("SELECT * FROM documentations WHERE documentation_name =:docName")
    AWSDocumentation queryDocumentationHTML(String docName);

}
