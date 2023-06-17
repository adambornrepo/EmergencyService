package com.tech.payload.request.update;

import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.Patient;
import com.tech.payload.annotations.custom.PhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.function.Consumer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientUpdateRequest implements Serializable, Consumer<Patient> {

    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String firstName;

    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String lastName;

    @PhoneNumber
    private String phoneNumber;

    @Valid
    private Address address;

    @Override
    public void accept(Patient patient) {
        if (StringUtils.hasText(firstName)) patient.setFirstName(getFirstName());
        if (StringUtils.hasText(lastName)) patient.setLastName(getLastName());
        if (StringUtils.hasText(phoneNumber)) patient.setPhoneNumber(getPhoneNumber());
        if (address != null) patient.setAddress(getAddress());
    }

}
