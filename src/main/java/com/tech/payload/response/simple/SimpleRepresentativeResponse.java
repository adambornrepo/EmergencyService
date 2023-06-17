package com.tech.payload.response.simple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.security.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleRepresentativeResponse implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Role role;

    private boolean isDisabled;

}
