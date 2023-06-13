package com.tech.mapper;

import com.tech.entites.concretes.Admin;
import com.tech.payload.response.detailed.DetailedAdminResponse;
import com.tech.payload.response.simple.SimpleAdminResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public SimpleAdminResponse buildSimpleAdminResponse(Admin admin) {
        return SimpleAdminResponse.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phoneNumber(admin.getPhoneNumber())
                .role(admin.getRole())
                .isDisabled(admin.isDisabled())
                .build();
    }


    public DetailedAdminResponse buildDetailedAdminResponse(Admin admin) {
        return DetailedAdminResponse.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .phoneNumber(admin.getPhoneNumber())
                .role(admin.getRole())
                .isDisabled(admin.isDisabled())

                .ssn(admin.getSsn())
                .gender(admin.getGender())
                .birthDate(admin.getBirthDate())
                .address(admin.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(admin.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(admin.getUpdatedAt()))
                .build();
    }
}
