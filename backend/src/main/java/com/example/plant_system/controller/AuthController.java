package com.example.plant_system.controller;

import com.example.plant_system.config.security.JwtProvider;
import com.example.plant_system.dto.*;
import com.example.plant_system.model.User;
import com.example.plant_system.payload.request.UserRequest;
import com.example.plant_system.payload.response.JWTResponse;
import com.example.plant_system.payload.response.MessageResponse;
import com.example.plant_system.payload.response.TokenResponse;
import com.example.plant_system.payload.response.UserResponse;
import com.example.plant_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "http://localhost:8086")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.findByLoginAndPassword(userRequest);
        TokenResponse tokenResponse = new TokenResponse(jwtProvider.generateToken(userResponse.getEmail()));
        return ResponseEntity.ok(new JWTResponse(tokenResponse.getToken(), userResponse.getId(),
                userResponse.getFirstName() + " " + userResponse.getLastName(),
                userResponse.getEmail()));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto) {
        User user = UserTransformer.convertToEntity(userDto);
        user.setId((long) (userService.getAll().size()+2));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User newUser = userService.create(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
