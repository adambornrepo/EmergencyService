package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.RepresentativeRegistrationRequest;
import com.tech.payload.request.update.RepresentativeUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedRepresentativeResponse;
import com.tech.payload.response.simple.SimpleRepresentativeResponse;
import com.tech.service.RepresentativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/psr")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeService representativeService;

    @GetMapping("/get")
    public ResponseEntity<DetailedRepresentativeResponse> getOneRepresentative(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return representativeService.getOneRepresentativeByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    public Page<SimpleRepresentativeResponse> getAllRepresentative(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return representativeService.getAllRepresentative(page, size, sort, type);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedRepresentativeResponse> saveRepresentative(@Valid @RequestBody RepresentativeRegistrationRequest request) {
        return representativeService.saveRepresentative(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedRepresentativeResponse> updateRepresentative(@Valid @RequestBody RepresentativeUpdateRequest request, @RequestParam("id") Long id) {
        return representativeService.updateRepresentative(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteRepresentative(@RequestParam("id") Long id) {
        return representativeService.deleteRepresentative(id);
    }


}
