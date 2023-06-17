package com.tech.payload.request.register;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.Patient;
import com.tech.entites.enums.Gender;
import com.tech.payload.annotations.custom.PhoneNumber;
import com.tech.payload.annotations.custom.SSN;
import com.tech.payload.request.register.abstracts.BaseRegistrationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRegistrationRequest implements Serializable, Supplier<Patient> {

    @NotNull(message = "{validation.null.first-name}")
    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String firstName;

    @NotNull(message = "{validation.null.last-name}")
    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String lastName;

    @JsonFormat(
            pattern = "yyyy-MM-dd",
            shape = JsonFormat.Shape.STRING
    )
    @Past(message = "{validation.date.past}")
    @NotNull(message = "{validation.null.birthdate}")
    private LocalDate birthDate;

    @NotNull(message = "{validation.null.gender}")
    private Gender gender;

    @SSN
    @NotNull(message = "{validation.null.ssn}")
    private String ssn;

    @PhoneNumber
    @NotNull(message = "{validation.null.phone}")
    private String phoneNumber;

    @Valid
    private Address address;

    @Override
    public Patient get() {
        return Patient.builder()
                .firstName(getFirstName())
                .lastName(getLastName())
                .gender(getGender())
                .ssn(getSsn())
                .birthDate(getBirthDate())
                .phoneNumber(getPhoneNumber())
                .address(getAddress())
                .build();
    }
}
