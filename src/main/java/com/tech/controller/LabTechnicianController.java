package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.LabTechRegistrationRequest;
import com.tech.payload.request.update.LabTechUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedLabTechResponse;
import com.tech.payload.response.simple.SimpleLabTechResponse;
import com.tech.service.LabTechnicianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/labTech")
@RequiredArgsConstructor
public class LabTechnicianController {

    private final LabTechnicianService labTechService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','lab_tech:read')")
    public ResponseEntity<DetailedLabTechResponse> getOneLabTech(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return labTechService.getOneLabTechByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','lab_tech:read','psr:read')")
    public Page<SimpleLabTechResponse> getAllLabTech(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return labTechService.getAllLabTech(page, size, sort, type);
    }
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','lab_tech:read','psr:read')")
    public List<SimpleLabTechResponse> getAllActiveLabTech() {
        return labTechService.getAllActiveLabTech();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedLabTechResponse> saveLabTech(@Valid @RequestBody LabTechRegistrationRequest request) {
        return labTechService.saveLabTech(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','lab_tech:update')")
    public ResponseEntity<DetailedLabTechResponse> updateLabTech(@Valid @RequestBody LabTechUpdateRequest request, @RequestParam("id") Long id) {
        return labTechService.updateLabTech(request, id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','lab_tech:delete')")
    public ResponseEntity<ApiResponse> deleteLabTech(@RequestParam("id") Long id) {
        return labTechService.deleteLabTech(id);
    }


}
