package com.tech.payload.response.detailed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.concretes.Address;
import com.tech.entites.enums.Gender;
import com.tech.entites.enums.Role;
import com.tech.entites.enums.Zone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedNurseResponse implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Gender gender;

    private String ssn;

    private String phoneNumber;

    private Address address;

    private Role role;

    private Zone zone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private boolean isDisabled;

}
