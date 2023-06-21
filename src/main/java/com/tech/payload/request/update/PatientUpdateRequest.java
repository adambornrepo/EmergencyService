package com.tech.payload.request.update;

import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.Patient;
import com.tech.payload.annotations.custom.PhoneNumber;
import com.tech.utils.GeneralUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Locale;
import java.util.function.Consumer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientUpdateRequest implements Serializable, Consumer<Patient> {

    @Size(min = 2, max = 50, message = "{validation.size.field}")
    private String firstName;

    @Size(min = 2, max = 50, message = "{validation.size.field}")
    private String lastName;

    @PhoneNumber
    private String phoneNumber;

    @Email(message = "{validation.email}")
    private String email;

    @Valid
    private Address address;

    @Override
    public void accept(Patient patient) {
        if (StringUtils.hasText(firstName)) patient.setFirstName(GeneralUtils.capitalize(getFirstName()));
        if (StringUtils.hasText(lastName)) patient.setLastName(getLastName().toUpperCase(Locale.US));
        if (StringUtils.hasText(phoneNumber)) patient.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(email)) patient.setEmail(getEmail().toLowerCase(Locale.US));
        if (address != null) patient.setAddress(getAddress());
    }

}
