package com.tech.payload.response.detailed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.concretes.Prescription;
import com.tech.utils.GeneralUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.tech.utils.GeneralUtils.convertMillisToLocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedPrescriptionResponse implements Serializable {

    private Long id;

    private Long appointmentId;

    private Long doctorId;

    private String doctorFirstName;

    private String doctorLastName;

    private String patientSsn;

    private String patientFirstName;

    private String patientLastName;

    private List<String> medicines;

    private boolean isDisabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
