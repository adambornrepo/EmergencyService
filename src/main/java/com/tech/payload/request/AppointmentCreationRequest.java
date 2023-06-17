package com.tech.payload.request;

import com.tech.payload.annotations.custom.SSN;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class AppointmentCreationRequest implements Serializable {

    @Positive(message = "{validation.id.negative}")
    @NotNull(message = "{validation.null.psr.id}")
    private Long representativeId;

    @Positive(message = "{validation.id.negative}")
    @NotNull(message = "{validation.null.doctor.id}")
    private Long doctorId;

    @SSN
    @NotNull(message = "{validation.null.ssn}")
    private String patientSsn;

    @Size(max = 100, message = "{validation.size.symptoms}")
    private String symptoms;

}
