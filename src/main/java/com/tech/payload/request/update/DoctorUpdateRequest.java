package com.tech.payload.request.update;

import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.enums.Zone;
import com.tech.payload.request.update.abstracts.EmployeeUpdateRequest;
import com.tech.utils.GeneralUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Locale;
import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class DoctorUpdateRequest extends EmployeeUpdateRequest implements Serializable, Consumer<Doctor> {

    @Override
    public void accept(Doctor doctor) {
        if (StringUtils.hasText(getFirstName())) doctor.setFirstName(GeneralUtils.capitalize(getFirstName()));
        if (StringUtils.hasText(getLastName())) doctor.setLastName(getLastName().toUpperCase(Locale.US));
        if (StringUtils.hasText(getPhoneNumber())) doctor.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) doctor.setPassword(getPassword());
        if (getAddress() != null) doctor.setAddress(getAddress());
    }
}
