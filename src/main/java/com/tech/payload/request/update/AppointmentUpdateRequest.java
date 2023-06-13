package com.tech.payload.request.update;

import com.tech.entites.enums.AppointmentStatus;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentUpdateRequest implements Serializable {

    @Positive(message = "{validation.id.negative}")
    private Long doctorId;

    @Size(max = 100, message = "{validation.size.symptoms}")
    private String symptoms;

    private AppointmentStatus status;
}
