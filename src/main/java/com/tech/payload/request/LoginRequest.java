package com.tech.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest implements Serializable {

    // TODO: 6.06.2023 validation regex
    @NotNull(message = "{validation.null.ssn}")
    private String ssn;

    // TODO: 6.06.2023 validation regex
    @NotNull(message = "{validation.null.password}")
    private String password;
}
