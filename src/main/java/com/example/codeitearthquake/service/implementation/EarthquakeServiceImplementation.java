package com.example.codeitearthquake.service.implementation;

import com.example.codeitearthquake.model.Earthquake;
import com.example.codeitearthquake.repository.EarthquakeRepository;
import com.example.codeitearthquake.service.EarthquakeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.codeitearthquake.service.FieldFilterSpecification.filterContainsText;
import static com.example.codeitearthquake.service.FieldFilterSpecification.greaterThan;

@Service
@AllArgsConstructor
public class EarthquakeServiceImplementation implements EarthquakeService {
    private final EarthquakeRepository earthquakeRepository;
    @Override
    public List<Earthquake> listEarthquakes() {
        return earthquakeRepository.findAll();
    }

    @Override
    public Page<Earthquake> find(Float magnitude, String magType, String place, String title, Long eventTime, Integer pageNum, Integer pageSize) {
        Specification<Earthquake> specification = Specification.allOf(
                filterContainsText(Earthquake.class,"place",place),
                greaterThan(Earthquake.class,"magnitude",magnitude)
        );
        return this.earthquakeRepository.findAll(specification, PageRequest.of(pageNum,pageSize));
    }

}
