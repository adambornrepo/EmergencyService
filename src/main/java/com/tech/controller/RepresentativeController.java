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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/psr")
@RequiredArgsConstructor
public class RepresentativeController {

    private final RepresentativeService representativeService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public ResponseEntity<DetailedRepresentativeResponse> getOneRepresentative(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return representativeService.getOneRepresentativeByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read')")
    public Page<SimpleRepresentativeResponse> getAllRepresentative(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return representativeService.getAllRepresentative(page, size, sort, type);
    }

    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','psr:read')")
    public List<SimpleRepresentativeResponse> getAllActiveRepresentative() {
        return representativeService.getAllActiveRepresentative();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedRepresentativeResponse> saveRepresentative(@Valid @RequestBody RepresentativeRegistrationRequest request) {
        return representativeService.saveRepresentative(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','psr:update')")
    public ResponseEntity<DetailedRepresentativeResponse> updateRepresentative(
            @Valid @RequestBody RepresentativeUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return representativeService.updateRepresentative(request, id, userDetails);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','psr:delete')")
    public ResponseEntity<ApiResponse> deleteRepresentative(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return representativeService.deleteRepresentative(id, userDetails);
    }


}
