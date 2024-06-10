package com.example.plant_system.repository;

import com.example.plant_system.model.Plant;
import com.example.plant_system.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorsDataRepository extends JpaRepository<SensorData, Long> {

    @Query("SELECT s FROM SensorData s WHERE s.plant = :plant AND s.dataTime BETWEEN :start AND :end")
    List<SensorData> findByPlantIdAndTimestampBetween(@Param("plant") Plant plant, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
