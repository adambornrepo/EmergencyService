package com.tech.mapper;

import com.tech.entites.concretes.Patient;
import com.tech.payload.response.detailed.DetailedPatientResponse;
import com.tech.payload.response.simple.SimplePatientResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public SimplePatientResponse buildSimplePatientResponse(Patient patient) {
        return SimplePatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phoneNumber(patient.getPhoneNumber())
                .role(patient.getRole())
                .build();
    }


    public DetailedPatientResponse buildDetailedPatientResponse(Patient patient) {
        return DetailedPatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phoneNumber(patient.getPhoneNumber())
                .role(patient.getRole())

                .ssn(patient.getSsn())
                .gender(patient.getGender())
                .birthDate(patient.getBirthDate())
                .address(patient.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(patient.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(patient.getUpdatedAt()))
                .build();
    }

}
