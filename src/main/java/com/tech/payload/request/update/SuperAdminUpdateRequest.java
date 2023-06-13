package com.tech.payload.request.update;

import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.SuperAdmin;
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
public class SuperAdminUpdateRequest extends EmployeeUpdateRequest implements Consumer<SuperAdmin>, Serializable {

    @Override
    public void accept(SuperAdmin superAdmin) {
        if (StringUtils.hasText(getFirstName())) superAdmin.setFirstName(getFirstName());
        if (StringUtils.hasText(getLastName())) superAdmin.setLastName(getLastName());
        if (StringUtils.hasText(getPhoneNumber())) superAdmin.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) superAdmin.setPassword(getPassword());
        if (getAddress() != null) superAdmin.setAddress(getAddress());
    }
}
