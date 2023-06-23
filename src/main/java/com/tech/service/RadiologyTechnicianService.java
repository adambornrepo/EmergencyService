package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.RadiologyTechnician;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.ForbiddenAccessException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.RadiologyTechnicianMapper;
import com.tech.payload.request.register.RadiologyTechRegistrationRequest;
import com.tech.payload.request.update.RadiologyTechUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedRadiologyTechResponse;
import com.tech.payload.response.simple.SimpleRadiologyTechResponse;
import com.tech.repository.RadiologyTechnicianRepository;
import com.tech.security.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j//logger
@Transactional
@Service
@RequiredArgsConstructor
public class RadiologyTechnicianService {
    private final RadiologyTechnicianRepository radiologyTechRepository;
    private final RadiologyTechnicianMapper radiologyTechMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;
    private final PasswordEncoder passwordEncoder;

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
        radiologyTech.setPassword(passwordEncoder.encode(radiologyTech.getPassword()));
        radiologyTech.setRole(Role.RADIOLOGY_TECHNICIAN);
        RadiologyTechnician saved = radiologyTechRepository.save(radiologyTech);
        log.info("Radiology Technician saved: {}", saved);
        return new ResponseEntity<>(radiologyTechMapper.buildDetailedRadiologyTechResponse(saved), HttpStatus.CREATED);
    }


    public ResponseEntity<DetailedRadiologyTechResponse> updateRadiologyTech(RadiologyTechUpdateRequest request, Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.RADIOLOGY_TECHNICIAN) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.rad-tech.update"));
        }

        RadiologyTechnician found = getOneRadiologyTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(passwordEncoder.encode(found.getPassword()));
        RadiologyTechnician updated = radiologyTechRepository.save(found);
        log.info("Radiology Technician updated: {}", updated);
        return new ResponseEntity<>(radiologyTechMapper.buildDetailedRadiologyTechResponse(updated), HttpStatus.ACCEPTED);
    }


    public ResponseEntity<ApiResponse> deleteRadiologyTech(Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.RADIOLOGY_TECHNICIAN) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.rad-tech.delete"));
        }

        RadiologyTechnician found = getOneRadiologyTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        RadiologyTechnician deleted = radiologyTechRepository.save(found);
        log.info("Radiology Technician deleted: {}", deleted);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.rad-tech.delete")).build(),
                HttpStatus.OK
        );
    }

    public List<SimpleRadiologyTechResponse> getAllActiveRadiologyTech() {
        return radiologyTechRepository.findByIsDisabled(false)
                .stream()
                .map(radiologyTechMapper::buildSimpleRadiologyTechResponse)
                .collect(Collectors.toList());
    }
}
