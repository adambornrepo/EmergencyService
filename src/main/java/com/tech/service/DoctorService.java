package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.enums.UniqueField;
import com.tech.entites.enums.Zone;
import com.tech.exception.custom.ForbiddenAccessException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.DoctorMapper;
import com.tech.payload.request.register.DoctorRegistrationRequest;
import com.tech.payload.request.update.DoctorUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.payload.response.simple.SimpleDoctorResponse;
import com.tech.repository.DoctorRepository;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<DetailedDoctorResponse> getOneDoctorByUniqueField(UniqueField searchIn, String value) {

        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        doctorMapper.buildDetailedDoctorResponse(getOneDoctorById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }

        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    doctorMapper.buildDetailedDoctorResponse(
                            doctorRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.doctor.phone"), value)
                                    ))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    doctorMapper.buildDetailedDoctorResponse(
                            doctorRepository.findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.doctor.ssn"), value)
                                    ))
                    ),
                    HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };

    }

    public Doctor getOneDoctorById(Long id) {
        return doctorRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.doctor.id"), id)));
    }

    public Page<SimpleDoctorResponse> getAllDoctor(int page, int size, String sort, String type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).ascending());
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).descending());
        }
        return doctorRepository.findAll(pageable).map(doctorMapper::buildSimpleDoctorResponse);
    }

    public ResponseEntity<DetailedDoctorResponse> saveDoctor(DoctorRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        Doctor doctor = request.get();
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setRole(Role.DOCTOR);
        Doctor saved = doctorRepository.save(doctor);
        log.info("Doctor saved: {}", saved);
        return new ResponseEntity<>(doctorMapper.buildDetailedDoctorResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedDoctorResponse> updateDoctor(DoctorUpdateRequest request, Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if ((employee.getRole().equals(Role.DOCTOR) || employee.getRole().equals(Role.CHIEF)) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.doctor.update"));
        }

        Doctor found = getOneDoctorById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(passwordEncoder.encode(found.getPassword()));
        Doctor updated = doctorRepository.save(found);
        log.info("Doctor updated: {}", updated);
        return new ResponseEntity<>(doctorMapper.buildDetailedDoctorResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteDoctor(Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.DOCTOR) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.doctor.delete"));
        }

        Doctor found = getOneDoctorById(id);
        if (found.getRole().equals(Role.CHIEF)) {
            found.setRole(Role.DOCTOR);
        }

        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        Doctor deleted = doctorRepository.save(found);
        log.info("Doctor deleted: {}", deleted);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.doctor.delete")).build(),
                HttpStatus.OK
        );
    }

    public ResponseEntity<DetailedDoctorResponse> assignChiefPhysician(Long id) {
        Doctor doctor = getOneDoctorById(id);
        Doctor chiefPhysician = getChiefPhysician();
        if (chiefPhysician == null) {
            doctor.setRole(Role.CHIEF);
            doctor.setZone(Zone.NONE);
            Doctor assigned = doctorRepository.save(doctor);
            return new ResponseEntity<>(doctorMapper.buildDetailedDoctorResponse(assigned), HttpStatus.ACCEPTED);
        }
        chiefPhysician.setRole(Role.DOCTOR);
        chiefPhysician.setZone(doctor.getZone());
        Doctor formerChief = doctorRepository.save(chiefPhysician);
        doctor.setRole(Role.CHIEF);
        doctor.setZone(Zone.NONE);
        Doctor assigned = doctorRepository.save(doctor);
        log.warn("Doctor assigned as chief physician: {}", assigned);
        return ResponseEntity.ok(doctorMapper.buildDetailedDoctorResponse(assigned));
    }

    private Doctor getChiefPhysician() {
        return doctorRepository.findByRole(Role.CHIEF).orElse(null);
    }


    public List<SimpleDoctorResponse> getAllActiveDoctor(Zone zone) {
        if (zone == null) {
            return doctorRepository.findByIsDisabledOrderByFirstNameAsc(false)
                    .stream()
                    .map(doctorMapper::buildSimpleDoctorResponse)
                    .collect(Collectors.toList());
        } else {
            return doctorRepository.findByIsDisabledAndZoneOrderByZoneAscFirstNameAsc(false, zone)
                    .stream()
                    .map(doctorMapper::buildSimpleDoctorResponse)
                    .collect(Collectors.toList());
        }
    }

    public ResponseEntity<DetailedDoctorResponse> updateDoctorZone(Zone zone, Long id) {
        Doctor found = getOneDoctorById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        found.setZone(zone);
        Doctor updated = doctorRepository.save(found);
        return new ResponseEntity<>(doctorMapper.buildDetailedDoctorResponse(updated), HttpStatus.ACCEPTED);
    }

}
