package com.tech.controller;

import com.tech.payload.request.PrescriptionCreationRequest;
import com.tech.payload.request.update.PrescriptionUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedPrescriptionResponse;
import com.tech.payload.response.simple.SimplePrescriptionResponse;
import com.tech.service.PrescriptionService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "PrescriptionController",
        description = "Prescription Controller is responsible for performing operations related to Prescriptions"
)
@RestController
@RequestMapping("api/v1/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Operation(
            summary = "Get a detailed prescription by ID",
            description = "This method retrieves a detailed prescription by its ID. " +
                    "This method can be executed by Admin, Chief, Doctor, or Nurse with read authority."
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public ResponseEntity<DetailedPrescriptionResponse> getOnePrescriptionById(@RequestParam("id") Long id) {
        return prescriptionService.getOneDetailedPrescriptionById(id);
    }

    @Operation(
            summary = "Get all prescriptions",
            description = "This method retrieves all prescriptions. " +
                    "This method can be executed by Admin or Chief with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimplePrescriptionResponse> getAllPrescription(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return prescriptionService.getAllPrescription(pageable);
    }

    @Operation(
            summary = "Get all prescriptions by doctor ID",
            description = "This method retrieves all prescriptions associated with a specific doctor. " +
                    "This method can be executed by Admin, Chief, or Doctor with read authority."
    )
    @GetMapping("/getAll/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByDoctorId(
            @RequestParam("id") Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return prescriptionService.getAllPrescriptionByDoctorId(id, pageable, userDetails);
    }

    @Operation(
            summary = "Get a prescription by appointment ID",
            description = "This method retrieves a prescription associated with a specific appointment. " +
                    "This method can be executed by Admin, Chief, Doctor, or Nurse with read authority."
    )
    @GetMapping("/get/appointment")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public ResponseEntity<SimplePrescriptionResponse> getPrescriptionByAppointmentId(@RequestParam("id") Long id) {
        return prescriptionService.getPrescriptionByAppointmentId(id);
    }


    @Operation(
            summary = "Export a prescription to PDF",
            description = "This method exports a prescription to a PDF file. " +
                    "This method can be executed by Admin, Chief, or Doctor with create authority."
    )
    @PostMapping("/save/pdf")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create','doctor:create')")
    public ResponseEntity<ApiResponse> exportOnePrescriptionToPdf(@RequestParam("id") Long prescriptionId) {
        return prescriptionService.exportPrescriptionToPdfById(prescriptionId);
    }

    @Operation(
            summary = "Send a prescription via email",
            description = "This method sends a prescription to the patient's email. " +
                    "This method can be executed by Admin, Chief, or Doctor with create authority."
    )
    @PostMapping("/send/mail")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create','doctor:create')")
    public ResponseEntity<ApiResponse> sendPrescriptionToEmailById(@RequestParam("id") Long prescriptionId) {
        return prescriptionService.sendPrescriptionToPatientEmailById(prescriptionId);
    }


    @Operation(
            summary = "Get all prescriptions by patient ID",
            description = "This method retrieves all prescriptions associated with a specific patient by their ID. " +
                    "This method can be executed by Admin, Chief, Doctor, or Nurse with read authority."
    )
    @GetMapping("/getAll/patient")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByPatientId(
            @RequestParam("id") Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return prescriptionService.getAllPrescriptionByPatientId(id, pageable);
    }

    @Operation(
            summary = "Get all prescriptions by patient SSN",
            description = "This method retrieves all prescriptions associated with a specific patient by their SSN. " +
                    "This method can be executed by Admin, Chief, Doctor, or Nurse with read authority."
    )
    @GetMapping("/getAll/patient/ssn")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByPatientSsn(
            @RequestParam("val") String ssn,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return prescriptionService.getAllPrescriptionByPatientSsn(ssn, pageable);
    }

    @Operation(
            summary = "Create a new prescription",
            description = "This method creates a new prescription. " +
                    "This method can be executed by Chief or Doctor with create authority. " +
                    "But only the doctor who is assigned to the appointment can create a prescription."
    )
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('chief:create','doctor:create')")
    public ResponseEntity<?> savePrescription(
            @Valid @RequestBody PrescriptionCreationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return prescriptionService.savePrescription(request, userDetails);
    }


    @Operation(
            summary = "Update an existing prescription",
            description = "This method updates an existing prescription. " +
                    "This method can be executed by Chief or Doctor with update authority. " +
                    "But only the doctor who is assigned to the appointment can update a prescription."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('chief:update','doctor:update')")
    public ResponseEntity<DetailedPrescriptionResponse> updatePrescription(
            @Valid @RequestBody PrescriptionUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return prescriptionService.updatePrescription(request, id, userDetails);
    }

    @Operation(
            summary = "Delete a prescription",
            description = "This method deletes a prescription. " +
                    "This method can be executed by Chief or Doctor with delete authority. " +
                    "But only the doctor who is assigned to the appointment can delete a prescription."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('chief:delete','doctor:delete')")
    public ResponseEntity<ApiResponse> deletePrescription(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return prescriptionService.deletePrescription(id, userDetails);
    }


}
