package org.example.ecommerce.service;

import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String firstname, String password) {
        return userRepository.findByFirtstnameAndPassword(firstname,password).isPresent();
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
