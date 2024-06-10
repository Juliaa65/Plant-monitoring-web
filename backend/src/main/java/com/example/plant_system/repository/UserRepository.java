package com.example.plant_system.repository;

import com.example.plant_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   User findUserByLogin(String login);
}
