package org.example.ecommerce.service;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public boolean login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password).isPresent();
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
