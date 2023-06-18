package com.tech.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionCreationRequest implements Serializable {

    @Positive(message = "{validation.id.negative}")
    @NotNull(message = "{validation.null.appointment.id}")
    private Long appointmentId;

    @NotNull(message = "{validation.null.medicine}")
    @Size(min = 1, message = "{validation.size.medicine}")
    private List<String> medicines;




}
