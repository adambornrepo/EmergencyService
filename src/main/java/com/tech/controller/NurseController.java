package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.NurseRegistrationRequest;
import com.tech.payload.request.update.NurseUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedNurseResponse;
import com.tech.payload.response.simple.SimpleNurseResponse;
import com.tech.service.NurseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/nurse")
@RequiredArgsConstructor
public class NurseController {

    private final NurseService nurseService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','nurse:read')")
    public ResponseEntity<DetailedNurseResponse> getOneNurse(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return nurseService.getOneNurseByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','nurse:read','psr:read')")
    public Page<SimpleNurseResponse> getAllNurse(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return nurseService.getAllNurse(page, size, sort, type);
    }

    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('admin:read','chief:read','nurse:read','psr:read')")
    public List<SimpleNurseResponse> getAllActiveNurse() {
        return nurseService.getAllActiveNurse();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('admin:create','chief:create')")
    public ResponseEntity<DetailedNurseResponse> saveNurse(@Valid @RequestBody NurseRegistrationRequest request) {
        return nurseService.saveNurse(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('admin:update','nurse:update')")
    public ResponseEntity<DetailedNurseResponse> updateNurse(@Valid @RequestBody NurseUpdateRequest request, @RequestParam("id") Long id) {
        return nurseService.updateNurse(request, id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('admin:delete','chief:delete','nurse:delete')")
    public ResponseEntity<ApiResponse> deleteNurse(@RequestParam("id") Long id) {
        return nurseService.deleteNurse(id);
    }


}
