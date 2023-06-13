package com.tech.payload.request.update;

import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.RadiologyTechnician;
import com.tech.payload.request.update.abstracts.EmployeeUpdateRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RadiologyTechUpdateRequest extends EmployeeUpdateRequest implements Serializable, Consumer<RadiologyTechnician> {

    @Override
    public void accept(RadiologyTechnician radiologyTechnician) {
        if (StringUtils.hasText(getFirstName())) radiologyTechnician.setFirstName(getFirstName());
        if (StringUtils.hasText(getLastName())) radiologyTechnician.setLastName(getLastName());
        if (StringUtils.hasText(getPhoneNumber())) radiologyTechnician.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) radiologyTechnician.setPassword(getPassword());
        if (getAddress() != null) radiologyTechnician.setAddress(getAddress());
    }
}
