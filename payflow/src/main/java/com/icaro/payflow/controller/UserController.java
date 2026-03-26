package com.icaro.payflow.controller;

import com.icaro.payflow.dto.CreateUserRequest;
import com.icaro.payflow.dto.UserResponse;
import com.icaro.payflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal String email) {
        return userService.findByEmail(email);
    }
}