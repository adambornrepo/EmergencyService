package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.PatientRegistrationRequest;
import com.tech.payload.request.update.PatientUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedPatientResponse;
import com.tech.payload.response.simple.SimplePatientResponse;
import com.tech.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PatientController", description = "Patient Controller is responsible for performing operations related to Patients")
@RestController
@RequestMapping("api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(
            summary = "Get One Patient",
            description = "This method can be executed by Admin, Chief, or Psr with read authority. " +
                    "Retrieves detailed information about a specific Patient based on unique field and value. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public ResponseEntity<DetailedPatientResponse> getOnePatient(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return patientService.getOnePatientByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Get All Patients",
            description = "Retrieves a paginated list of all Patients. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimplePatientResponse> getAllPatient(
            @PageableDefault(page = 0, size = 10, sort = "ssn", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return patientService.getAllPatient(pageable);
    }

    @Operation(
            summary = "Register Patient",
            description = "Registers a new Patient. This method can be executed by Admin or Psr with create authority."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','psr:create')")
    public ResponseEntity<DetailedPatientResponse> savePatient(@Valid @RequestBody PatientRegistrationRequest request) {
        return patientService.savePatient(request);
    }

    @Operation(
            summary = "Update Patient",
            description = "Updates the information of an existing Patient based on the specified SSN. " +
                    "This method can be executed by Admin or Psr with update authority."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','psr:update')")
    public ResponseEntity<DetailedPatientResponse> updatePatient(
            @Valid @RequestBody PatientUpdateRequest request, @RequestParam("ssn") String ssn
    ) {
        return patientService.updatePatient(request, ssn);
    }

    @Operation(
            summary = "Delete Patient",
            description = "Deletes a Patient based on the specified SSN. " +
                    "This method can be executed by Admin or Psr with delete authority."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','psr:delete')")
    public ResponseEntity<ApiResponse> deletePatient(@RequestParam("ssn") String ssn) {
        return patientService.deletePatient(ssn);
    }


}
