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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('super_admin:read', 'admin:read')")
    public ResponseEntity<DetailedAdminResponse> getOneAdmin(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return adminService.getOneAdminByUniqueField(searchIn, value);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('super_admin:read', 'admin:read')")
    public Page<SimpleAdminResponse> getAllAdmin(
            @PageableDefault(size = 10, page = 0, sort = {"ssn"}, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return adminService.getAllAdmin(pageable);
    }

    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('super_admin:read', 'admin:read')")
    public List<SimpleAdminResponse> getAllActiveAdmin() {
        return adminService.getAllActiveAdmin();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('super_admin:create')")
    public ResponseEntity<DetailedAdminResponse> saveAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        return adminService.saveAdmin(request);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<DetailedAdminResponse> updateAdmin(@Valid @RequestBody AdminUpdateRequest request, @RequestParam("id") Long id) {
        return adminService.updateAdmin(request, id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse> deleteAdmin(@RequestParam("id") Long id) {
        return adminService.deleteAdmin(id);
    }


}
