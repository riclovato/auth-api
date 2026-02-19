package com.ricardolovato.usermanagement.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ricardolovato.usermanagement.entity.Role;
import com.ricardolovato.usermanagement.entity.User;
import com.ricardolovato.usermanagement.exception.BusinessException;
import com.ricardolovato.usermanagement.repository.UserRepository;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
}
