package com.tech.mapper;

import com.tech.entites.concretes.Nurse;
import com.tech.payload.response.detailed.DetailedNurseResponse;
import com.tech.payload.response.simple.SimpleNurseResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class NurseMapper {


    public SimpleNurseResponse buildSimpleNurseResponse(Nurse nurse) {
        return SimpleNurseResponse.builder()
                .id(nurse.getId())
                .firstName(nurse.getFirstName())
                .lastName(nurse.getLastName())
                .phoneNumber(nurse.getPhoneNumber())
                .role(nurse.getRole())
                .zone(nurse.getZone())
                .isDisabled(nurse.isDisabled())
                .build();
    }


    public DetailedNurseResponse buildDetailedNurseResponse(Nurse nurse) {
        return DetailedNurseResponse.builder()
                .id(nurse.getId())
                .firstName(nurse.getFirstName())
                .lastName(nurse.getLastName())
                .phoneNumber(nurse.getPhoneNumber())
                .role(nurse.getRole())
                .zone(nurse.getZone())
                .isDisabled(nurse.isDisabled())

                .ssn(nurse.getSsn())
                .gender(nurse.getGender())
                .birthDate(nurse.getBirthDate())
                .address(nurse.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(nurse.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(nurse.getUpdatedAt()))
                .build();
    }

}
