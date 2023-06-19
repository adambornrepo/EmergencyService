package com.tech.payload.request.update;

import com.tech.entites.concretes.Nurse;
import com.tech.entites.enums.Zone;
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
public class NurseUpdateRequest extends EmployeeUpdateRequest implements Serializable, Consumer<Nurse> {

    @Override
    public void accept(Nurse nurse) {
        if (StringUtils.hasText(getFirstName())) nurse.setFirstName(getFirstName());
        if (StringUtils.hasText(getLastName())) nurse.setLastName(getLastName());
        if (StringUtils.hasText(getPhoneNumber())) nurse.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) nurse.setPassword(getPassword());
        if (getAddress() != null) nurse.setAddress(getAddress());
    }

}
