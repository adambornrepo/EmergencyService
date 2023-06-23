package com.tech.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ProcedureResponse implements Serializable {

    private Long id;

    private String doctorNote;

    private String applied;

    private Long appointmentId;

    private Long employeeId;

    private String employeeFirstName;

    private String employeeLastName;

    private boolean isDisabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
