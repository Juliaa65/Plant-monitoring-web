package com.example.plant_system.repository;

import com.example.plant_system.model.Photo;
import com.example.plant_system.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT p FROM Photo p WHERE p.plant = :plant AND p.dataTime BETWEEN :start AND :end")
    List<Photo> findByPlantIdAndTime(@Param("plant") Plant plant, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
