package com.tech.payload.request.update;

import com.tech.entites.concretes.SuperAdmin;
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
public class SuperAdminUpdateRequest extends EmployeeUpdateRequest implements Consumer<SuperAdmin>, Serializable {

    @Override
    public void accept(SuperAdmin superAdmin) {
        if (StringUtils.hasText(getFirstName())) superAdmin.setFirstName(GeneralUtils.capitalize(getFirstName()));
        if (StringUtils.hasText(getLastName())) superAdmin.setLastName(getLastName().toUpperCase(Locale.US));
        if (StringUtils.hasText(getPhoneNumber())) superAdmin.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) superAdmin.setPassword(getPassword());
        if (getAddress() != null) superAdmin.setAddress(getAddress());
    }
}
