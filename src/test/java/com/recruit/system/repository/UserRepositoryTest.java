package com.recruit.system.repository;

import com.recruit.system.model.Roles;
import com.recruit.system.model.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@org.springframework.test.context.ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Roles createAndSaveRole(String roleName) {
        Roles role = new Roles();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    private Users createUser(String username, String email, String password, Roles role) {
        Users user = new Users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUserSuccessfully() {
        Roles applicantRole = createAndSaveRole("APPLICANT");

        Users user = createUser(
                "patrick",
                "patrick@example.com",
                "encodedPassword",
                applicantRole
        );

        Users savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("patrick", savedUser.getUsername());
        assertEquals("patrick@example.com", savedUser.getEmail());
        assertEquals("APPLICANT", savedUser.getRole().getName());
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        Roles hrRole = createAndSaveRole("HR");

        Users user = createUser(
                "alice",
                "alice@example.com",
                "encodedPassword",
                hrRole
        );

        userRepository.save(user);

        Optional<Users> foundUser = userRepository.findByEmail("alice@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("alice", foundUser.get().getUsername());
        assertEquals("alice@example.com", foundUser.get().getEmail());
        assertEquals("HR", foundUser.get().getRole().getName());
    }

    @Test
    @DisplayName("Should return empty when email does not exist")
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<Users> foundUser = userRepository.findByEmail("missing@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
        Roles adminRole = createAndSaveRole("ADMIN");

        Users user = createUser(
                "john",
                "john@example.com",
                "encodedPassword",
                adminRole
        );

        userRepository.save(user);

        Optional<Users> foundUser = userRepository.findByUsername("john");

        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
        assertEquals("ADMIN", foundUser.get().getRole().getName());
    }

    @Test
    @DisplayName("Should return empty when username does not exist")
    void shouldReturnEmptyWhenUsernameDoesNotExist() {
        Optional<Users> foundUser = userRepository.findByUsername("unknown");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {
        Roles applicantRole = createAndSaveRole("APPLICANT");

        Users user = createUser(
                "mark",
                "mark@example.com",
                "encodedPassword",
                applicantRole
        );

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("mark@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("ghost@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return true when username exists")
    void shouldReturnTrueWhenUsernameExists() {
        Roles hrRole = createAndSaveRole("HR");

        Users user = createUser(
                "grace",
                "grace@example.com",
                "encodedPassword",
                hrRole
        );

        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("grace");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when username does not exist")
    void shouldReturnFalseWhenUsernameDoesNotExist() {
        boolean exists = userRepository.existsByUsername("nobody");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should get all saved users")
    void shouldGetAllSavedUsers() {
        Roles applicantRole = createAndSaveRole("APPLICANT");
        Roles hrRole = createAndSaveRole("HR");

        Users user1 = createUser(
                "patrick",
                "patrick@example.com",
                "pass1",
                applicantRole
        );

        Users user2 = createUser(
                "alice",
                "alice@example.com",
                "pass2",
                hrRole
        );

        userRepository.save(user1);
        userRepository.save(user2);

        var users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        Roles applicantRole = createAndSaveRole("APPLICANT");

        Users user = createUser(
                "delete_me",
                "delete@example.com",
                "encodedPassword",
                applicantRole
        );

        Users savedUser = userRepository.save(user);

        userRepository.delete(savedUser);

        Optional<Users> foundUser = userRepository.findById(savedUser.getId());

        assertFalse(foundUser.isPresent());
    }
}