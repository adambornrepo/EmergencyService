package com.tech.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureCreationRequestForDoctor implements Serializable {

    @NotNull(message = "{validation.null.applied}")
    @Size(min = 2, max = 150, message = "{validation.size.field}")
    private String applied;

    @Positive(message = "{validation.id.negative}")
    @NotNull(message = "{validation.null.appointment.id}")
    private Long appointmentId;

}
