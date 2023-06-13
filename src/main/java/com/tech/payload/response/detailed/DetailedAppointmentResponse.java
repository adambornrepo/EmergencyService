package com.tech.payload.response.detailed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.concretes.Appointment;
import com.tech.entites.concretes.Procedure;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedAppointmentResponse {

    private Long appointment_id;

    private Long representativeId;

    private Long doctorId;

    private String doctorFirstName;

    private String doctorLastName;

    private String patientSsn;

    private String patientFirstName;

    private String patientLastName;

    private String symptoms;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    private Map<Long, String> procedures;

    private Long prescriptionId;

    private List<String> medicines;

    private AppointmentStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
