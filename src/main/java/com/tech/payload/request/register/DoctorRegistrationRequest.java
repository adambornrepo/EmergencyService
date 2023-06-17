package com.tech.payload.request.register;

import com.tech.entites.concretes.Doctor;
import com.tech.entites.enums.Zone;
import com.tech.payload.request.register.abstracts.BaseRegistrationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DoctorRegistrationRequest extends BaseRegistrationRequest implements Serializable, Supplier<Doctor> {

    @NotNull(message = "{validation.null.zone}")
    private Zone zone;


    @Override
    public Doctor get() {
        return Doctor.builder()
                .firstName(getFirstName())
                .lastName(getLastName())
                .gender(getGender())
                .ssn(getSsn())
                .birthDate(getBirthDate())
                .phoneNumber(getPhoneNumber())
                .address(getAddress())
                .zone(getZone())
                .password(getPassword())
                .build();
    }
}
