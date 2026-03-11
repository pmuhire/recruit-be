package com.recruit.system.dto.request;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicantCreateRequest {

    private String nidNumber;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private LocalDate dateOfBirth;

}