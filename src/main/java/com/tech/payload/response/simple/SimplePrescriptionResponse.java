package com.tech.payload.response.simple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tech.entites.concretes.Prescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimplePrescriptionResponse implements Serializable {

    private Long id;

    private Long appointmentId;

    private Long doctorId;

    private String doctorFirstName;

    private String doctorLastName;

    private String patientSsn;

}
