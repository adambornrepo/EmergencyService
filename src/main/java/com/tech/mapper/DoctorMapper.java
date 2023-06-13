package com.tech.mapper;

import com.tech.entites.concretes.Doctor;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.payload.response.simple.SimpleDoctorResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public SimpleDoctorResponse buildSimpleDoctorResponse(Doctor doctor) {
        return SimpleDoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .phoneNumber(doctor.getPhoneNumber())
                .role(doctor.getRole())
                .zone(doctor.getZone())
                .isDisabled(doctor.isDisabled())
                .build();
    }


    public DetailedDoctorResponse buildDetailedDoctorResponse(Doctor doctor) {
        return DetailedDoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .phoneNumber(doctor.getPhoneNumber())
                .role(doctor.getRole())
                .zone(doctor.getZone())
                .isDisabled(doctor.isDisabled())

                .ssn(doctor.getSsn())
                .gender(doctor.getGender())
                .birthDate(doctor.getBirthDate())
                .address(doctor.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(doctor.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(doctor.getUpdatedAt()))
                .build();
    }
}
