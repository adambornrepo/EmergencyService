package com.tech.payload.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.payload.annotations.custom.ExportToExcel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcedureExcelResource implements Serializable {

    @ExportToExcel(index = 0, headerText = "PROC ID", width = 3000)
    private Long id;

    @ExportToExcel(index = 1, headerText = "DR NOTE", width = 12500)
    private String doctorNote;

    @ExportToExcel(index = 2, headerText = "APPLIED", width = 12500)
    private String applied;

    @ExportToExcel(index = 3, headerText = "APPT ID", width = 3000)
    private Long appointmentId;

    @ExportToExcel(index = 4, headerText = "PERSONAL ID", width = 4000)
    private Long employeeId;

    @ExportToExcel(index = 5, headerText = "FIRSTNAME", width = 5000)
    private String employeeFirstName;

    @ExportToExcel(index = 6, headerText = "LASTNAME", width = 5000)
    private String employeeLastName;

    @ExportToExcel(index = 7, headerText = "DELETED", width = 3000)
    private boolean isDisabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
