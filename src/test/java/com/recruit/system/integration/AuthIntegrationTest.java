package com.recruit.system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruit.system.dto.request.RegisterRequest;
import com.recruit.system.model.Roles;
import com.recruit.system.repository.RoleRepository;
import com.recruit.system.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should register applicant successfully")
    void shouldRegisterApplicantSuccessfully() throws Exception {
        roleRepository.findByName("APPLICANT").orElseGet(() -> {
            Roles role = new Roles();
            role.setName("APPLICANT");
            return roleRepository.save(role);
        });

        RegisterRequest request = new RegisterRequest();
        request.setUsername("patrick");
        request.setEmail("patrick@example.com");
        request.setPassword("123456");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("APPLICANT created successfully"))
                .andExpect(jsonPath("$.username").value("patrick"))
                .andExpect(jsonPath("$.role").value("APPLICANT"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNumber());

        assertTrue(userRepository.existsByUsername("patrick"));
        assertTrue(userRepository.existsByEmail("patrick@example.com"));
    }
}