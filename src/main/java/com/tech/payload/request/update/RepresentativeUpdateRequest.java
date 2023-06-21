package com.tech.payload.request.update;

import com.tech.entites.concretes.Address;
import com.tech.entites.concretes.Representative;
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
public class RepresentativeUpdateRequest extends EmployeeUpdateRequest implements Serializable, Consumer<Representative> {

    @Override
    public void accept(Representative representative) {
        if (StringUtils.hasText(getFirstName())) representative.setFirstName(GeneralUtils.capitalize(getFirstName()));
        if (StringUtils.hasText(getLastName())) representative.setLastName(getLastName().toUpperCase(Locale.US));
        if (StringUtils.hasText(getPhoneNumber())) representative.setPhoneNumber(getPhoneNumber());
        if (StringUtils.hasText(getPassword())) representative.setPassword(getPassword());
        if (getAddress() != null) representative.setAddress(getAddress());
    }


}
