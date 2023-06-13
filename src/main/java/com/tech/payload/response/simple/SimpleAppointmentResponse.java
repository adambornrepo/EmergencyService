package com.tech.payload.response.simple;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.concretes.Appointment;
import com.tech.entites.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAppointmentResponse implements Serializable {

    private Long appointment_id;

    private Long doctorId;

    private String doctorFirstName;

    private String doctorLastName;

    private String patientSsn;

    private String patientFirstName;

    private String patientLastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    private String symptoms;

    private AppointmentStatus status;

}
