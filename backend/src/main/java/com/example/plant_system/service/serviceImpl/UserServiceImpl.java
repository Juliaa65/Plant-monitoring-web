package com.example.plant_system.service.serviceImpl;

import com.example.plant_system.payload.request.UserRequest;
import com.example.plant_system.payload.response.UserResponse;
import com.example.plant_system.model.User;
import com.example.plant_system.repository.UserRepository;
import com.example.plant_system.service.UserService;
import com.example.plant_system.exception.NullEntityReferenceException;
import javax.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User create(User role) {
        if (role != null) {
            return userRepository.save(role);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User update(User role) {
        if (role != null) {
            readById(role.getId());
            return userRepository.save(role);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public User findByLogin(String login) {
        if(!login.isBlank()){
            return userRepository.findUserByLogin(login);}
        else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Override
    public UserResponse findByLoginAndPassword(UserRequest userRequest) {
        UserResponse result = null;
        User user = userRepository.findUserByLogin(userRequest.getEmail());
        if ((user != null)
                && (passwordEncoder.matches(userRequest.getPassword(),
                user.getPassword()))) {
            result = new UserResponse(user);
        }
        return result;
    }

    @Override
    public void delete(long id) {
        User user = readById(id);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }


}
