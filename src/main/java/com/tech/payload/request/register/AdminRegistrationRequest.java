package com.tech.payload.request.register;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.Admin;
import com.tech.entites.enums.Gender;
import com.tech.payload.request.register.abstracts.BaseRegistrationRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AdminRegistrationRequest extends BaseRegistrationRequest implements Serializable, Supplier<Admin> {


    @Override
    public Admin get() {
        return Admin.builder()
                .firstName(getFirstName())
                .lastName(getLastName())
                .gender(getGender())
                .ssn(getSsn())
                .birthDate(getBirthDate())
                .phoneNumber(getPhoneNumber())
                .address(getAddress())
                .password(getPassword())
                .build();
    }

}