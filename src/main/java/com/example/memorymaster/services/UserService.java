package com.example.memorymaster.services;

import com.example.memorymaster.repository.UserRepository;
import com.example.memorymaster.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
