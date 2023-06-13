package com.tech.mapper;

import com.tech.entites.concretes.RadiologyTechnician;
import com.tech.payload.response.detailed.DetailedRadiologyTechResponse;
import com.tech.payload.response.simple.SimpleRadiologyTechResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class RadiologyTechnicianMapper {

    public SimpleRadiologyTechResponse buildSimpleRadiologyTechResponse(RadiologyTechnician radiologyTechnician) {
        return SimpleRadiologyTechResponse.builder()
                .id(radiologyTechnician.getId())
                .firstName(radiologyTechnician.getFirstName())
                .lastName(radiologyTechnician.getLastName())
                .phoneNumber(radiologyTechnician.getPhoneNumber())
                .role(radiologyTechnician.getRole())
                .isDisabled(radiologyTechnician.isDisabled())
                .build();
    }


    public DetailedRadiologyTechResponse buildDetailedRadiologyTechResponse(RadiologyTechnician radiologyTechnician) {
        return DetailedRadiologyTechResponse.builder()
                .id(radiologyTechnician.getId())
                .firstName(radiologyTechnician.getFirstName())
                .lastName(radiologyTechnician.getLastName())
                .phoneNumber(radiologyTechnician.getPhoneNumber())
                .role(radiologyTechnician.getRole())
                .isDisabled(radiologyTechnician.isDisabled())

                .ssn(radiologyTechnician.getSsn())
                .gender(radiologyTechnician.getGender())
                .birthDate(radiologyTechnician.getBirthDate())
                .address(radiologyTechnician.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(radiologyTechnician.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(radiologyTechnician.getUpdatedAt()))
                .build();
    }
}
