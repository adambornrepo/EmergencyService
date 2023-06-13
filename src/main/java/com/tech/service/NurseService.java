package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.concretes.Nurse;
import com.tech.entites.enums.Role;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.NurseMapper;
import com.tech.payload.request.register.NurseRegistrationRequest;
import com.tech.payload.request.update.NurseUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedNurseResponse;
import com.tech.payload.response.simple.SimpleNurseResponse;
import com.tech.repository.NurseRepository;
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
public class NurseService {
    private final NurseRepository nurseRepository;
    private final NurseMapper nurseMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;

    public ResponseEntity<DetailedNurseResponse> getOneNurseByUniqueField(UniqueField searchIn, String value) {
        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        nurseMapper.buildDetailedNurseResponse(getOneNurseById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }
        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    nurseMapper.buildDetailedNurseResponse(
                            nurseRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.nurse.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    nurseMapper.buildDetailedNurseResponse(
                            nurseRepository
                                    .findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.nurse.ssn"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };
    }

    public Nurse getOneNurseById(Long id) {
        return nurseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.nurse.id"), id)));
    }

    public Page<SimpleNurseResponse> getAllNurse(int page, int size, String sort, String type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).ascending());
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).descending());
        }
        return nurseRepository.findAll(pageable).map(nurseMapper::buildSimpleNurseResponse);
    }

    public ResponseEntity<DetailedNurseResponse> saveNurse(NurseRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        Nurse nurse = request.get();
        nurse.setPassword(nurse.getPassword());// TODO: 6.06.2023 Encode
        nurse.setRole(Role.NURSE);
        Nurse saved = nurseRepository.save(nurse);
        return new ResponseEntity<>(nurseMapper.buildDetailedNurseResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedNurseResponse> updateNurse(NurseUpdateRequest request, Long id) {
        Nurse found = getOneNurseById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(found.getPassword());// TODO: 6.06.2023 Encode
        Nurse updated = nurseRepository.save(found);
        return new ResponseEntity<>(nurseMapper.buildDetailedNurseResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteNurse(Long id) {
        Nurse found = getOneNurseById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        Nurse deleted = nurseRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.nurse.delete")).build(),
                HttpStatus.OK
        );
    }
}
