package com.example.plant_system.payload.response;

import com.example.plant_system.model.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    Long id;
    String firstName;
    String lastName;
    String email;
    String info;



    public UserResponse(User user) {
        id = user.getId();
        firstName = user.getName();
        lastName = user.getSurname();
        email = user.getLogin();
        info = user.getInfo();
    }
}
