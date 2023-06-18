package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.RadiologyTechRegistrationRequest;
import com.tech.payload.request.update.RadiologyTechUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedRadiologyTechResponse;
import com.tech.payload.response.simple.SimpleRadiologyTechResponse;
import com.tech.service.RadiologyTechnicianService;
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
@RequestMapping("api/v1/radioTech")
@RequiredArgsConstructor
public class RadiologyTechnicianController {

    private final RadiologyTechnicianService radiologyTechService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','radiology_tech:read')")
    public ResponseEntity<DetailedRadiologyTechResponse> getOneRadioTech(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return radiologyTechService.getOneRadiologyTechByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleRadiologyTechResponse> getAllRadiologyTech(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return radiologyTechService.getAllRadiologyTech(page, size, sort, type);
    }

    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','doctor:read','radiology_tech:read','psr:read')")
    public List<SimpleRadiologyTechResponse> getAllActiveRadiologyTech() {
        return radiologyTechService.getAllActiveRadiologyTech();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedRadiologyTechResponse> saveRadiologyTech(@Valid @RequestBody RadiologyTechRegistrationRequest request) {
        return radiologyTechService.saveRadiologyTech(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','radiology_tech:update')")
    public ResponseEntity<DetailedRadiologyTechResponse> updateRadiologyTech(
            @Valid @RequestBody RadiologyTechUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return radiologyTechService.updateRadiologyTech(request, id, userDetails);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','radiology_tech:delete')")
    public ResponseEntity<ApiResponse> deleteRadiologyTech(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return radiologyTechService.deleteRadiologyTech(id, userDetails);
    }


}
