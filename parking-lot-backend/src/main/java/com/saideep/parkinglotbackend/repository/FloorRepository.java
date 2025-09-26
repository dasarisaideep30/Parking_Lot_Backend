package com.saideep.parkinglotbackend.repository;

import com.saideep.parkinglotbackend.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {}