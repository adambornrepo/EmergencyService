package com.tech.payload.request.update;

import com.tech.entites.concretes.LabTechnician;
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
public class LabTechUpdateRequest extends EmployeeUpdateRequest implements Serializable, Consumer<LabTechnician> {

    @Override
    public void accept(LabTechnician labTechnician) {
        if (StringUtils.hasText(getFirstName())) labTechnician.setFirstName(GeneralUtils.capitalize(getFirstName()));
        if (StringUtils.hasText(getLastName())) labTechnician.setLastName(getLastName().toUpperCase(Locale.US));
        if (StringUtils.hasText(getPhoneNumber())) labTechnician.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) labTechnician.setPassword(getPassword());
        if (getAddress() != null) labTechnician.setAddress(getAddress());
    }

}
