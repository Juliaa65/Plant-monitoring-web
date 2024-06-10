package com.example.plant_system.repository;

import com.example.plant_system.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    @Query(value = "select id, name, type, info, system_url, camera_url, user_id, location, plantDate from plants where user_id = ?1", nativeQuery = true)
    List<Plant> getPlantByUserId(long userId);

}