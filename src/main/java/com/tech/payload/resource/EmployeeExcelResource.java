package com.tech.payload.resource;

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
public class EmployeeExcelResource implements Serializable {

    @ExportToExcel(index = 0, headerText = "ID", width = 3000)
    private Long id;

    @ExportToExcel(index = 1, headerText = "FIRSTNAME", width = 5000)
    private String firstName;

    @ExportToExcel(index = 2, headerText = "LASTNAME", width = 5000)
    private String lastName;

    @ExportToExcel(index = 3, headerText = "SSN", width = 4000)
    private String ssn;

    @ExportToExcel(index = 4, headerText = "GENDER", width = 3000)
    private Gender gender;

    @ExportToExcel(index = 5, headerText = "PHONE", width = 5000)
    private String phoneNumber;

    @ExportToExcel(index = 6, headerText = "BIRTHDATE", width = 4000)
    private LocalDate birthDate;

    @ExportToExcel(index = 7, headerText = "STREET", width = 4000)
    private String street;

    @ExportToExcel(index = 8, headerText = "CITY", width = 4000)
    private String city;

    @ExportToExcel(index = 9, headerText = "STATE", width = 4000)
    private String state;

    @ExportToExcel(index = 10, headerText = "POSITION", width = 7000)
    private Role role;

}
