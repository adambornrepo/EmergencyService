package com.tech.payload.response.simple;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.concretes.Address;
import com.tech.entites.enums.Gender;
import com.tech.entites.enums.Role;
import com.tech.entites.enums.Zone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleDoctorResponse implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Role role;

    private Zone zone;

    private boolean isDisabled;

}
