package com.example.codeitearthquake.repository;

import com.example.codeitearthquake.model.Earthquake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EarthquakeRepository extends JpaRepository<Earthquake,Long>, JpaSpecificationExecutor<Earthquake> {
    List<Earthquake> findByMagnitudeGreaterThan(double minMagnitude);

    List<Earthquake> findEarthquakesByMagTypeContains(String magType);
    List<Earthquake> findByEventTimeAfter(long ms);
    List<Earthquake> findByMagnitudeGreaterThanAndEventTimeAfter(double minMagnitude, long ms);
}
