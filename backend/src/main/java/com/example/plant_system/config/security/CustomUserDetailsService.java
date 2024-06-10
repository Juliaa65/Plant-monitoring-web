package com.example.plant_system.config.security;

import com.example.plant_system.config.security.CustomUserDetails;
import com.example.plant_system.model.User;
import com.example.plant_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByLogin(username);
        if (user==null)
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }


}
