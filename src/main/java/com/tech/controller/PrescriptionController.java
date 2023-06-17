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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return prescriptionService.getAllPrescription(page, size, sort, type);
    }

    @GetMapping("/getAll/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByDoctorId(
            @RequestParam("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return prescriptionService.getAllPrescriptionByDoctorId(id, page, size, sort, type);
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
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return prescriptionService.getAllPrescriptionByPatientId(id, page, size, sort, type);
    }
    @GetMapping("/getAll/patient/ssn")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read')")
    public Page<SimplePrescriptionResponse> getAllPrescriptionByPatientSsn(
            @RequestParam("val") String ssn,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return prescriptionService.getAllPrescriptionByPatientSsn(ssn, page, size, sort, type);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('chief:create','doctor:create')")
    public ResponseEntity<DetailedPrescriptionResponse> savePrescription(@Valid @RequestBody PrescriptionCreationRequest request) {
        return prescriptionService.savePrescription(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('chief:update','doctor:update')")
    public ResponseEntity<DetailedPrescriptionResponse> updatePrescription(@Valid @RequestBody PrescriptionUpdateRequest request, @RequestParam("id") Long id) {
        return prescriptionService.updatePrescription(request, id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('chief:delete','doctor:delete')")
    public ResponseEntity<ApiResponse> deletePrescription(@RequestParam("id") Long id) {
        return prescriptionService.deletePrescription(id);
    }


}
