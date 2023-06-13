package com.tech.mapper;

import com.tech.entites.concretes.LabTechnician;
import com.tech.payload.response.detailed.DetailedLabTechResponse;
import com.tech.payload.response.simple.SimpleLabTechResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class LabTechnicianMapper {

    public SimpleLabTechResponse buildSimpleLabTechResponse(LabTechnician labTechnician) {
        return SimpleLabTechResponse.builder()
                .id(labTechnician.getId())
                .firstName(labTechnician.getFirstName())
                .lastName(labTechnician.getLastName())
                .phoneNumber(labTechnician.getPhoneNumber())
                .role(labTechnician.getRole())
                .isDisabled(labTechnician.isDisabled())
                .build();
    }


    public DetailedLabTechResponse buildDetailedLabTechResponse(LabTechnician labTechnician) {
        return DetailedLabTechResponse.builder()
                .id(labTechnician.getId())
                .firstName(labTechnician.getFirstName())
                .lastName(labTechnician.getLastName())
                .phoneNumber(labTechnician.getPhoneNumber())
                .role(labTechnician.getRole())
                .isDisabled(labTechnician.isDisabled())

                .ssn(labTechnician.getSsn())
                .gender(labTechnician.getGender())
                .birthDate(labTechnician.getBirthDate())
                .address(labTechnician.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(labTechnician.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(labTechnician.getUpdatedAt()))
                .build();
    }
}
