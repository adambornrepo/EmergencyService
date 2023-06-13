package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.RadiologyTechnician;
import com.tech.entites.enums.Role;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.RadiologyTechnicianMapper;
import com.tech.payload.request.register.RadiologyTechRegistrationRequest;
import com.tech.payload.request.update.RadiologyTechUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedRadiologyTechResponse;
import com.tech.payload.response.simple.SimpleRadiologyTechResponse;
import com.tech.repository.RadiologyTechnicianRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j//logger
@Transactional
@Service
@RequiredArgsConstructor
public class RadiologyTechnicianService {
    private final RadiologyTechnicianRepository radiologyTechRepository;
    private final RadiologyTechnicianMapper radiologyTechMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;

    public ResponseEntity<DetailedRadiologyTechResponse> getOneRadiologyTechByUniqueField(UniqueField searchIn, String value) {
        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        radiologyTechMapper.buildDetailedRadiologyTechResponse(getOneRadiologyTechById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }
        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    radiologyTechMapper.buildDetailedRadiologyTechResponse(
                            radiologyTechRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.rad-tech.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    radiologyTechMapper.buildDetailedRadiologyTechResponse(
                            radiologyTechRepository
                                    .findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.rad-tech.ssn"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };
    }

    public RadiologyTechnician getOneRadiologyTechById(Long id) {
        return radiologyTechRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.rad-tech.id"), id)));
    }

    public Page<SimpleRadiologyTechResponse> getAllRadiologyTech(int page, int size, String sort, String type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).ascending());
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).descending());
        }
        return radiologyTechRepository.findAll(pageable).map(radiologyTechMapper::buildSimpleRadiologyTechResponse);
    }


    public ResponseEntity<DetailedRadiologyTechResponse> saveRadiologyTech(RadiologyTechRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        RadiologyTechnician radiologyTech = request.get();
        radiologyTech.setPassword(radiologyTech.getPassword()); // TODO: 6.06.2023 Encode
        radiologyTech.setRole(Role.RADIOLOGY_TECHNICIAN);
        RadiologyTechnician saved = radiologyTechRepository.save(radiologyTech);
        return new ResponseEntity<>(radiologyTechMapper.buildDetailedRadiologyTechResponse(saved), HttpStatus.CREATED);
    }


    public ResponseEntity<DetailedRadiologyTechResponse> updateRadiologyTech(RadiologyTechUpdateRequest request, Long id) {
        RadiologyTechnician found = getOneRadiologyTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(found.getPassword());// TODO: 6.06.2023 Encode
        RadiologyTechnician updated = radiologyTechRepository.save(found);
        return new ResponseEntity<>(radiologyTechMapper.buildDetailedRadiologyTechResponse(updated), HttpStatus.ACCEPTED);
    }


    public ResponseEntity<ApiResponse> deleteRadiologyTech(Long id) {
        RadiologyTechnician found = getOneRadiologyTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        RadiologyTechnician deleted = radiologyTechRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.rad-tech.delete")).build(),
                HttpStatus.OK
        );
    }
}
