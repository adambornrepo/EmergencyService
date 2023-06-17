package com.tech.payload.request;

import com.tech.payload.annotations.custom.SSN;
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

    @SSN
    @NotNull(message = "{validation.null.ssn}")
    private String ssn;

    @Size(min = 8, message = "{validation.size.password}")
    @NotNull(message = "{validation.null.password}")
    private String password;
}
