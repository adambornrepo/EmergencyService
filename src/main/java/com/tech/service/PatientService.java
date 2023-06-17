package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.Patient;
import com.tech.entites.enums.UniqueField;
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
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final CheckAndCoordinationService coordinationService;
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


    public Page<SimplePatientResponse> getAllPatient(int page, int size, String sort, String type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return patientRepository.findAll(pageable).map(patientMapper::buildSimplePatientResponse);
    }

    // TODO: 7.06.2023 buyuk sorun
    public ResponseEntity<DetailedPatientResponse> savePatient(PatientRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
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
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
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
        Patient deleted = patientRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.patient.delete")).build(),
                HttpStatus.OK
        );
    }
}
