package com.ricardolovato.usermanagement.service;

import com.ricardolovato.usermanagement.entity.User;
import com.ricardolovato.usermanagement.exception.BusinessException;
import com.ricardolovato.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        boolean ok = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!ok) {
            throw new BusinessException("Invalid credentials");
        }

        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }
}
