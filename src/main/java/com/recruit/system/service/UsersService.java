package com.recruit.system.service;

import com.recruit.system.dto.request.LoginRequest;
import com.recruit.system.dto.request.RegisterRequest;
import com.recruit.system.dto.response.AuthResponse;
import com.recruit.system.dto.response.UserResponse;
import com.recruit.system.model.Roles;
import com.recruit.system.model.Users;
import com.recruit.system.repository.RoleRepository;
import com.recruit.system.repository.UserRepository;
import com.recruit.system.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UserRepository usersRepository;
    private final RoleRepository rolesRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UsersService(UserRepository usersRepository,
                        RoleRepository rolesRepository,
                        JwtService jwtService,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // ✅ Register a new user
    public AuthResponse register(RegisterRequest request) {

        // Normalize input
        String username = request.getUsername().trim().toLowerCase();
        String email = request.getEmail().trim().toLowerCase();

        // Check duplicates
        if (usersRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (usersRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Assign default role safely (never trust client for roles)
        String roleName = "APPLICANT"; // Default role for new users

        Roles role = rolesRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Default role not found"));

        // Create user entity
        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        usersRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(true, "User registered successfully", token);
    }

    // ✅ Login user
    public AuthResponse login(LoginRequest request) {

        // Authenticate username/password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Fetch user from database
        Users user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(true, "Login successful", token);
    }

    // ✅ Read all users (response safe, no passwords)
    public List<UserResponse> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole().getName());
                    return response;
                })
                .collect(Collectors.toList());
    }
}