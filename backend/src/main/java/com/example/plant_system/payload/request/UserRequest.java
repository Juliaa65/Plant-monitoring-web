package com.example.plant_system.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
public class UserRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

}