package com.tech.controller;

import com.tech.entites.enums.UniqueField;
import com.tech.payload.request.register.AdminRegistrationRequest;
import com.tech.payload.request.update.AdminUpdateRequest;
import com.tech.payload.response.*;
import com.tech.payload.response.detailed.DetailedAdminResponse;
import com.tech.payload.response.simple.SimpleAdminResponse;
import com.tech.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "AdminController",
        description = "Admin Controller is responsible for performing operations related to Admins."
)
@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "Get One Admin",
            description = "This method can be executed by Super Admin or Admin with read authority. " +
                    "Retrieves detailed information about a specific Admin based on unique field and value. " +
                    "This method can be executed by providing one of the following values: ID, SSN or PHONE_NUMBER"
    )
    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('super_admin:read', 'admin:read')")
    public ResponseEntity<DetailedAdminResponse> getOneAdmin(@RequestParam("in") UniqueField searchIn, @RequestParam("val") String value) {
        return adminService.getOneAdminByUniqueField(searchIn, value);
    }

    @Operation(
            summary = "Get All Admins",
            description = "Retrieves a paginated list of all Admins. " +
                    "This method can be executed by Super Admin or Admin with read authority."
    )
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('super_admin:read', 'admin:read')")
    public Page<SimpleAdminResponse> getAllAdmin(
            @PageableDefault(page = 0, size = 10, sort = "ssn", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return adminService.getAllAdmin(pageable);
    }

    @Operation(
            summary = "Get All Active Admins",
            description = "Retrieves a list of all active Admins. " +
                    "This method can be executed by Super Admin or Admin with read authority."
    )
    @GetMapping("/getAll/active")
    @PreAuthorize("hasAnyAuthority('super_admin:read', 'admin:read')")
    public List<SimpleAdminResponse> getAllActiveAdmin() {
        return adminService.getAllActiveAdmin();
    }

    @Operation(
            summary = "Register Admin",
            description = "Registers a new Admin. This method can be executed by Super Admin."
    )
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('super_admin:create')")
    public ResponseEntity<DetailedAdminResponse> saveAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        return adminService.saveAdmin(request);
    }

    @Operation(
            summary = "Update Admin",
            description = "Updates the information of an existing Admin based on the specified ID. " +
                    "This method can be executed by Super Admin or Admin with update authority. " +
                    "Super Admin has authority over all admin accounts, while Admin can only update their own account."
    )
    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('super_admin:update','admin:update')")
    public ResponseEntity<DetailedAdminResponse> updateAdmin(
            @Valid @RequestBody AdminUpdateRequest request,
            @RequestParam("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return adminService.updateAdmin(request, id, userDetails);
    }

    @Operation(
            summary = "Delete Admin",
            description = "Deletes an Admin based on the specified ID. " +
                    "This method can be executed by Super Admin or Admin with delete authority. " +
                    "Super Admin has authority over all admin accounts, while Admin can only delete their own account."
    )
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('super_admin:delete','admin:delete')")
    public ResponseEntity<ApiResponse> deleteAdmin(@RequestParam("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return adminService.deleteAdmin(id, userDetails);
    }


}
