package com.tech.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.enums.Gender;
import com.tech.payload.annotations.custom.ExportToExcel;
import com.tech.security.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String ssn;

    private Gender gender;

    private String phoneNumber;

    private LocalDate birthDate;

    private String street;

    private String city;

    private String state;

    private Role role;

}
