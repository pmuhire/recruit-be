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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UserRepository usersRepository;

    @Mock
    private RoleRepository rolesRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UsersService usersService;

    @Test
    void shouldFailWhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("patrick");
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        when(usersRepository.existsByUsername("patrick")).thenReturn(true);

        AuthResponse result = usersService.register(request);

        assertNotNull(result);
        assertEquals(false, result.isSuccess());
        assertEquals("Username already exists", result.getMessage());
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("patrick");
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        when(usersRepository.existsByUsername("patrick")).thenReturn(false);
        when(usersRepository.existsByEmail("patrick@example.com")).thenReturn(true);

        AuthResponse result = usersService.register(request);

        assertNotNull(result);
        assertEquals(false, result.isSuccess());
        assertEquals("Email already exists", result.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("patrick");
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        when(usersRepository.existsByUsername("patrick")).thenReturn(false);
        when(usersRepository.existsByEmail("patrick@example.com")).thenReturn(false);
        when(rolesRepository.findByName("APPLICANT")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usersService.register(request)
        );

        assertEquals("Role not found: APPLICANT", exception.getMessage());
    }

    @Test
    void shouldCreateHrSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("hruser");
        request.setEmail("hr@example.com");
        request.setPassword("123456");

        Roles hrRole = new Roles();
        hrRole.setId(2L);
        hrRole.setName("HR");

        when(usersRepository.existsByUsername("hruser")).thenReturn(false);
        when(usersRepository.existsByEmail("hr@example.com")).thenReturn(false);
        when(rolesRepository.findByName("HR")).thenReturn(Optional.of(hrRole));
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(jwtService.generateToken("hr@example.com")).thenReturn("mocked-hr-token");

        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> {
            Users user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });

        AuthResponse result = usersService.createHR(request);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals("HR created successfully", result.getMessage());
        assertEquals("mocked-hr-token", result.getToken());
        assertEquals("hruser", result.getUsername());
        assertEquals("HR", result.getRole());
        assertEquals(2L, result.getUserId());
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest();
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        Roles role = new Roles();
        role.setName("APPLICANT");

        Users user = new Users();
        user.setId(1L);
        user.setUsername("patrick");
        user.setEmail("patrick@example.com");
        user.setRole(role);

        when(usersRepository.findByEmail("patrick@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("patrick@example.com")).thenReturn("login-token");

        AuthResponse result = usersService.login(request);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals("Login successful", result.getMessage());
        assertEquals("login-token", result.getToken());
        assertEquals("patrick", result.getUsername());
        assertEquals("APPLICANT", result.getRole());
        assertEquals(1L, result.getUserId());

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
        verify(usersRepository).findByEmail("patrick@example.com");
        verify(jwtService).generateToken("patrick@example.com");
    }

    @Test
    void shouldThrowExceptionWhenLoginUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("missing@example.com");
        request.setPassword("123456");

        when(usersRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usersService.login(request)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldCreateHrWithDefaultPasswordSuccessfully() {
        Roles hrRole = new Roles();
        hrRole.setName("HR");

        when(usersRepository.existsByUsername("hruser")).thenReturn(false);
        when(usersRepository.existsByEmail("hr@example.com")).thenReturn(false);
        when(rolesRepository.findByName("HR")).thenReturn(Optional.of(hrRole));
        when(passwordEncoder.encode("defaultPassword123")).thenReturn("encodedDefaultPassword");
        when(jwtService.generateToken("hr@example.com")).thenReturn("hr-default-token");

        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> {
            Users user = invocation.getArgument(0);
            user.setId(3L);
            return user;
        });

        AuthResponse result = usersService.createHRWithDefaultPassword("HRUSER", "HR@EXAMPLE.COM");

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals("HR created successfully", result.getMessage());
        assertEquals("hr-default-token", result.getToken());
        assertEquals("hruser", result.getUsername());
        assertEquals("HR", result.getRole());
        assertEquals(3L, result.getUserId());
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        Long userId = 1L;
        String newPassword = "newPassword123";

        Roles role = new Roles();
        role.setName("APPLICANT");

        Users user = new Users();
        user.setId(userId);
        user.setUsername("patrick");
        user.setEmail("patrick@example.com");
        user.setPassword("oldPassword");
        user.setRole(role);

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        AuthResponse result = usersService.updatePassword(userId, newPassword);

        assertNotNull(result);
        assertEquals(true, result.isSuccess());
        assertEquals("Password updated successfully", result.getMessage());
        assertEquals(userId, result.getUserId());
        assertEquals("encodedNewPassword", user.getPassword());

        verify(usersRepository).findById(userId);
        verify(passwordEncoder).encode(newPassword);
        verify(usersRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPasswordForMissingUser() {
        Long userId = 99L;
        String newPassword = "newPassword123";

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usersService.updatePassword(userId, newPassword)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldGetAllUsersSuccessfully() {
        Roles applicantRole = new Roles();
        applicantRole.setName("APPLICANT");

        Roles hrRole = new Roles();
        hrRole.setName("HR");

        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("patrick");
        user1.setEmail("patrick@example.com");
        user1.setRole(applicantRole);

        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("alice");
        user2.setEmail("alice@example.com");
        user2.setRole(hrRole);

        when(usersRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponse> result = usersService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("patrick", result.get(0).getUsername());
        assertEquals("patrick@example.com", result.get(0).getEmail());
        assertEquals("APPLICANT", result.get(0).getRole());

        assertEquals(2L, result.get(1).getId());
        assertEquals("alice", result.get(1).getUsername());
        assertEquals("alice@example.com", result.get(1).getEmail());
        assertEquals("HR", result.get(1).getRole());

        verify(usersRepository).findAll();
    }
}