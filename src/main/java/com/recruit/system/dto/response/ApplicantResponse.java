package com.recruit.system.dto.response;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicantResponse {

    private Long id;

    private String nidNumber;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private LocalDate dateOfBirth;

}
