package com.recruit.system.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "applicants")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(unique = true, nullable = false)
    private String nidNumber;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private LocalDate dateOfBirth;

    private LocalDateTime createdAt;

    public Applicant() {
    }

    public Applicant(Long id, Users user, String nidNumber, String firstName,
                     String lastName, String phone, String email,
                     LocalDate dateOfBirth, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.nidNumber = nidNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public String getNidNumber() {
        return nidNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setNidNumber(String nidNumber) {
        this.nidNumber = nidNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}