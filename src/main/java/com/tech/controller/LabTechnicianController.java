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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/labTech")
@RequiredArgsConstructor
public class LabTechnicianController {

    private final LabTechnicianService labTechService;

    @GetMapping("/get")
    public ResponseEntity<DetailedLabTechResponse> getOneLabTech(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return labTechService.getOneLabTechByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    public Page<SimpleLabTechResponse> getAllLabTech(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return labTechService.getAllLabTech(page, size, sort, type);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedLabTechResponse> saveLabTech(@Valid @RequestBody LabTechRegistrationRequest request) {
        return labTechService.saveLabTech(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedLabTechResponse> updateLabTech(@Valid @RequestBody LabTechUpdateRequest request, @RequestParam("id") Long id) {
        return labTechService.updateLabTech(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteLabTech(@RequestParam("id") Long id) {
        return labTechService.deleteLabTech(id);
    }


}
