package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.Representative;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.ForbiddenAccessException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.RepresentativeMapper;
import com.tech.payload.request.register.RepresentativeRegistrationRequest;
import com.tech.payload.request.update.RepresentativeUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedRepresentativeResponse;
import com.tech.payload.response.simple.SimpleRepresentativeResponse;
import com.tech.repository.RepresentativeRepository;
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
public class RepresentativeService {
    private final RepresentativeRepository representativeRepository;
    private final RepresentativeMapper representativeMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<DetailedRepresentativeResponse> getOneRepresentativeByUniqueField(UniqueField searchIn, String value) {
        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        representativeMapper.buildDetailedRepresentativeResponse(getOneRepresentativeById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }
        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    representativeMapper.buildDetailedRepresentativeResponse(
                            representativeRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.psr.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    representativeMapper.buildDetailedRepresentativeResponse(
                            representativeRepository
                                    .findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.psr.ssn"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };
    }

    public Representative getOneRepresentativeById(Long id) {
        return representativeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.psr.id"), id)));
    }

    public Page<SimpleRepresentativeResponse> getAllRepresentative(int page, int size, String sort, String type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).ascending());
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by("isDisabled").ascending().and(Sort.by(sort)).descending());
        }
        return representativeRepository.findAll(pageable).map(representativeMapper::buildSimpleRepresentativeResponse);
    }

    public ResponseEntity<DetailedRepresentativeResponse> saveRepresentative(RepresentativeRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        Representative representative = request.get();
        representative.setPassword(passwordEncoder.encode(representative.getPassword()));
        representative.setRole(Role.PSR);
        Representative saved = representativeRepository.save(representative);
        log.info("PSR saved: {}", saved);
        return new ResponseEntity<>(representativeMapper.buildDetailedRepresentativeResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedRepresentativeResponse> updateRepresentative(RepresentativeUpdateRequest request, Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.PSR) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.psr.update"));
        }

        Representative found = getOneRepresentativeById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(passwordEncoder.encode(found.getPassword()));
        Representative updated = representativeRepository.save(found);
        log.info("PSR updated: {}", updated);
        return new ResponseEntity<>(representativeMapper.buildDetailedRepresentativeResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteRepresentative(Long id, UserDetails userDetails) {

        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.PSR) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.psr.delete"));
        }

        Representative found = getOneRepresentativeById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        Representative deleted = representativeRepository.save(found);
        log.info("PSR deleted: {}", deleted);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.psr.delete")).build(),
                HttpStatus.OK
        );
    }

    public List<SimpleRepresentativeResponse> getAllActiveRepresentative() {
        return representativeRepository.findByIsDisabled(false)
                .stream()
                .map(representativeMapper::buildSimpleRepresentativeResponse)
                .collect(Collectors.toList());
    }
}
