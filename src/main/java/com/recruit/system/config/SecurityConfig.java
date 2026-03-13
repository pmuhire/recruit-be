package com.recruit.system.config;

import com.recruit.system.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/auth/me").authenticated()
                        .requestMatchers("/auth/users").hasAnyAuthority("HR", "SUPERADMIN")
                        .requestMatchers("/jobs/all", "/jobs/{id}").permitAll()
                        .requestMatchers("/jobs/create", "/jobs/{jobId}/close").hasAnyAuthority("HR", "SUPERADMIN")
                        .requestMatchers("/api/applications/my").hasAuthority("APPLICANT")
                        .requestMatchers("/api/applications").hasAnyAuthority("HR", "SUPERADMIN") // view all
                        .requestMatchers("/api/applications/job/**").hasAnyAuthority("HR", "SUPERADMIN")
                        .requestMatchers("/api/applications/{id}/approve", "/api/applications/{id}/reject").hasAnyAuthority("HR", "SUPERADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}