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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/get")
    public ResponseEntity<DetailedPatientResponse> getOnePatient(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return patientService.getOnePatientByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    public Page<SimplePatientResponse> getAllPatient(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return patientService.getAllPatient(page, size, sort, type);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedPatientResponse> savePatient(@Valid @RequestBody PatientRegistrationRequest request) {
        return patientService.savePatient(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedPatientResponse> updatePatient(@Valid @RequestBody PatientUpdateRequest request, @RequestParam("ssn") String ssn) {
        return patientService.updatePatient(request, ssn);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deletePatient(@RequestParam("ssn") String ssn) {
        return patientService.deletePatient(ssn);
    }


}
