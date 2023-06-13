package com.tech.payload.request.register.abstracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tech.entites.concretes.Address;
import com.tech.entites.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseRegistrationRequest implements Serializable {

    @NotNull(message = "{validation.null.first-name}")
    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String firstName;

    @NotNull(message = "{validation.null.last-name}")
    @Size(min = 2,max = 50,message = "{validation.size.field}")
    private String lastName;

    @JsonFormat(
            pattern = "yyyy-MM-dd",
            shape = JsonFormat.Shape.STRING
    )
    @Past(message = "{validation.date.past}")
    @NotNull(message = "{validation.null.birthdate}")
    private LocalDate birthDate;

    @NotNull(message = "{validation.null.gender}")
    private Gender gender;

    // TODO: 6.06.2023 validation regex
    @NotNull(message = "{validation.null.ssn}")
    private String ssn;

    // TODO: 6.06.2023 validation regex
    @NotNull(message = "{validation.null.phone}")
    private String phoneNumber;

    @Valid
    private Address address;

    // TODO: 6.06.2023 validation regex
    @NotNull(message = "{validation.null.password}")
    private String password;

}
