package com.tech.payload.request.register;

import com.tech.entites.concretes.RadiologyTechnician;
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
public class RadiologyTechRegistrationRequest extends BaseRegistrationRequest implements Serializable, Supplier<RadiologyTechnician> {

    @Override
    public RadiologyTechnician get() {
        return RadiologyTechnician.builder()
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
