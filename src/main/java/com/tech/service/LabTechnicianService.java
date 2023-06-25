package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.LabTechnician;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.ForbiddenAccessException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.LabTechnicianMapper;
import com.tech.payload.request.register.LabTechRegistrationRequest;
import com.tech.payload.request.update.LabTechUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedLabTechResponse;
import com.tech.payload.response.simple.SimpleLabTechResponse;
import com.tech.repository.LabTechnicianRepository;
import com.tech.security.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
public class LabTechnicianService {


    private final LabTechnicianRepository labTechnicianRepository;
    private final LabTechnicianMapper labTechMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;
    private final PasswordEncoder passwordEncoder;

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
    @CacheEvict(cacheNames = {"activeEmployees", "activeLabTechnicians"}, allEntries = true)
    public ResponseEntity<DetailedLabTechResponse> saveLabTech(LabTechRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        LabTechnician labTech = request.get();
        labTech.setPassword(passwordEncoder.encode(labTech.getPassword()));
        labTech.setRole(Role.LAB_TECHNICIAN);
        LabTechnician saved = labTechnicianRepository.save(labTech);
        log.info("Laboratory Technician saved: {}", saved);
        return new ResponseEntity<>(labTechMapper.buildDetailedLabTechResponse(saved), HttpStatus.CREATED);
    }
    @CacheEvict(cacheNames = {"activeEmployees", "activeLabTechnicians"}, allEntries = true)
    public ResponseEntity<DetailedLabTechResponse> updateLabTech(LabTechUpdateRequest request, Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.LAB_TECHNICIAN) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.lab-tech.update"));
        }

        LabTechnician found = getOneLabTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(passwordEncoder.encode(found.getPassword()));
        LabTechnician updated = labTechnicianRepository.save(found);
        log.info("Laboratory Technician updated: {}", updated);
        return new ResponseEntity<>(labTechMapper.buildDetailedLabTechResponse(updated), HttpStatus.ACCEPTED);
    }
    @CacheEvict(cacheNames = {"activeEmployees", "activeLabTechnicians"}, allEntries = true)
    public ResponseEntity<ApiResponse> deleteLabTech(Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.LAB_TECHNICIAN) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.lab-tech.delete"));
        }

        LabTechnician found = getOneLabTechById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        LabTechnician deleted = labTechnicianRepository.save(found);
        log.info("Laboratory Technician deleted: {}", deleted);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.lab-tech.delete")).build(),
                HttpStatus.OK
        );
    }
    @Cacheable(value = "activeLabTechnicians")
    public List<SimpleLabTechResponse> getAllActiveLabTech() {
        return labTechnicianRepository.findByIsDisabled(false)
                .stream()
                .map(labTechMapper::buildSimpleLabTechResponse)
                .collect(Collectors.toList());
    }
}
