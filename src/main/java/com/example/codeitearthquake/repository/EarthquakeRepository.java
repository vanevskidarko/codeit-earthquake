package com.example.codeitearthquake.repository;

import com.example.codeitearthquake.model.Earthquake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EarthquakeRepository extends JpaRepository<Earthquake,Long> {
    List<Earthquake> findByMagnitudeGreaterThan(double minMagnitude);

    List<Earthquake> findEarthquakesByMagTypeContains(String magType);
    List<Earthquake> findByEventTimeAfter(long ms);
    List<Earthquake> findByMagnitudeGreaterThanAndEventTimeAfter(double minMagnitude, long ms);

    Page<Earthquake> findAll(Specification<Earthquake> specification, PageRequest of);
}
