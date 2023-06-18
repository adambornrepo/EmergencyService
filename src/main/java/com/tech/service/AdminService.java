package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.entites.abstracts.Employee;
import com.tech.entites.concretes.Admin;
import com.tech.entites.enums.UniqueField;
import com.tech.exception.custom.ForbiddenAccessException;
import com.tech.exception.custom.UnsuitableRequestException;
import com.tech.exception.custom.ResourceNotFoundException;
import com.tech.mapper.AdminMapper;
import com.tech.payload.request.register.AdminRegistrationRequest;
import com.tech.payload.request.update.AdminUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedAdminResponse;
import com.tech.payload.response.simple.SimpleAdminResponse;
import com.tech.repository.AdminRepository;
import com.tech.security.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final CheckAndCoordinationService coordinationService;
    private final ApiMessages apiMessages;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<DetailedAdminResponse> getOneAdminByUniqueField(UniqueField searchIn, String value) {
        if (searchIn.equals(UniqueField.ID)) {
            if (!value.matches("\\d+")) {
                throw new UnsuitableRequestException(apiMessages.getMessage("error.format.id"));
            } else {
                return new ResponseEntity<>(
                        adminMapper.buildDetailedAdminResponse(getOneAdminById(Long.valueOf(value))), HttpStatus.OK
                );
            }
        }
        return switch (searchIn) {
            case PHONE_NUMBER -> new ResponseEntity<>(
                    adminMapper.buildDetailedAdminResponse(
                            adminRepository
                                    .findByPhoneNumberEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.admin.phone"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            case SSN -> new ResponseEntity<>(
                    adminMapper.buildDetailedAdminResponse(
                            adminRepository
                                    .findBySsnEquals(value)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                            String.format(apiMessages.getMessage("error.not.found.admin.ssn"), value
                                            )))
                    ),
                    HttpStatus.OK
            );
            default -> throw new UnsuitableRequestException(apiMessages.getMessage("error.invalid.search"));
        };
    }

    public Admin getOneAdminById(Long id) {
        return adminRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(apiMessages.getMessage("error.not.found.admin.id"), id)));
    }

    public Page<SimpleAdminResponse> getAllAdmin(Pageable pageable) {
        return adminRepository.findAll(pageable).map(adminMapper::buildSimpleAdminResponse);
    }

    public ResponseEntity<DetailedAdminResponse> saveAdmin(AdminRegistrationRequest request) {
        coordinationService.checkDuplicate(request.getSsn(), request.getPhoneNumber()); // ssn - phoneNum
        Admin admin = request.get();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(Role.ADMIN);
        Admin saved = adminRepository.save(admin);
        return new ResponseEntity<>(adminMapper.buildDetailedAdminResponse(saved), HttpStatus.CREATED);
    }

    public ResponseEntity<DetailedAdminResponse> updateAdmin(AdminUpdateRequest request, Long id, UserDetails userDetails) {
        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.ADMIN) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.admin.update"));
        }
        Admin found = getOneAdminById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        if (!found.getPhoneNumber().equals(request.getPhoneNumber())) {
            coordinationService.checkDuplicate(null, request.getPhoneNumber());
        }
        request.accept(found);
        found.setPassword(passwordEncoder.encode(found.getPassword()));
        Admin updated = adminRepository.save(found);
        return new ResponseEntity<>(adminMapper.buildDetailedAdminResponse(updated), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ApiResponse> deleteAdmin(Long id, UserDetails userDetails) {
        Employee employee = coordinationService.getOneEmployeeByUserDetails(userDetails);
        if (employee.getRole().equals(Role.ADMIN) && !employee.getId().equals(id)) {
            throw new ForbiddenAccessException(apiMessages.getMessage("error.forbidden.admin.delete"));
        }
        Admin found = getOneAdminById(id);
        if (found.isDisabled()) {
            throw new UnsuitableRequestException(String.format(apiMessages.getMessage("error.not.exists.id"), id));
        }
        String mark = System.currentTimeMillis() + "¨!¨";
        found.setSsn(mark + found.getSsn());
        found.setPhoneNumber(mark + found.getPhoneNumber());
        found.setDisabled(true);
        Admin deleted = adminRepository.save(found);
        return new ResponseEntity<ApiResponse>(
                ApiResponse.builder().success(true).message(apiMessages.getMessage("success.admin.delete")).build(),
                HttpStatus.OK
        );
    }


    public List<SimpleAdminResponse> getAllActiveAdmin() {
        return adminRepository.findByIsDisabled(false)
                .stream()
                .map(adminMapper::buildSimpleAdminResponse)
                .collect(Collectors.toList());
    }
}
