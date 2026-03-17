package com.recruit.system.controller;

import com.recruit.system.dto.request.LoginRequest;
import com.recruit.system.dto.request.RegisterRequest;
import com.recruit.system.dto.response.AuthResponse;
import com.recruit.system.dto.response.UserResponse;
import com.recruit.system.service.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/create-hr")
    @PreAuthorize("hasRole('ADMIN')")
    public AuthResponse createHR(@RequestBody RegisterRequest request) {
        return userService.createHR(request);
    }
    @PostMapping("/create-hr-default")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public AuthResponse createHRWithDefaultPassword(@RequestBody RegisterRequest request) {
        return userService.createHRWithDefaultPassword(request.getUsername(), request.getEmail());
    }

    // New API to update HR password
    @PutMapping("/update-password/{userId}")
    public AuthResponse updatePassword(@PathVariable Long userId, @RequestBody String newPassword) {
        return userService.updatePassword(userId, newPassword);
    }
}