package com.tech.payload.request.update;

import com.tech.entites.concretes.Admin;
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
public class AdminUpdateRequest extends EmployeeUpdateRequest implements Serializable, Consumer<Admin> {

    @Override
    public void accept(Admin admin) {
        if (StringUtils.hasText(getFirstName())) admin.setFirstName(GeneralUtils.capitalize(getFirstName()));
        if (StringUtils.hasText(getLastName())) admin.setLastName(getLastName().toUpperCase(Locale.US));
        if (StringUtils.hasText(getPhoneNumber())) admin.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) admin.setPassword(getPassword());
        if (getAddress() != null) admin.setAddress(getAddress());
    }
}
