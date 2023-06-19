package com.tech.payload.response.simple;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.enums.AppointmentStatus;
import com.tech.payload.annotations.custom.ExportToExcel;
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

    @ExportToExcel(index = 0, text = "APPT ID", width = 3000)
    private Long appointment_id;

    @ExportToExcel(index = 1, text = "DOCTOR ID", width = 3000)
    private Long doctorId;

    @ExportToExcel(index = 2, text = "DOCTOR FN", width = 5000)
    private String doctorFirstName;

    @ExportToExcel(index = 3, text = "DOCTOR LN", width = 5000)
    private String doctorLastName;

    @ExportToExcel(index = 4, text = "PATIENT SSN", width = 4000)
    private String patientSsn;

    @ExportToExcel(index = 5, text = "PATIENT FN", width = 5000)
    private String patientFirstName;

    @ExportToExcel(index = 6, text = "PATIENT LN", width = 5000)
    private String patientLastName;

    @ExportToExcel(index = 7, text = "DATE", width = 4000)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @ExportToExcel(index = 9, text = "SYMPTOMS", width = 10500)
    private String symptoms;

    @ExportToExcel(index = 8, text = "STATUS", width = 3500)
    private AppointmentStatus status;

}
