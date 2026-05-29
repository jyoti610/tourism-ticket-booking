package com.college.tourism.repository;

import com.college.tourism.entity.TouristPlace.TouristPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Long> {

}
