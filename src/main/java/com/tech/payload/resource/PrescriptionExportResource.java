package com.tech.payload.resource;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionExportResource implements Serializable {

    private String doctorFirstName;
    private String doctorLastName;
    private String patientSSN;
    private String patientFirstName;
    private String patientLastName;
    private String patientAge;
    private String patientGender;
    private String date;
    private String patientPhoneNum;
    private String patientAddress;
    private List<String> medicines;
    private String fileName;
    private String patientEmail;

}
