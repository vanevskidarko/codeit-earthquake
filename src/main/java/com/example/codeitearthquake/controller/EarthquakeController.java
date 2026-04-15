package com.example.codeitearthquake.controller;

import com.example.codeitearthquake.model.Earthquake;
import com.example.codeitearthquake.service.EarthquakeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/earthquakes")
@AllArgsConstructor
public class EarthquakeController {
    
    private final EarthquakeService earthquakeService;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchEarthquakes() {
        earthquakeService.fetchAndSaveEarthquakes();
        return ResponseEntity.ok("Successfully fetched and saved earthquakes from USGS API.");
    }

    // Get all quakes
    @GetMapping("/all")
    public List<Earthquake> getAllEarthquakes() {
        return earthquakeService.listEarthquakes();
    }

    // Get paginated and filtered quakes
    @GetMapping
    public Page<Earthquake> getEarthquakes(
            @RequestParam(required = false) Float magnitude,
            @RequestParam(required = false) String magType,
            @RequestParam(required = false) String place,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long eventTime,
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return earthquakeService.find(magnitude, magType, place, title, eventTime, pageNum - 1 , pageSize);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEarthquake(@PathVariable Long id) {
        try {
            earthquakeService.deleteEarthquake(id);
            return ResponseEntity.ok("Earthquake deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
