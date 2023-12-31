package com.tech.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureCreationRequest implements Serializable {

    @NotNull(message = "{validation.null.doctor.note}")
    @Size(min = 2, max = 150, message = "{validation.size.field}")
    private String doctorNote;

    @Positive(message = "{validation.id.negative}")
    @NotNull(message = "{validation.null.appointment.id}")
    private Long appointmentId;

    @Positive(message = "{validation.id.negative}")
    @NotNull(message = "{validation.null.employee.id}")
    private Long employeeId;
}
