package com.example.johann.awsdocs.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ServiceDao {

    @Query("SELECT * FROM services")
    List<AWSService> getAll();

    @Query("DELETE FROM services")
    void deleteAll();

    @Insert
    void insert(AWSService awsService);

}
