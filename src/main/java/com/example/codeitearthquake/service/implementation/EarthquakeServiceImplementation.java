package com.example.codeitearthquake.service.implementation;

import com.example.codeitearthquake.dto.EarthquakeResponse;
import com.example.codeitearthquake.dto.Feature;
import com.example.codeitearthquake.dto.Properties;
import com.example.codeitearthquake.model.Earthquake;
import com.example.codeitearthquake.repository.EarthquakeRepository;
import com.example.codeitearthquake.service.EarthquakeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;
import java.util.List;

import static com.example.codeitearthquake.service.FieldFilterSpecification.filterContainsText;
import static com.example.codeitearthquake.service.FieldFilterSpecification.greaterThan;

@Slf4j
@Service
@AllArgsConstructor
public class EarthquakeServiceImplementation implements EarthquakeService {
    private final EarthquakeRepository earthquakeRepository;
    private final RestTemplate restTemplate;
    @Override
    public List<Earthquake> listEarthquakes() {
        return earthquakeRepository.findAll();
    }

    @Override
    public Page<Earthquake> find(Float magnitude, String magType, String place, String title, Long eventTime, Integer pageNum, Integer pageSize) {
        Specification<Earthquake> specification = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (place != null && !place.isEmpty()) {
            specification = specification.and(filterContainsText(Earthquake.class, "place", place));
        }
        if (magnitude != null) {
            specification = specification.and(greaterThan(Earthquake.class, "magnitude", magnitude));
        }
        if (magType != null && !magType.isEmpty()) {
            specification = specification.and(filterContainsText(Earthquake.class, "magType", magType));
        }
        if (title != null && !title.isEmpty()) {
            specification = specification.and(filterContainsText(Earthquake.class, "title", title));
        }
        if (eventTime != null) {
            specification = specification.and(greaterThan(Earthquake.class, "eventTime", eventTime));
        }
        
        return this.earthquakeRepository.findAll(specification, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public void fetchAndSaveEarthquakes() {
        earthquakeRepository.deleteAll();

        //String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
        String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";
        try {
            EarthquakeResponse response = restTemplate.getForObject(url, EarthquakeResponse.class);
            if (response != null && response.getFeatures() != null) {
                //long thirtyDaysAgoInMillis = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

                // Filter quake
                List<Earthquake> earthquakesToSave = response.getFeatures().stream()
                        .map(Feature::getProperties)
                        .filter(props -> props != null 
                                && props.getMag() != null && props.getMag() > 2.0
                                && props.getTime() != null)
                        .map(props -> {
                            Earthquake entity = new Earthquake();
                            entity.setMagnitude(props.getMag());
                            entity.setMagType(props.getMagType());
                            entity.setPlace(props.getPlace());
                            entity.setTitle(props.getTitle());
                            entity.setEventTime(props.getTime());
                            return entity;
                        })
                        .collect(Collectors.toList());

                earthquakeRepository.saveAll(earthquakesToSave);
                log.info("Saved {} new earthquake records.", earthquakesToSave.size());
            }
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    @Override
    public void deleteEarthquake(Long id) {
        if (earthquakeRepository.existsById(id)) {
            earthquakeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Earthquake with id " + id + " does not exist.");
        }
    }

}
