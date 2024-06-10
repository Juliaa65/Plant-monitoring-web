package com.example.plant_system.service;

import com.example.plant_system.payload.request.UserRequest;
import com.example.plant_system.payload.response.UserResponse;
import com.example.plant_system.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User readById(long id);
    User update(User user);
    User findByLogin(String email);

    UserResponse findByLoginAndPassword(UserRequest userRequest);

    void delete(long id);
    List<User> getAll();
}
