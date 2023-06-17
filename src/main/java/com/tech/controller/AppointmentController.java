package com.tech.controller;

import com.tech.payload.request.AppointmentCreationRequest;
import com.tech.payload.request.update.AppointmentUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedAppointmentResponse;
import com.tech.payload.response.simple.SimpleAppointmentResponse;
import com.tech.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','psr:read')")
    public ResponseEntity<DetailedAppointmentResponse> getOneDetailedAppointmentById(@RequestParam("id") Long id) {
        return appointmentService.getOneDetailedAppointmentById(id);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllAppointment(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return appointmentService.getAllAppointment(page, size, sort, type);
    }

    @GetMapping("/getAll/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllAppointmentByDoctorId(
            @RequestParam("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return appointmentService.getAllAppointmentByDoctorId(id, page, size, sort, type);
    }

    @GetMapping("/getAll/inProgress/doctor")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllInProgressAppointmentByDoctorId(
            @RequestParam("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return appointmentService.getAllInProgressAppointmentByDoctorId(id, page, size, sort, type);
    }


    @GetMapping("/getAll/patient")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','nurse:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllAppointmentByPatientId(
            @RequestParam("ssn") String ssn,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return appointmentService.getAllAppointmentByPatientSsn(ssn, page, size, sort, type);
    }

    @GetMapping("/getAll/date")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public Page<SimpleAppointmentResponse> getAllAppointmentByDate(
            @RequestParam("on") LocalDate date,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return appointmentService.getAllAppointmentByDate(date, page, size, sort, type);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:create','psr:create')")
    public ResponseEntity<SimpleAppointmentResponse> saveAppointment(@Valid @RequestBody AppointmentCreationRequest request) {
        return appointmentService.saveAppointment(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','psr:update')")
    public ResponseEntity<DetailedAppointmentResponse> updateAppointment(@Valid @RequestBody AppointmentUpdateRequest request, @RequestParam("id") Long id) {
        return appointmentService.updateAppointment(request, id);
    }

    @PatchMapping("/cancel")
    @PreAuthorize("hasAnyAuthority('admin:update','psr:update')")
    public ResponseEntity<ApiResponse> cancelAppointment(@RequestParam("id") Long id) {
        return appointmentService.cancelAppointment(id);
    }

    @PatchMapping("/complete")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update','doctor:update','nurse:update')")
    public ResponseEntity<ApiResponse> completeAppointment(@RequestParam("id") Long id) {
        return appointmentService.completeAppointment(id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','psr:delete')")
    public ResponseEntity<ApiResponse> deleteAppointment(@RequestParam("id") Long id) {
        return appointmentService.deleteAppointment(id);
    }

}
