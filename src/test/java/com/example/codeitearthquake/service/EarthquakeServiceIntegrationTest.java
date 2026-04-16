package com.example.codeitearthquake.service;

import com.example.codeitearthquake.dto.EarthquakeResponse;
import com.example.codeitearthquake.dto.Feature;
import com.example.codeitearthquake.dto.Properties;
import com.example.codeitearthquake.model.Earthquake;
import com.example.codeitearthquake.repository.EarthquakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EarthquakeServiceIntegrationTest {

    @Autowired
    private EarthquakeService earthquakeService;

    @Autowired
    private EarthquakeRepository earthquakeRepository;

    @MockitoBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        earthquakeRepository.deleteAll();
    }

    @Test
    void testFetchAndSaveEarthquakes() {
        // prep the quake response
        EarthquakeResponse mockResponse = new EarthquakeResponse();
        
        Feature validFeature = new Feature();
        Properties validProps = new Properties();
        validProps.setMag(3.5f);
        validProps.setPlace("California");
        validProps.setTime(System.currentTimeMillis());
        validFeature.setProperties(validProps);

        Feature invalidFeature = new Feature();
        Properties invalidProps = new Properties();
        invalidProps.setMag(1.5f); // should be filtered out (mag < 2.0)
        invalidProps.setPlace("Nevada");
        invalidProps.setTime(System.currentTimeMillis());
        invalidFeature.setProperties(invalidProps);

        mockResponse.setFeatures(List.of(validFeature, invalidFeature));

        // m,ake RestTemplate return our mocked response instead of calling the real USGS API
        when(restTemplate.getForObject(any(String.class), eq(EarthquakeResponse.class)))
                .thenReturn(mockResponse);

        // execute
        earthquakeService.fetchAndSaveEarthquakes();

        // check
        List<Earthquake> savedEarthquakes = earthquakeRepository.findAll();
        assertEquals(1, savedEarthquakes.size(), "Only one earthquake should be saved due to magnitude filter");
        assertEquals(3.5f, savedEarthquakes.get(0).getMagnitude());
        assertEquals("California", savedEarthquakes.get(0).getPlace());
    }

    @Test
    void testFindAndFilter() {
        //prepare some data
        Earthquake e1 = new Earthquake(null, 3.0f, "ml", "Los Angeles", "M 3.0", System.currentTimeMillis());
        Earthquake e2 = new Earthquake(null, 4.5f, "ml", "San Francisco", "M 4.5", System.currentTimeMillis());
        earthquakeRepository.saveAll(List.of(e1, e2));

        //test the filter
        Page<Earthquake> resultMag = earthquakeService.find(4.0f, null, null, null, null, 0, 10);
        assertEquals(1, resultMag.getTotalElements());
        assertEquals("San Francisco", resultMag.getContent().get(0).getPlace());
        
        // test place filter city of angels
        Page<Earthquake> resultPlace = earthquakeService.find(null, null, "Los", null, null, 0, 10);
        assertEquals(1, resultPlace.getTotalElements());
        assertEquals("Los Angeles", resultPlace.getContent().get(0).getPlace());
    }

    @Test
    void testDeleteEarthquake() {
        // make new quake
        Earthquake e1 = new Earthquake(null, 3.0f, "ml", "Los Angeles", "M 3.0", System.currentTimeMillis());
        Earthquake saved = earthquakeRepository.save(e1);
        assertNotNull(saved.getId());

        // try to delete
        earthquakeService.deleteEarthquake(saved.getId());

        // check if quake deleted
        assertFalse(earthquakeRepository.existsById(saved.getId()), "Earthquake should be deleted from DB");
    }
}
