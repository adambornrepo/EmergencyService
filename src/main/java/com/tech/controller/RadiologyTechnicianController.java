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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/radioTech")
@RequiredArgsConstructor
public class RadiologyTechnicianController {

    private final RadiologyTechnicianService radiologyTechService;

    @GetMapping("/get")
    public ResponseEntity<DetailedRadiologyTechResponse> getOneRadioTech(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return radiologyTechService.getOneRadiologyTechByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    public Page<SimpleRadiologyTechResponse> getAllRadiologyTech(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return radiologyTechService.getAllRadiologyTech(page, size, sort, type);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedRadiologyTechResponse> saveRadiologyTech(@Valid @RequestBody RadiologyTechRegistrationRequest request) {
        return radiologyTechService.saveRadiologyTech(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedRadiologyTechResponse> updateRadiologyTech(@Valid @RequestBody RadiologyTechUpdateRequest request, @RequestParam("id") Long id) {
        return radiologyTechService.updateRadiologyTech(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteRadiologyTech(@RequestParam("id") Long id) {
        return radiologyTechService.deleteRadiologyTech(id);
    }


}
