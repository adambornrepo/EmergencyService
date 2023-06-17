package com.tech.payload.request.update.abstracts;

import com.tech.entites.concretes.Address;
import com.tech.payload.annotations.custom.PhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeUpdateRequest {

    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String firstName;

    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String lastName;

    @PhoneNumber
    private String phoneNumber;

    @Valid
    private Address address;

    @Size(min = 8, message = "{validation.size.password}")
    private String password;
}
