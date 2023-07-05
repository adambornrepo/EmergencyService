package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.Patient;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.ConflictException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.PatientMapper;
import com.tech.payload.request.register.PatientRegistrationRequest;
import com.tech.payload.request.update.PatientUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedPatientResponse;
import com.tech.payload.response.simple.SimplePatientResponse;
import com.tech.repository.PatientRepository;
import com.tech.security.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j//logger
@Transactional
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final ApiMessages apiMessages;

    public ResponseEntity<DetailedPatientResponse> getOnePatientByUniqueField(UniqueField searchIn, String value) {
        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        patientMapper.buildDetailedPatientResponse(getOnePatientById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }
        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    patientMapper.buildDetailedPatientResponse(
                            patientRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.patient.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    patientMapper.buildDetailedPatientResponse(getOnePatientBySsn(value)), HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };
    }

    public Patient getOnePatientById(Long id) {
        return patientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.patient.id"), id)));
    }

    public Patient getOnePatientBySsn(String ssn) {
        return patientRepository
                .findBySsnEquals(ssn)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.patient.ssn"), ssn)));
    }


    public Page<SimplePatientResponse> getAllPatient(Pageable pageable) {
        return patientRepository.findAll(pageable).map(patientMapper::buildSimplePatientResponse);
    }

    public ResponseEntity<DetailedPatientResponse> savePatient(PatientRegistrationRequest request) {
        checkDuplicateForPatient(request.getSsn(), request.getPhoneNumber(), request.getEmail()); // ssn - phoneNum - email
        Patient patient = request.get();
        patient.setRole(Role.PATIENT);
        Patient saved = patientRepository.save(patient);
        return new ResponseEntity<>(patientMapper.buildDetailedPatientResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedPatientResponse> updatePatient(PatientUpdateRequest request, String ssn) {
        Patient found = getOnePatientBySsn(ssn);
        if (found.getSsn().contains("¨!¨")) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.ssn"), ssn));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            checkDuplicateForPatient(null, request.getPhoneNumber(), null);
        }
        if (found.getEmail() == null || !found.getEmail().equals(request.getEmail())) {
            checkDuplicateForPatient(null, null, request.getEmail());
        }
        request.accept(found);
        Patient updated = patientRepository.save(found);
        return new ResponseEntity<>(patientMapper.buildDetailedPatientResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deletePatient(String ssn) {
        Patient found = getOnePatientBySsn(ssn);
        if (found.getSsn().contains("¨!¨")) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.ssn"), ssn));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setEmail(mark + found.getEmail());
        Patient deleted = patientRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.patient.delete")).build(),
                HttpStatus.OK
        );
    }

    private void checkDuplicateForPatient(String ssn, String phoneNumber, String email) {
        if (StringUtils.hasText(ssn) && patientRepository.existsBySsn(ssn)) {
            throw new ConflictException(String.format(apiMessages.getMessage("error.conflict.ssn"), ssn));
        }
        if (StringUtils.hasText(phoneNumber) && patientRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ConflictException(String.format(apiMessages.getMessage("error.conflict.phone"), phoneNumber));
        }
        if (StringUtils.hasText(email) && patientRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(apiMessages.getMessage("error.conflict.email"), email));
        }
    }

}
