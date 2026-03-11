package com.recruit.system.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}