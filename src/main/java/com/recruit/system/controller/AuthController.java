package com.recruit.system.controller;

import com.recruit.system.dto.request.LoginRequest;
import com.recruit.system.dto.request.RegisterRequest;
import com.recruit.system.dto.response.AuthResponse;
import com.recruit.system.dto.response.UserResponse;
import com.recruit.system.service.UsersService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsersService userService;

    public AuthController(UsersService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}