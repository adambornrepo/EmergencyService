package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.Doctor;
import com.tech.entites.enums.Role;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.DoctorMapper;
import com.tech.payload.request.register.DoctorRegistrationRequest;
import com.tech.payload.request.update.DoctorUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.payload.response.simple.SimpleDoctorResponse;
import com.tech.repository.DoctorRepository;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;

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
                                            String.format(apiMessages.getMessage("error.not.found.doctor.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    doctorMapper.buildDetailedDoctorResponse(
                            doctorRepository.findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.doctor.ssn"), value
                                            )))
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
        doctor.setPassword(doctor.getPassword());// TODO: 6.06.2023 Encode
        doctor.setRole(Role.DOCTOR);
        Doctor saved = doctorRepository.save(doctor);
        return new ResponseEntity<>(doctorMapper.buildDetailedDoctorResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedDoctorResponse> updateDoctor(DoctorUpdateRequest request, Long id) {
        Doctor found = getOneDoctorById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(found.getPassword());// TODO: 6.06.2023 Encode
        Doctor updated = doctorRepository.save(found);
        return new ResponseEntity<>(doctorMapper.buildDetailedDoctorResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteDoctor(Long id) {
        Doctor found = getOneDoctorById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        Doctor deleted = doctorRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.doctor.delete")).build(),
                HttpStatus.OK
        );
    }
}
