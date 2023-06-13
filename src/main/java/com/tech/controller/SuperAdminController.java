package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.SuperAdminRegistrationRequest;
import com.tech.payload.request.update.SuperAdminUpdateRequest;
import com.tech.payload.response.ApiResponse;
import com.tech.payload.response.detailed.DetailedSuperAdminResponse;
import com.tech.service.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @GetMapping("/get")
    public ResponseEntity<DetailedSuperAdminResponse> getOneSuperAdmin(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return superAdminService.getOneSuperAdminByUniqueField(searchIn, value);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedSuperAdminResponse> saveSuperAdmin(@Valid @RequestBody SuperAdminRegistrationRequest request) {
        return superAdminService.saveSuperAdmin(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedSuperAdminResponse> updateSuperAdmin(@Valid @RequestBody SuperAdminUpdateRequest request, @RequestParam("id") Long id) {
        return superAdminService.updateSuperAdmin(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteSuperAdmin(@RequestParam("id") Long id) {
        return superAdminService.deleteSuperAdmin(id);
    }


}
