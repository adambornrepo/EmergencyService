package com.tech.controller;

import com.tech.payload.request.PrescriptionCreationRequest;
import com.tech.payload.request.update.PrescriptionUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedPrescriptionResponse;
import com.tech.payload.response.simple.SimplePrescriptionResponse;
import com.tech.service.PrescriptionService;
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

@RestController
@RequestMapping("api/v1/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public ResponseEntity<DetailedPrescriptionResponse> getOnePrescriptionById(@RequestParam("id") Long id) {
        return prescriptionService.getOneDetailedPrescriptionById(id);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimplePrescriptionResponse> getAllPrescription(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return prescriptionService.getAllPrescription(pageable);
    }

    @GetMapping("/getAll/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByDoctorId(
            @RequestParam("id") Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return prescriptionService.getAllPrescriptionByDoctorId(id, pageable, userDetails);
    }

    @GetMapping("/get/appointment")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public ResponseEntity<SimplePrescriptionResponse> getPrescriptionByAppointmentId(@RequestParam("id") Long id) {
        return prescriptionService.getPrescriptionByAppointmentId(id);
    }

    @GetMapping("/getAll/patient")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByPatientId(
            @RequestParam("id") Long id,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return prescriptionService.getAllPrescriptionByPatientId(id, pageable);
    }

    @GetMapping("/getAll/patient/ssn")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByPatientSsn(
            @RequestParam("val") String ssn,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return prescriptionService.getAllPrescriptionByPatientSsn(ssn, pageable);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('chief:create','doctor:create')")
    public ResponseEntity<DetailedPrescriptionResponse> savePrescription(
            @Valid @RequestBody PrescriptionCreationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return prescriptionService.savePrescription(request, userDetails);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('chief:update','doctor:update')")
    public ResponseEntity<DetailedPrescriptionResponse> updatePrescription(
            @Valid @RequestBody PrescriptionUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return prescriptionService.updatePrescription(request, id, userDetails);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('chief:delete','doctor:delete')")
    public ResponseEntity<ApiResponse> deletePrescription(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return prescriptionService.deletePrescription(id, userDetails);
    }


}
