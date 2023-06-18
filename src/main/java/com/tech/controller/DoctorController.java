package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.entites.enums.Zone;
import com.tech.payload.request.register.DoctorRegistrationRequest;
import com.tech.payload.request.update.DoctorUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedDoctorResponse;
import com.tech.payload.response.simple.SimpleDoctorResponse;
import com.tech.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dr")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read')")
    public ResponseEntity<DetailedDoctorResponse> getOneDoctor(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return doctorService.getOneDoctorByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleDoctorResponse> getAllDoctor(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return doctorService.getAllDoctor(page, size, sort, type);
    }

    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','psr:read')")
    public List<SimpleDoctorResponse> getAllActiveDoctor(@RequestParam(required = false, value = "zone") Zone zone) {
        return doctorService.getAllActiveDoctor(zone);
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedDoctorResponse> saveDoctor(@Valid @RequestBody DoctorRegistrationRequest request) {
        return doctorService.saveDoctor(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update','doctor:update')")
    public ResponseEntity<DetailedDoctorResponse> updateDoctor(
            @Valid @RequestBody DoctorUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return doctorService.updateDoctor(request, id, userDetails);
    }

    @PatchMapping("/update/zone")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update')")
    public ResponseEntity<DetailedDoctorResponse> updateDoctorZone(
            @RequestParam("to") Zone zone,
            @RequestParam("id") Long id) {
        return doctorService.updateDoctorZone(zone, id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','doctor:delete')")
    public ResponseEntity<ApiResponse> deleteDoctor(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return doctorService.deleteDoctor(id, userDetails);
    }

    @PatchMapping("/assign/chief")
    @PreAuthorize("hasAnyAuthority('admin:update','chief:update')")
    public ResponseEntity<DetailedDoctorResponse> toAssignChiefPhysician(@RequestParam("id") Long id) {
        return doctorService.assignChiefPhysician(id);
    }


}
