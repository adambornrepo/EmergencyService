package com.tech.payload.request.register;

import com.tech.entites.concretes.Admin;
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
public class AdminRegistrationRequest extends BaseRegistrationRequest implements Serializable, Supplier<Admin> {


    @Override
    public Admin get() {
        return Admin.builder()
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
