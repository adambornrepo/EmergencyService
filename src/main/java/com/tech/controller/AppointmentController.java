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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @GetMapping("/get")
    public ResponseEntity<DetailedAppointmentResponse> getOneDetailedAppointmentById(@RequestParam("id") Long id) {
        return appointmentService.getOneDetailedAppointmentById(id);
    }

    @GetMapping("/getAll")
    public Page<SimpleAppointmentResponse> getAllAppointment(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "DESC") String type
    ) {
        return appointmentService.getAllAppointment(page, size, sort, type);
    }

    @GetMapping("/getAll/doctor")
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
    public ResponseEntity<SimpleAppointmentResponse> saveAppointment(@Valid @RequestBody AppointmentCreationRequest request) {
        return appointmentService.saveAppointment(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedAppointmentResponse> updateAppointment(@Valid @RequestBody AppointmentUpdateRequest request, @RequestParam("id") Long id) {
        return appointmentService.updateAppointment(request, id);
    }

    @PatchMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelAppointment(@RequestParam("id") Long id) {
        return appointmentService.cancelAppointment(id);
    }

    @PatchMapping("/complete")
    public ResponseEntity<ApiResponse> completeAppointment(@RequestParam("id") Long id) {
        return appointmentService.completeAppointment(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteAppointment(@RequestParam("id") Long id) {
        return appointmentService.deleteAppointment(id);
    }

}
