package com.tech.mapper;

import com.tech.entites.concretes.Representative;
import com.tech.payload.response.detailed.DetailedRepresentativeResponse;
import com.tech.payload.response.simple.SimpleRepresentativeResponse;
import com.tech.utils.GeneralUtils;
import org.springframework.stereotype.Component;

@Component
public class RepresentativeMapper {
    public SimpleRepresentativeResponse buildSimpleRepresentativeResponse(Representative representative) {
        return SimpleRepresentativeResponse.builder()
                .id(representative.getId())
                .firstName(representative.getFirstName())
                .lastName(representative.getLastName())
                .phoneNumber(representative.getPhoneNumber())
                .role(representative.getRole())
                .isDisabled(representative.isDisabled())
                .build();
    }


    public DetailedRepresentativeResponse buildDetailedRepresentativeResponse(Representative representative) {
        return DetailedRepresentativeResponse.builder()
                .id(representative.getId())
                .firstName(representative.getFirstName())
                .lastName(representative.getLastName())
                .phoneNumber(representative.getPhoneNumber())
                .role(representative.getRole())
                .isDisabled(representative.isDisabled())

                .ssn(representative.getSsn())
                .gender(representative.getGender())
                .birthDate(representative.getBirthDate())
                .address(representative.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(representative.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(representative.getUpdatedAt()))
                .build();
    }
}
