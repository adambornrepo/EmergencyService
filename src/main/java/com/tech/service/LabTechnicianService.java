package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.LabTechnician;
import com.tech.entites.enums.Role;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.LabTechnicianMapper;
import com.tech.payload.request.register.LabTechRegistrationRequest;
import com.tech.payload.request.update.LabTechUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedLabTechResponse;
import com.tech.payload.response.simple.SimpleLabTechResponse;
import com.tech.repository.LabTechnicianRepository;
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
public class LabTechnicianService {


    private final LabTechnicianRepository labTechnicianRepository;
    private final LabTechnicianMapper labTechMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;

    public ResponseEntity<DetailedLabTechResponse> getOneLabTechByUniqueField(UniqueField searchIn, String value) {
        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        labTechMapper.buildDetailedLabTechResponse(getOneLabTechById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }
        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    labTechMapper.buildDetailedLabTechResponse(
                            labTechnicianRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.lab-tech.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    labTechMapper.buildDetailedLabTechResponse(
                            labTechnicianRepository
                                    .findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.lab-tech.ssn"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };
    }

    public LabTechnician getOneLabTechById(Long id) {
        return labTechnicianRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.lab-tech.id"), id)));
    }

    public Page<SimpleLabTechResponse> getAllLabTech(int page, int size, String sort, String type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).ascending());
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).descending());
        }
        return labTechnicianRepository.findAll(pageable).map(labTechMapper::buildSimpleLabTechResponse);
    }

    public ResponseEntity<DetailedLabTechResponse> saveLabTech(LabTechRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        LabTechnician labTech = request.get();
        labTech.setPassword(labTech.getPassword()); // TODO: 6.06.2023 Encode
        labTech.setRole(Role.LAB_TECHNICIAN);
        LabTechnician saved = labTechnicianRepository.save(labTech);
        return new ResponseEntity<>(labTechMapper.buildDetailedLabTechResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedLabTechResponse> updateLabTech(LabTechUpdateRequest request, Long id) {
        LabTechnician found = getOneLabTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(found.getPassword());// TODO: 6.06.2023 Encode
        LabTechnician updated = labTechnicianRepository.save(found);
        return new ResponseEntity<>(labTechMapper.buildDetailedLabTechResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteLabTech(Long id) {
        LabTechnician found = getOneLabTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        LabTechnician deleted = labTechnicianRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.lab-tech.delete")).build(),
                HttpStatus.OK
        );
    }


}
