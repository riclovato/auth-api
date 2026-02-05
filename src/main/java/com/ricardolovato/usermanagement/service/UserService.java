package com.ricardolovato.usermanagement.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ricardolovato.usermanagement.entity.User;
import com.ricardolovato.usermanagement.exception.BusinessException;
import com.ricardolovato.usermanagement.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already in use");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("Username already in use");
        }

        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
