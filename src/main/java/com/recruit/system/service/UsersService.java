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

    public AuthResponse register(RegisterRequest request) {
        return createUser(request, "APPLICANT");
    }

    public AuthResponse createHR(RegisterRequest request) {
        return createUser(request, "HR");
    }

    private AuthResponse createUser(RegisterRequest request, String roleName) {
        String username = request.getUsername().trim().toLowerCase();
        String email = request.getEmail().trim().toLowerCase();

        if (usersRepository.existsByUsername(username)) {
            return new AuthResponse(false, "Username already exists", null, null, null, null);
        }

        if (usersRepository.existsByEmail(email)) {
            return new AuthResponse(false, "Email already exists", null, null, null, null);
        }

        Roles role = rolesRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        usersRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        // Now we return userId as part of the AuthResponse
        return new AuthResponse(true, roleName + " created successfully", token, user.getUsername(), user.getRole().getName(), user.getId());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Users user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        // Returning the AuthResponse with userId
        return new AuthResponse(true, "Login successful", token, user.getUsername(), user.getRole().getName(), user.getId());
    }

    public List<UserResponse> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().getName()))
                .collect(Collectors.toList());
    }
}