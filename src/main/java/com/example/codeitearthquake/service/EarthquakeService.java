package com.example.codeitearthquake.service;

import com.example.codeitearthquake.model.Earthquake;
import org.springframework.data.domain.Page;

import java.util.List;


//    private Long id;
//    private Float magnitude;
//    private String magType;
//    private String place;
//    private String title;
//    private Long eventTime;

public interface EarthquakeService {
    List<Earthquake> listEarthquakes();

    Page<Earthquake> find(Float magnitude,String magType,String place, String title,Long eventTime,Integer pageNum,Integer pageSize);

    void fetchAndSaveEarthquakes();

    void deleteEarthquake(Long id);
}