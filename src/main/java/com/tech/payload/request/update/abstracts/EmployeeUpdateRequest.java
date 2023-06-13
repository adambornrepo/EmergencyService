package com.tech.payload.request.update.abstracts;

import com.tech.entites.concretes.Address;
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

    // TODO: 6.06.2023 validation regex
    private String phoneNumber;

    @Valid
    private Address address;

    // TODO: 6.06.2023 validation regex
    private String password;
}
