package com.ricardolovato.usermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricardolovato.usermanagement.dto.CreateUserRequest;
import com.ricardolovato.usermanagement.dto.UserResponse;
import com.ricardolovato.usermanagement.entity.User;
import com.ricardolovato.usermanagement.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @RequestBody CreateUserRequest request) {
        User user = User.builder()
                .username(request.username())
                .fullName(request.fullName())
                .email(request.email())
                .password(request.password())
                .build();

        User savedUser = userService.create(user);

        UserResponse response = new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}