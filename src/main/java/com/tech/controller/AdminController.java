package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.AdminRegistrationRequest;
import com.tech.payload.request.update.AdminUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedAdminResponse;
import com.tech.payload.response.simple.SimpleAdminResponse;
import com.tech.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get")
    public ResponseEntity<DetailedAdminResponse> getOneAdmin(@RequestParam("in") UniqueField searchIn, @RequestParam("val")String value) {
        return adminService.getOneAdminByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    public Page<SimpleAdminResponse> getAllAdmin(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "ssn") String sort,
            @RequestParam(value = "type", defaultValue = "ASC") String type
    ) {
        return adminService.getAllAdmin(page, size, sort, type);
    }

    @PostMapping("/register")
    public ResponseEntity<DetailedAdminResponse> saveAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        return adminService.saveAdmin(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<DetailedAdminResponse> updateAdmin(@Valid @RequestBody AdminUpdateRequest request, @RequestParam("id") Long id) {
        return adminService.updateAdmin(request, id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteAdmin(@RequestParam("id") Long id) {
        return adminService.deleteAdmin(id);
    }


}
