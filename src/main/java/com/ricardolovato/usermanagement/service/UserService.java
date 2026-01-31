package com.ricardolovato.usermanagement.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ricardolovato.usermanagement.entity.User;
import com.ricardolovato.usermanagement.repository.UserRepository;
import com.ricardolovato.usermanagement.exception.BusinessException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {

        boolean emailAlreadyInUse = userRepository.findByEmail(user.getEmail()).isPresent();
        if (emailAlreadyInUse) {
            throw new BusinessException("Email already in use");
        }

        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
