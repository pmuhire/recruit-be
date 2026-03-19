package com.recruit.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruit.system.dto.request.RegisterRequest;
import com.recruit.system.dto.response.AuthResponse;
import com.recruit.system.dto.response.UserResponse;
import com.recruit.system.model.Roles;
import com.recruit.system.security.JwtAuthenticationFilter;
import com.recruit.system.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class
                )
        }
)
@Import(AuthControllerSecurityTest.TestSecurityConfig.class)
class AuthControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsersService usersService;

    @TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/register", "/auth/login").permitAll()
                            .anyRequest().authenticated()
                    )
                    .httpBasic(Customizer.withDefaults())
                    .build();
        }
    }

    @Test
    @WithMockUser(roles = "HR")
    void shouldAllowHrToGetAllUsers() throws Exception {
        Roles role = new Roles();
        role.setName("HR");

        UserResponse user = new UserResponse(1L, "alice", "alice@example.com", role);
        when(usersService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk());

        verify(usersService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminToGetAllUsers() throws Exception {
        Roles role = new Roles();
        role.setName("APPLICANT");

        UserResponse user = new UserResponse(1L, "patrick", "patrick@example.com", role);
        when(usersService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk());

        verify(usersService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "APPLICANT")
    void shouldForbidApplicantFromGettingAllUsers() throws Exception {
        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(usersService);
    }

    @Test
    void shouldRequireAuthenticationForGettingAllUsers() throws Exception {
        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(usersService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminToCreateHr() throws Exception {
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

        when(usersService.createHR(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/create-hr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(usersService).createHR(any(RegisterRequest.class));
    }

    @Test
    @WithMockUser(roles = "HR")
    void shouldForbidHrFromCreatingHr() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("hruser");
        request.setEmail("hr@example.com");
        request.setPassword("123456");

        mockMvc.perform(post("/auth/create-hr")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(usersService);
    }

    @Test
    @WithMockUser(roles = "SUPERADMIN")
    void shouldAllowSuperadminToCreateHrWithDefaultPassword() throws Exception {
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
                .andExpect(status().isOk());

        verify(usersService).createHRWithDefaultPassword("hrdefault", "hrdefault@example.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldForbidAdminFromCreatingHrWithDefaultPassword() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("hrdefault");
        request.setEmail("hrdefault@example.com");

        mockMvc.perform(post("/auth/create-hr-default")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(usersService);
    }

    @Test
    void shouldAllowAnonymousRegister() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("patrick");
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        AuthResponse response = new AuthResponse(
                true,
                "APPLICANT created successfully",
                "token",
                "patrick",
                "APPLICANT",
                1L
        );

        when(usersService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(usersService).register(any(RegisterRequest.class));
    }

    @Test
    void shouldAllowAnonymousLogin() throws Exception {
        String body = """
                {
                  "email": "patrick@example.com",
                  "password": "123456"
                }
                """;

        AuthResponse response = new AuthResponse(
                true,
                "Login successful",
                "login-token",
                "patrick",
                "APPLICANT",
                1L
        );

        when(usersService.login(any())).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(usersService).login(any());
    }
}