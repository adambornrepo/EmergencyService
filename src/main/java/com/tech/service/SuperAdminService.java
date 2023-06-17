package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.SuperAdmin;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.payload.request.register.SuperAdminRegistrationRequest;
import com.tech.payload.request.update.SuperAdminUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedSuperAdminResponse;
import com.tech.repository.SuperAdminRepository;
import com.tech.security.role.Role;
import com.tech.utils.GeneralUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j//logger
@Transactional
@Service
@RequiredArgsConstructor
public class SuperAdminService {
    private final SuperAdminRepository superAdminRepository;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<DetailedSuperAdminResponse> getOneSuperAdminByUniqueField(UniqueField searchIn, String value) {
        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        buildDetailedSuperAdminResponse(getOneSuperAdminById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }
        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    buildDetailedSuperAdminResponse(
                            superAdminRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.super-admin.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    buildDetailedSuperAdminResponse(
                            superAdminRepository
                                    .findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.super-admin.ssn"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };
    }

    public SuperAdmin getOneSuperAdminById(Long id) {
        return superAdminRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.super-admin.id"), id)));
    }

    public ResponseEntity<DetailedSuperAdminResponse> saveSuperAdmin(SuperAdminRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        SuperAdmin superAdmin = request.get();
        superAdmin.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
        superAdmin.setRole(Role.SUPER_ADMIN);
        SuperAdmin saved = superAdminRepository.save(superAdmin);
        return new ResponseEntity<>(buildDetailedSuperAdminResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedSuperAdminResponse> updateSuperAdmin(SuperAdminUpdateRequest request, Long id) {
        SuperAdmin found = getOneSuperAdminById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(passwordEncoder.encode(found.getPassword()));
        SuperAdmin updated = superAdminRepository.save(found);
        return new ResponseEntity<>(buildDetailedSuperAdminResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteSuperAdmin(Long id) {
        SuperAdmin found = getOneSuperAdminById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (superAdminRepository.isLastSuperAdmin()) {
            throw new UnsuitableRequestException(apiMessages.getMessage("error.last.super-admin"));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        SuperAdmin deleted = superAdminRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.super-admin.delete")).build(),
                HttpStatus.OK
        );
    }
    public boolean isExistsAtLeastOneSuperAdmin(){
        return superAdminRepository.checkSuperAdminExistence();
    }

    private DetailedSuperAdminResponse buildDetailedSuperAdminResponse(SuperAdmin superAdmin) {
        return DetailedSuperAdminResponse.builder()
                .id(superAdmin.getId())
                .firstName(superAdmin.getFirstName())
                .lastName(superAdmin.getLastName())
                .phoneNumber(superAdmin.getPhoneNumber())
                .role(superAdmin.getRole())
                .isDisabled(superAdmin.isDisabled())

                .ssn(superAdmin.getSsn())
                .gender(superAdmin.getGender())
                .birthDate(superAdmin.getBirthDate())
                .address(superAdmin.getAddress())
                .createdAt(GeneralUtils.convertMillisToLocalDateTime(superAdmin.getCreatedAt()))
                .updatedAt(GeneralUtils.convertMillisToLocalDateTime(superAdmin.getUpdatedAt()))
                .build();
    }

}
