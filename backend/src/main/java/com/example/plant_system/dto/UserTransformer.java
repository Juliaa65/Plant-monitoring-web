package com.example.plant_system.dto;

import com.example.plant_system.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserTransformer {

    static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User convertToEntity(UserDto userDto) {

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getFirstname());
        user.setSurname(userDto.getLastname());
        user.setLogin(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setInfo(userDto.getInfo());
        return user;
    }
}
