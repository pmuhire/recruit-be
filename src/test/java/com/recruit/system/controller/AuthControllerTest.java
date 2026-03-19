package com.recruit.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruit.system.dto.request.LoginRequest;
import com.recruit.system.dto.request.RegisterRequest;
import com.recruit.system.dto.response.AuthResponse;
import com.recruit.system.dto.response.UserResponse;
import com.recruit.system.model.Roles;
import com.recruit.system.security.JwtAuthenticationFilter;
import com.recruit.system.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsersService usersService;

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("patrick");
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        AuthResponse response = new AuthResponse(
                true,
                "APPLICANT created successfully",
                "mocked-token",
                "patrick",
                "APPLICANT",
                1L
        );

        when(usersService.register(org.mockito.ArgumentMatchers.any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("APPLICANT created successfully"))
                .andExpect(jsonPath("$.token").value("mocked-token"))
                .andExpect(jsonPath("$.username").value("patrick"))
                .andExpect(jsonPath("$.role").value("APPLICANT"))
                .andExpect(jsonPath("$.userId").value(1L));

        verify(usersService).register(org.mockito.ArgumentMatchers.any(RegisterRequest.class));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        AuthResponse response = new AuthResponse(
                true,
                "Login successful",
                "login-token",
                "patrick",
                "APPLICANT",
                1L
        );

        when(usersService.login(org.mockito.ArgumentMatchers.any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value("login-token"))
                .andExpect(jsonPath("$.username").value("patrick"))
                .andExpect(jsonPath("$.role").value("APPLICANT"))
                .andExpect(jsonPath("$.userId").value(1L));

        verify(usersService).login(org.mockito.ArgumentMatchers.any(LoginRequest.class));
    }

    @Test
    void shouldGetAllUsersSuccessfully() throws Exception {
        Roles applicantRole = new Roles();
        applicantRole.setName("APPLICANT");

        Roles hrRole = new Roles();
        hrRole.setName("HR");

        UserResponse user1 = new UserResponse(1L, "patrick", "patrick@example.com", applicantRole);
        UserResponse user2 = new UserResponse(2L, "alice", "alice@example.com", hrRole);

        when(usersService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("patrick"))
                .andExpect(jsonPath("$[0].email").value("patrick@example.com"))
                .andExpect(jsonPath("$[0].role").value("APPLICANT"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].username").value("alice"))
                .andExpect(jsonPath("$[1].email").value("alice@example.com"))
                .andExpect(jsonPath("$[1].role").value("HR"));

        verify(usersService).getAllUsers();
    }

    @Test
    void shouldCreateHrSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("hruser");
        request.setEmail("hr@example.com");
        request.setPassword("123456");

        AuthResponse response = new AuthResponse(
                true,
                "HR created successfully",
                "hr-token",
                "hruser",
                "HR",
                2L
        );

        when(usersService.createHR(org.mockito.ArgumentMatchers.any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/create-hr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("HR created successfully"))
                .andExpect(jsonPath("$.token").value("hr-token"))
                .andExpect(jsonPath("$.username").value("hruser"))
                .andExpect(jsonPath("$.role").value("HR"))
                .andExpect(jsonPath("$.userId").value(2L));

        verify(usersService).createHR(org.mockito.ArgumentMatchers.any(RegisterRequest.class));
    }

    @Test
    void shouldCreateHrWithDefaultPasswordSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("hrdefault");
        request.setEmail("hrdefault@example.com");

        AuthResponse response = new AuthResponse(
                true,
                "HR created successfully",
                "default-hr-token",
                "hrdefault",
                "HR",
                3L
        );

        when(usersService.createHRWithDefaultPassword("hrdefault", "hrdefault@example.com"))
                .thenReturn(response);

        mockMvc.perform(post("/auth/create-hr-default")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("HR created successfully"))
                .andExpect(jsonPath("$.token").value("default-hr-token"))
                .andExpect(jsonPath("$.username").value("hrdefault"))
                .andExpect(jsonPath("$.role").value("HR"))
                .andExpect(jsonPath("$.userId").value(3L));

        verify(usersService).createHRWithDefaultPassword("hrdefault", "hrdefault@example.com");
    }

    @Test
    void shouldUpdatePasswordSuccessfully() throws Exception {
        Long userId = 1L;
        String newPassword = "newPassword123";

        AuthResponse response = new AuthResponse(
                true,
                "Password updated successfully",
                null,
                null,
                null,
                userId
        );

        when(usersService.updatePassword(eq(userId), anyString())).thenReturn(response);

        mockMvc.perform(put("/auth/update-password/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password updated successfully"))
                .andExpect(jsonPath("$.userId").value(1L));

        verify(usersService).updatePassword(eq(userId), anyString());
    }
}