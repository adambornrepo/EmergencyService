package com.tech.payload.resource;

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
public class AppointmentExcelResource implements Serializable {

    @ExportToExcel(index = 0, headerText = "APPT ID", width = 3000)
    private Long appointment_id;

    @ExportToExcel(index = 1, headerText = "DOCTOR ID", width = 3000)
    private Long doctorId;

    @ExportToExcel(index = 2, headerText = "DOCTOR FN", width = 5000)
    private String doctorFirstName;

    @ExportToExcel(index = 3, headerText = "DOCTOR LN", width = 5000)
    private String doctorLastName;

    @ExportToExcel(index = 4, headerText = "PATIENT SSN", width = 4000)
    private String patientSsn;

    @ExportToExcel(index = 5, headerText = "PATIENT FN", width = 5000)
    private String patientFirstName;

    @ExportToExcel(index = 6, headerText = "PATIENT LN", width = 5000)
    private String patientLastName;

    @ExportToExcel(index = 7, headerText = "DATE", width = 4000)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @ExportToExcel(index = 9, headerText = "SYMPTOMS", width = 10500)
    private String symptoms;

    @ExportToExcel(index = 8, headerText = "STATUS", width = 3500)
    private AppointmentStatus status;

}
