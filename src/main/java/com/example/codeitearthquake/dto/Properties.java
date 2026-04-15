package com.example.codeitearthquake.dto;

import lombok.Data;

@Data
public class Properties {
    private Float mag;
    private String place;
    private Long time;
    private String magType;
    private String title;
}
