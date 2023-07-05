package com.tech.payload.request.register;

import com.tech.entites.concretes.LabTechnician;
import com.tech.payload.request.register.abstracts.BaseRegistrationRequest;
import com.tech.utils.GeneralUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Locale;
import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LabTechRegistrationRequest extends BaseRegistrationRequest implements Serializable, Supplier<LabTechnician> {


    @Override
    public LabTechnician get() {
        return LabTechnician.builder()
                .firstName(GeneralUtils.capitalize(getFirstName()))
                .lastName(getLastName().toUpperCase(Locale.US))
                .gender(getGender())
                .ssn(getSsn())
                .birthDate(getBirthDate())
                .phoneNumber(getPhoneNumber())
                .address(getAddress())
                .password(getPassword())
                .build();
    }
}
