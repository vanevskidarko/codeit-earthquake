package com.example.codeitearthquake.dto;

import lombok.Data;

import java.util.List;

@Data
public class EarthquakeResponse {
    private String type;
    private List<Feature> features;
}
