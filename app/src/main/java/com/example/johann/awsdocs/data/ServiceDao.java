package com.example.johann.awsdocs.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface ServiceDao {

    @Query("SELECT * FROM services")
    AWSService[] getAllServices();

    @Query("DELETE FROM services")
    void deleteAll();

    @Insert
    Long insert(AWSService awsService);

    @Query("SELECT serviceID FROM services WHERE service_name = :serviceName LIMIT 1")
    Long getServiceName(String serviceName);

}
