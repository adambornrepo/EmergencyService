package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.PatientRegistrationRequest;
import com.tech.payload.request.update.PatientUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedPatientResponse;
import com.tech.payload.response.simple.SimplePatientResponse;
import com.tech.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public ResponseEntity<DetailedPatientResponse> getOnePatient(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return patientService.getOnePatientByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimplePatientResponse> getAllPatient(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return patientService.getAllPatient(page, size, sort, type);
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create','psr:create')")
    public ResponseEntity<DetailedPatientResponse> savePatient(@Valid @RequestBody PatientRegistrationRequest request) {
        return patientService.savePatient(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update','psr:update')")
    public ResponseEntity<DetailedPatientResponse> updatePatient(@Valid @RequestBody PatientUpdateRequest request, @RequestParam("ssn") String ssn) {
        return patientService.updatePatient(request, ssn);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','psr:delete')")
    public ResponseEntity<ApiResponse> deletePatient(@RequestParam("ssn") String ssn) {
        return patientService.deletePatient(ssn);
    }


}
